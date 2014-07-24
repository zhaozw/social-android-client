package com.lang.social.teachstudy.lesson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.google.gson.JsonObject;
import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.User;
import com.lang.social.teachstudy.StudentTeacherActivity;
import com.lang.social.teachstudy.StudentTeacherConstants;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.ImageUtils;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;

public class StudentTeacherLessonActivity extends Activity implements LessonGameListener {
	private static final String TAG = "LessonActivity";
	private ActionBar m_ActionBar;
	
	private ImageView ivStudentMic;
	private ImageView ivTeacherMic;	
	private ImageView ivStudentSpeaker;
	private ImageView ivTeacherSpeaker;
	
	private StudentTeacherPhotosTwoWayFragment mPhotosTwoWayFragment = new StudentTeacherPhotosTwoWayFragment();
	private StudentWaitingForTeacherSelectFragment mWaitingForTeacherSelectionFragment;
	private LessonFragment mLessonFragment = new LessonFragment();
	private FragmentManager mFragmentManager = getFragmentManager();
	
	private User m_Teacher;
	private User m_Student;
	private String isTeacherHost;
	private String m_UserState;
	
	private int m_Points;
	
	boolean m_isLongedClicked = false;
	
	//--------Audio Chat Members---------------
	private MediaRecorder mRecorder = null;
	private FileInputStream  buffer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_teacher_lesson);
		
		setTitle(" Lesson");
		m_ActionBar = getActionBar();
		m_ActionBar.setIcon(getResources().getDrawable(R.drawable.lessonicon));
		
		m_Student = (User) getIntent().getSerializableExtra(StudentTeacherConstants.IntentStudentKey);
		m_Teacher = (User) getIntent().getSerializableExtra(StudentTeacherConstants.IntentTeacherKey);
		m_UserState = (String) getIntent().getSerializableExtra(StudentTeacherConstants.IntentRoomStateKEY);
		isTeacherHost = (String) getIntent().getSerializableExtra(StudentTeacherConstants.IsTeacherHost);
		
		setViews();
		setOnLongClicksMicrophonesListeners();
		handleRelevantHost();
		
		

	}
	
	private void setOnLongClicksMicrophonesListeners() {
		ivStudentMic.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ivStudentMic.setImageResource(R.drawable.microphonepressedicon);
				startRecording();
				m_isLongedClicked = true;
				return false;
			}
		});
		
		ivStudentMic.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP) {
					if(m_isLongedClicked) {
						ivStudentMic.setImageResource(R.drawable.microphoneicon);
						stopRecording();
					}
					m_isLongedClicked = false;
				}
				return false;
			}
		});
		
		ivTeacherMic.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ivTeacherMic.setImageResource(R.drawable.microphonepressedicon);
				startRecording();
				m_isLongedClicked = true;
				return false;
			}
		});
		
		ivTeacherMic.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP) {
					if(m_isLongedClicked) {
						ivTeacherMic.setImageResource(R.drawable.microphoneicon);
						stopRecording();
					}
					m_isLongedClicked = false;	
				}
				return false;
			}
		});	
	}
	
	private void handleRelevantHost() {
		if(isTeacherHost.equals(StudentTeacherConstants.TeacherNotHost) && m_UserState.equals(StudentTeacherConstants.IntentRoomStateVALUECreated)) {
			ivTeacherMic.setVisibility(View.GONE);
			showStudentFragmnet();
		}
		else if(isTeacherHost.equals(StudentTeacherConstants.TeacherNotHost) && m_UserState.equals(StudentTeacherConstants.IntentRoomStateVALUEJoined)) {
			ivStudentMic.setVisibility(View.GONE);
			showTeacherFragment();
		}
		else if(isTeacherHost.equals(StudentTeacherConstants.TeacherHost) && m_UserState.equals(StudentTeacherConstants.IntentRoomStateVALUECreated)) {
			ivStudentMic.setVisibility(View.GONE);
			showTeacherFragment();
		}
		else if(isTeacherHost.equals(StudentTeacherConstants.TeacherHost) && m_UserState.equals(StudentTeacherConstants.IntentRoomStateVALUEJoined)) {
			ivTeacherMic.setVisibility(View.GONE);
			showStudentFragmnet();
		}
		showLessonFragment();
	}

	private void showTeacherFragment() {
		mFragmentManager
			.beginTransaction()
				.replace(R.id.fragment_twoway_photos_container, mPhotosTwoWayFragment, "photosFragment")
					.commit();
	}
	
	private void showLessonFragment() {
		mFragmentManager
		.beginTransaction()
			.replace(R.id.fragment_lesson_container, mLessonFragment, "lessonFragment")
				.commit();
	}
	
	//Causes Crash For Some Reason !
	private void showStudentFragmnet() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("Teacher", m_Teacher);
		bundle.putSerializable("Student", m_Student);
		mWaitingForTeacherSelectionFragment = new StudentWaitingForTeacherSelectFragment();
		mWaitingForTeacherSelectionFragment.setArguments(bundle);
		
		mFragmentManager
			.beginTransaction()
				.replace(R.id.fragment_twoway_photos_container, mWaitingForTeacherSelectionFragment, "waitingTeacherSelectionFragment")
					.commit();
	}
    
	@Override
	protected void onStart() {
		super.onStart();
		IOCallBackHandler.getInstance().setCloseActiveLessonGame(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		updateUsersPoints();
	}

	
	private void updateUsersPoints() {
		JSONObject jsonToSend = new JSONObject();
		try {
			jsonToSend.put("points", m_Points);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServerController.sendJSONMessage("updatePointsRequest", jsonToSend);
		MyToaster.showToast(this, "You Have Earned " + m_Points + " Points!", Toast.LENGTH_LONG);
		
	}

	private void setViews() {
		ivStudentMic = (ImageView) findViewById(R.id.ivMicroPhoneStudent);
		ivTeacherMic = (ImageView) findViewById(R.id.ivMicroPhoneTeacher);
		
		//Speakers
		ivStudentSpeaker = (ImageView) findViewById(R.id.ivSpeakerStudent);
		ivTeacherSpeaker = (ImageView) findViewById(R.id.ivSpeakerTeacher);
		ivTeacherSpeaker.setVisibility(View.GONE);
		ivStudentSpeaker.setVisibility(View.GONE);
		ivStudentSpeaker.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
		ivTeacherSpeaker.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
		//--------------------------------------------------------------------------------------------------------------
		
		ProfilePictureView ppvStudent = (ProfilePictureView) findViewById(R.id.ppvLessonStudent);
		ProfilePictureView ppvTeacher = (ProfilePictureView) findViewById(R.id.ppvLessonTeacher);
		
		if(m_Student.isFacebookUser()) {
			ppvStudent.setProfileId(m_Student.getProfileID());
		}
		
		if(m_Teacher.isFacebookUser()) {
			ppvTeacher.setProfileId(m_Teacher.getProfileID());
		}
		
		TextView tvStudent = (TextView) findViewById(R.id.tvStudent);
		TextView tvTeacher = (TextView) findViewById(R.id.tvTeacher);
		
		tvTeacher.setText(m_Teacher.getFullName());
		tvStudent.setText(m_Student.getFullName());
		
		
		
		ImageView ivFlagStudent = (ImageView) findViewById(R.id.ivFlagStudent);
		ImageView ivFlagTeacher = (ImageView) findViewById(R.id.ivFlagTeacher);
		
		if(m_Student.getLearningLanguage().equals("sp") || m_Student.getLearningLanguage().equals("Spanish")) {
			ivFlagStudent.setImageResource(R.drawable.spain1);
		}
		else {	//Assuming there are only 2 languages, and the second is Israel
			ivFlagStudent.setImageResource(R.drawable.israel1);
		}
		
		if(m_Teacher.getLearningLanguage().equals("sp") || m_Teacher.getLearningLanguage().equals("Spanish")) {
			ivFlagTeacher.setImageResource(R.drawable.spain1);
		}
		else {	//Assuming there are only 2 languages, and the second is Israel
			ivFlagTeacher.setImageResource(R.drawable.israel1);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_teacher_game_study, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if(id == R.id.action_logout) {
			new UserSessionManager(this).logOutUser();
			sendServerMessageToCloseGame();
		}
		else if(id == android.R.id.home) {
			askUserIfHeWantsToExit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		askUserIfHeWantsToExit();
	}

	private void askUserIfHeWantsToExit() {	//Leaked Window PRoblem. Dont know why..
		new AlertDialog.Builder(this)
	    .setTitle("Quit Lesson ")
	    .setMessage("Are You Sure You Want To Quit Lesson ?")
	    .setPositiveButton("Quit!", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	sendServerMessageToCloseGame();
	        	StudentTeacherLessonActivity.this.finish();
	        }
	     })
	    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
		
	}

	private void sendServerMessageToCloseGame() {
		ServerController.sendJSONMessage("CloseActiveStudentTeacherGame", new JSONObject());
		
	}

	@Override
	public void onActiveGameClosed(JSONObject jsonResponse) {
		MyToaster.showToast(this, "Lesson Was Closed Because One Side Left The Game", Toast.LENGTH_SHORT);
	    finish();
	}
	
	//---------Audio Chat Related Functions Only--------------
	 private void startRecording() {
	        mRecorder = new MediaRecorder();
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        mRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() +"/audiorecordtest.3gp");
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

	        try {
	            mRecorder.prepare();
	        } catch (IOException e) {
	            Log.d("Audio Chat", "prepare() failed");
	        }
	        
	        mRecorder.start();
	    }
	 
	 
	 private void stopRecording() {
	       	mRecorder.stop();
	        mRecorder.release();
	        mRecorder = null;
	        try {
	        	buffer = new FileInputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/audiorecordtest.3gp"));
	        	byte[] bytes  = new byte[32768];	//At least 6 seconds of voice msg...
	        	buffer.read(bytes);
	        	buffer.close();
	        	String encoded = Base64.encodeToString(bytes , 0);      
	        	JSONObject jsonToSend = new JSONObject();
	        	jsonToSend.put("audio", encoded);
	        	ServerController.sendJSONMessage("AudioFile", jsonToSend);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	@Override
	public void onAudioMessageRecived(final JSONObject jsonResponse) {
		runOnUiThread(new Runnable() {
			 @Override
			 public void run() {
				String voiceMsg = JSONUtils.getStringFromJSON(jsonResponse, "audio", null);
				byte[] decoded = Base64.decode(voiceMsg, 0);
				try
				{
				    File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/audiorecordtest11.3gp");
				    FileOutputStream os = new FileOutputStream(file2, false);
				    os.write(decoded);
				    os.close();
				    MediaPlayer mediaPlayer = new MediaPlayer();
				    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				    mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() +"/audiorecordtest11.3gp");
				    mediaPlayer.prepare();
				    mediaPlayer.start();
				    setRelevantSpeakersOn();
				    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				        public void onCompletion(MediaPlayer mp) {
				        	ivTeacherSpeaker.setVisibility(View.GONE);
				        	ivStudentSpeaker.setVisibility(View.GONE);
				        }
				    });
				}
				catch (Exception e)
				{
				    e.printStackTrace();
				}
			 }
		 });
	}
	
	private void setRelevantSpeakersOn() {
		if(isTeacherHost.equals(StudentTeacherConstants.TeacherNotHost) && m_UserState.equals(StudentTeacherConstants.IntentRoomStateVALUECreated)) {
			ivTeacherSpeaker.setVisibility(View.VISIBLE);
		}
		else if(isTeacherHost.equals(StudentTeacherConstants.TeacherNotHost) && m_UserState.equals(StudentTeacherConstants.IntentRoomStateVALUEJoined)) {
			ivStudentSpeaker.setVisibility(View.VISIBLE);
		}
		else if(isTeacherHost.equals(StudentTeacherConstants.TeacherHost) && m_UserState.equals(StudentTeacherConstants.IntentRoomStateVALUECreated)) {
			ivStudentSpeaker.setVisibility(View.VISIBLE);
		}
		else if(isTeacherHost.equals(StudentTeacherConstants.TeacherHost) && m_UserState.equals(StudentTeacherConstants.IntentRoomStateVALUEJoined)) {
			ivTeacherSpeaker.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onImageResponse(JSONObject jsonResponse) {
		Log.d(TAG, "Changing the student fragment text");
		m_Points += 5;
		if(m_Points > 100) {
			m_Points = 100;
		}
		if(isTeacherHost.equals(StudentTeacherConstants.TeacherNotHost) && m_UserState.equals(StudentTeacherConstants.IntentRoomStateVALUECreated) ||
				isTeacherHost.equals(StudentTeacherConstants.TeacherHost) && m_UserState.equals(StudentTeacherConstants.IntentRoomStateVALUEJoined)) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(mWaitingForTeacherSelectionFragment != null) {
							mWaitingForTeacherSelectionFragment.changeText("Use The Audio Chat To Learn Pronounce The Word With Your Teacher!\n" +
									"You Can Also Ask For Another Picture.");
						}
						
						
					}
				});
		}
		
	}



}
