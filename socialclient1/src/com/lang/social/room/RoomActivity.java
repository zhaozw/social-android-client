package com.lang.social.room;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.competition.CompetitionActivity;
import com.lang.social.competition.SocialGameConstants;
import com.lang.social.controllers.ServerController;
import com.lang.social.interfaces.GameCloseListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.GameType;
import com.lang.social.logic.User;
import com.lang.social.memorygame.MemoryGameActivity;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.teachstudy.StudentTeacherConstants;
import com.lang.social.teachstudy.lesson.StudentTeacherLessonActivity;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;

public class RoomActivity extends Activity implements RoomListener , GameCloseListener{
	
	private static final String TAG = "ROOM";
	public GameType mGameType;
	public static final String GameTypeKEY = "GameType";
	public static final String Default_Guest_Name = "Waiting for a player...";
	//private members
	//--------------------------------------------------------
	private	TextView tvUserGuest;
	private TextView tvUserHost;
	private TextView tvWaiting;
	private Button btnLaunch;
	private ProgressBar pbWaiting;
	private ProfilePictureView ppvUserHost;
	private ProfilePictureView ppvUserGuest;
	private ProgressDialog progressDialog;
	private User mPlayerHost;
	private User mPlayerGuest;
	private String mUserState;
	//--------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room);
		
		setViewsAndResources();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(" Waiting Room");
        getActionBar().setIcon(getResources().getDrawable(R.drawable.roomicon));

        mGameType = (GameType) getIntent().getSerializableExtra(RoomConstants.GameTypeKEY);
		mUserState = getIntent().getStringExtra(SocialGameConstants.IntentRoomStateKEY);
		mPlayerHost = (User) getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer1Key);
		
		tvUserHost.setText(mPlayerHost.getFullName());
		tvUserGuest.setText(Default_Guest_Name);
		
		if(mPlayerHost.isFacebookUser())
		{
			ppvUserHost.setProfileId(mPlayerHost.getProfileID());
		}
		
		if(mUserState.equals(SocialGameConstants.IntentRoomStateVALUEJoined))
		{
			tvWaiting.setText("Waiting for " + mPlayerHost.getFirstName() + " to launch game...");
			mPlayerGuest = (User) getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer2Key);
			if(mPlayerGuest.isFacebookUser())
			{
				ppvUserGuest.setProfileId(mPlayerGuest.getProfileID());
			}
			tvUserGuest.setText(mPlayerGuest.getFullName());
		}
		
		setBtnLaunchListener();
		createProgressDialog();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		IOCallBackHandler.getInstance().setRoomListener(this);
		IOCallBackHandler.getInstance().setGameCloseListener(this);
	}
	
	///NEW added in 15.6
	@Override
	public void onBackPressed() {
		handleExitRoomUserRequest();
	}

	
	private void handleExitRoomUserRequest() {
		new AlertDialog.Builder(this)
		    .setTitle("Exit Room")
		    .setMessage("Are you sure you want to exit the room?")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	if(mUserState.equals(SocialGameConstants.IntentRoomStateVALUECreated)){
		        		closeGameNotification();
		        	} else {
		        		notifyHostAboutGuestLeave();
		        	}
		        	Log.d(TAG, "finishing...");
		        	RoomActivity.this.finish();
		        }
		     })
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            //do nothing
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .show();	
	}

	private void notifyHostAboutGuestLeave() {
		JSONObject jsonToSend = new JSONObject();
		JSONUtils.setStringValue(jsonToSend, RoomConstants.GameTypeKEY, mGameType.toString());
		ServerController.sendJSONMessage(RoomConstants.GuestQuitGameNotification, jsonToSend);
	}

	private void closeGameNotification() {
		JSONObject jsonToSend = new JSONObject();
		if(mGameType != null) {
			JSONUtils.setStringValue(jsonToSend, RoomConstants.GameTypeKEY, mGameType.toString());
			ServerController.sendJSONMessage(RoomConstants.HostQuitGameNotification, jsonToSend);
		}
		
	}

	@Override
	public void onPlayerLeftGameEvent(JSONObject jsonResponse) {
		Log.d(TAG, "in onGameClosedEvent");
		if(mUserState.equals(SocialGameConstants.IntentRoomStateVALUEJoined)) {
			Log.d(TAG, "host left room");
			MyToaster.showToast(RoomActivity.this, "Room was closed by host!", Toast.LENGTH_SHORT);
			finish();
		} else {
			Log.d(TAG, "guest left room");
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showGuetLeftRoomToast();
					removeGuestFromScreen();
					hideReadyGraphic();
				}
			});
		}
	}

	private void hideReadyGraphic() {
		  tvWaiting.setText("Waiting...");
		  btnLaunch.setVisibility(View.INVISIBLE);
		  pbWaiting.setVisibility(View.VISIBLE);
		  btnLaunch.setEnabled(false);
		  findViewById(R.id.lineBelowWaiting).setVisibility(View.VISIBLE);
	}

	private void showGuetLeftRoomToast() {
		Toast.makeText(RoomActivity.this,
				mPlayerGuest.getFullName() + " left the room.",
				Toast.LENGTH_SHORT)
				.show();
	}

	private void removeGuestFromScreen() {
		ppvUserGuest.setProfileId("");
		tvUserGuest.setText(Default_Guest_Name);
	}

	@Override
	public void onPlayerJoinedRoomEvent(JSONObject jsonResponse) {
		Log.d(TAG, jsonResponse.toString());
		List<String> jsonKeys = Arrays.asList(new String[]{RoomConstants.Player2Key});
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult())
		{
			JSONObject player2Json = JSONUtils.getJSONObject(jsonResponse, RoomConstants.Player2Key);
			mPlayerGuest = new User(player2Json);
			showUserEnteredRoom();
		}
	}


	@Override
	public void onGameLaunchedRoomEvent(JSONObject jsonResponse) {
		Log.d(TAG, jsonResponse.toString());
		List<String> jsonKeys = null;
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		Intent intent = null;
		if(srp.isOkResult())
		{
			dismissProgressDialog();
			if(mGameType == GameType.HeadToHeadQuizGame) {
				User host = new User(JSONUtils.getJSONObject(jsonResponse, SocialGameConstants.IntentPlayer1Key));
				User guest = new User(JSONUtils.getJSONObject(jsonResponse, SocialGameConstants.IntentPlayer2Key));
				intent = new Intent(RoomActivity.this, CompetitionActivity.class);
				intent.putExtra(SocialGameConstants.IntentRoomStateKEY, mUserState);
				intent.putExtra(RoomConstants.GameTypeKEY, mGameType);
				intent.putExtra(SocialGameConstants.IntentPlayer1Key, host);
				intent.putExtra(SocialGameConstants.IntentPlayer2Key, guest);
			}
			else if(mGameType == GameType.StudentGame || mGameType == GameType.TeacherGame) {
				User Student = new User(JSONUtils.getJSONObject(jsonResponse, StudentTeacherConstants.IntentStudentKey));
				User Teacher = new User(JSONUtils.getJSONObject(jsonResponse, StudentTeacherConstants.IntentTeacherKey));
				intent = new Intent(RoomActivity.this, StudentTeacherLessonActivity.class);
				intent.putExtra(StudentTeacherConstants.IntentStudentKey, Student);
				intent.putExtra(StudentTeacherConstants.IntentTeacherKey, Teacher);
				intent.putExtra(StudentTeacherConstants.IntentRoomStateKEY, mUserState);
				String isTeacherHost = getIntent().getStringExtra(StudentTeacherConstants.IsTeacherHost);
				intent.putExtra(StudentTeacherConstants.IsTeacherHost, isTeacherHost);
			}
			else if(mGameType == GameType.MemoryGame) {
				User host = new User(JSONUtils.getJSONObject(jsonResponse, SocialGameConstants.IntentPlayer1Key));
				User guest = new User(JSONUtils.getJSONObject(jsonResponse, SocialGameConstants.IntentPlayer2Key));
				intent = new Intent(RoomActivity.this, MemoryGameActivity.class);
				intent.putExtra(SocialGameConstants.IntentRoomStateKEY, mUserState);
				intent.putExtra(RoomConstants.GameTypeKEY, mGameType);
				intent.putExtra(SocialGameConstants.IntentPlayer1Key, host);
				intent.putExtra(SocialGameConstants.IntentPlayer2Key, guest);
			}
			startActivity(intent);
			finish();
		}
	}


	
	private void showUserEnteredRoom() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showRoomReadyToLaunchGraphic();
				  tvUserGuest.setText(mPlayerGuest.getFullName());
				  if(mPlayerGuest.isFacebookUser()){
					  ppvUserGuest.setProfileId(mPlayerGuest.getProfileID());
				  }
			}
		});
	}

	private void showRoomReadyToLaunchGraphic() {
		  tvWaiting.setText("Ready!");
		  pbWaiting.setVisibility(View.GONE);
		  btnLaunch.setVisibility(View.VISIBLE);
		  btnLaunch.setEnabled(true);
		  findViewById(R.id.lineBelowWaiting).setVisibility(View.INVISIBLE);
	}
	

	
	private void dismissProgressDialog() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
			}
		});
	}
	
	
	private void setBtnLaunchListener() {
		btnLaunch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				notifyServerAboutGameLaunched();
			}
		});
	}
	
	private void notifyServerAboutGameLaunched() {
		progressDialog.show();
		JSONObject jsonToSend = new JSONObject();
		JSONUtils.setStringValue(jsonToSend, GameTypeKEY, mGameType.toString());
		ServerController.sendJSONMessage(RoomConstants.gameLaunchRequest, jsonToSend);
	}
	
	private void setViewsAndResources() {
		ppvUserHost = (ProfilePictureView) findViewById(R.id.ppvCompetitionHost);
		ppvUserGuest = (ProfilePictureView) findViewById(R.id.ppvCompetitionGuest);
		tvUserGuest = (TextView) findViewById(R.id.tvRoomPlayerGuest);
		tvUserHost = (TextView) findViewById(R.id.tvRoomPlayerHost);
		tvWaiting = (TextView) findViewById(R.id.tvWaiting);
		pbWaiting = (ProgressBar) findViewById(R.id.progressBarWaiting);
		btnLaunch = (Button) findViewById(R.id.btnLaunchCompetition);
		
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Jura-DemiBold.ttf");
        tvUserHost.setTypeface(tf);
        tvUserGuest.setTypeface(tf);
        tvWaiting.setTypeface(tf);
        btnLaunch.setTypeface(tf);
	}



	private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	    	handleExitRoomUserRequest();
	    	return true;
	        case R.id.action_settings:
	            return true;
	        case R.id.action_logout:
	        	new UserSessionManager(this).logOutUser();
	        	handleExitRoomUserRequest();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}


}
