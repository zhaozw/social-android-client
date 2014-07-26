package com.lang.social.room;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.chat.ChatLineItem;
import com.lang.social.chat.ChatListener;
import com.lang.social.chat.CustomChatAdapter;
import com.lang.social.competition.CompetitionActivity;
import com.lang.social.competition.SocialGameConstants;
import com.lang.social.controllers.ServerController;
import com.lang.social.interfaces.GameCloseListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.GameType;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.memorygame.MemoryGameActivity;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.teachstudy.StudentTeacherConstants;
import com.lang.social.teachstudy.lesson.StudentTeacherLessonActivity;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;
import com.mikhaellopez.circularimageview.CircularImageView;

public class RoomActivity extends Activity implements RoomListener , GameCloseListener , ChatListener {
	
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
	
	private CircularImageView ivCompetitionHost;
	private CircularImageView ivCompetitionGuest;
	
	private ProgressDialog progressDialog;
	private User mPlayerHost;
	private User mPlayerGuest;
	private String mUserState;
	//--------------------------------------------------------
	
	//---------------------------------------------------------------------------------------
	//SideDrawer
	//---------------------------------------------------------------------------------------
	private EditText chatLine;
	private Button sendChatMsgButton;
	//private ArrayList<String> listItems = new ArrayList<String>();
	private ArrayList<ChatLineItem> listItems = new ArrayList<ChatLineItem>();
	//private ArrayAdapter<String> adapter;
	private CustomChatAdapter adapter;
	
	private ListView listView;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle = "Chat";
	
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
			setProfilePicture(ivCompetitionHost, mPlayerHost.getProfileID());
		}
		
		if(mUserState.equals(SocialGameConstants.IntentRoomStateVALUEJoined))
		{
			tvWaiting.setText("Waiting for " + mPlayerHost.getFirstName() + " to launch game...");
			mPlayerGuest = (User) getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer2Key);
			if(mPlayerGuest.isFacebookUser())
			{
				setProfilePicture(ivCompetitionGuest, mPlayerGuest.getProfileID());
			}
			tvUserGuest.setText(mPlayerGuest.getFullName());
		}
		
		initializeNavigationDrawer();
		setBtnLaunchListener();
		createProgressDialog();
	}
	

	
	private void initializeNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_chat);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_chat_listView);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
		setButtonChatListener();
		notifyServerToStartChat();

		adapter = new CustomChatAdapter(this, R.layout.custom_chat_item , listItems);
    	mDrawerList.setAdapter(adapter);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(getResources().getDrawable(R.drawable.chaticon));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(mTitle);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()     
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
	}


	private void vibrate(int miliSec){
		 Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		 v.vibrate(miliSec);
	}
	
	
	private void setButtonChatListener() {
		sendChatMsgButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String msgToSend = chatLine.getText().toString();
				JSONObject json = new JSONObject();
				chatLine.setText(" ");
				JSONUtils.setStringValue(json, "message", msgToSend);
				JSONUtils.setStringValue(json, "sender", UserController.getUser().getFullName());
				JSONUtils.setStringValue(json, "type",  "userMessage");
				ServerController.sendJSONMessage("message", json);
			}
		});
	}
	
	@Override
	public void onMsgRecived(JSONObject obj) {
		Log.d("Chat", "Recieved Message " + obj.toString());
		final String msg = JSONUtils.getStringFromJSON(obj, "message", null);
		final String sender = JSONUtils.getStringFromJSON(obj, "sender", null);
		String type = JSONUtils.getStringFromJSON(obj, "type", null);	
		ChatLineItem line = null;
		Log.d("Chat", UserController.getUser().getFullName());
		Log.d("Chat", sender);
		
		if(checkIfNeedToVibrate()){
			Log.d("Chat", "vibrating");
			vibrate(500);
		}
		
		if(UserController.getUser().getFullName().equals(sender)){
			Log.d("Chat", "inside if");
			line = new ChatLineItem(sender, msg, android.R.color.holo_green_light);
		}
		else{
			Log.d("Chat", "inside else");
			line = new ChatLineItem(sender, msg, android.R.color.holo_orange_light);
		}
    	listItems.add(line);
		runOnUiThread(new Runnable() {
		    public void run() {
				scrollMyListViewToBottom();
				adapter.notifyDataSetChanged();
		    }
		});
	}
	
	private boolean checkIfNeedToVibrate() {
		if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			  Log.d("Chat", "drawer open");
			  return false;
		}
		Log.d("Chat", "drawer closed");
		return true;
	}

	private void scrollMyListViewToBottom() {
		listView.post(new Runnable() {
	        @Override
	        public void run() {
	            // Select the last row so it will scroll into view...
	        	listView.setSelection(adapter.getCount() - 1);
	        }
	    });
	}

	public void notifyServerToStartChat() {
		JSONObject obj = new JSONObject();
		Log.d("Chat", "Notifying to server to start chat.");
		ServerController.sendJSONMessage("addUserToChat", obj);
	}

	
	
	@Override
	protected void onStart() {
		super.onStart();
		IOCallBackHandler.getInstance().setRoomListener(this);
		IOCallBackHandler.getInstance().setGameCloseListener(this);
		IOCallBackHandler.getInstance().setChatListener(this);
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
		ivCompetitionGuest.setImageBitmap(null);
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
					  setProfilePicture(ivCompetitionGuest, mPlayerGuest.getProfileID());
				  }
			}
		});
	}
	
	private void setProfilePicture(final CircularImageView iv, final String profileid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				 try {
					 //img_value = new URL("http://graph.facebook.com/1118208362/picture?type=large");
					 URL MyProfilePicURL = new URL("https://graph.facebook.com/" +  profileid + "/picture?type=large&width=100&height=100");
					 Bitmap mIcon1 = BitmapFactory.decodeStream(MyProfilePicURL.openConnection().getInputStream());
					 onProfileImageRecieved(mIcon1, iv);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void onProfileImageRecieved(final Bitmap icon, final CircularImageView iv) {
		 runOnUiThread(new Runnable() {
			public void run() {
				iv.setImageBitmap(icon);
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
		ivCompetitionHost = (CircularImageView) findViewById(R.id.ivCompetitionHost);
		ivCompetitionGuest = (CircularImageView) findViewById(R.id.ivCompetitionGuest);
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
        
		sendChatMsgButton = (Button)findViewById(R.id.ButtonSendChatMessage);
		chatLine = (EditText)findViewById(R.id.EditTextChatLine);
		listView = (ListView)findViewById(R.id.left_drawer_chat_listView);
	}



	private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // The action bar home/up action should open or close the drawer.
	    // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}


}
