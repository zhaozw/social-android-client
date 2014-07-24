package com.lang.social.competition;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.activities.GamesListActivity;
import com.lang.social.chat.ChatLineItem;
import com.lang.social.chat.ChatListener;
import com.lang.social.chat.CustomChatAdapter;
import com.lang.social.competition.CompetitionGame.PlayerNumber;
import com.lang.social.controllers.ServerController;
import com.lang.social.interfaces.GameCloseListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.room.RoomActivity;
import com.lang.social.room.RoomConstants;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;

public class CompetitionActivity extends Activity
			implements CompetitionActivityNotifier , ChatListener, GameCloseListener {
	
	//--------------------------------------------------------------------------------------
	private TextView tvPlayer2;
	private TextView tvPlayer1;
	//--------------------------------------------------------------------------------------
	private ProfilePictureView ppvPlayer1;
	private ProfilePictureView ppvPlayer2;
	//--------------------------------------------------------------------------------------
	private CompetitionGameFragment mCompetitionGameFragment = new CompetitionGameFragment();
	private CompetitionTimerFragment mCompetitionTimerFragment = new CompetitionTimerFragment();
	private FragmentManager mFragmentManager = getFragmentManager();
	//--------------------------------------------------------------------------------------
	private ImageView [] mPlayer1HeartsIVs = new ImageView[5];
	private ImageView [] mPlayer2HeartsIVs = new ImageView[5];
	//--------------------------------------------------------------------------------------
	private CompetitionPlayer mPlayer1;
	private CompetitionPlayer mPlayer2;
	//--------------------------------------------------------------------------------------
	private String mUserState;
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
    //----------------------------------------------
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_competition_layout);
		
		getViews();
		
		mUserState = getIntent().getStringExtra(SocialGameConstants.IntentRoomStateKEY);
		
		User user1 = (User)getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer1Key);
		mPlayer1 = new CompetitionPlayer(user1);
		User user2 = (User)getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer2Key);
		mPlayer2 = new CompetitionPlayer(user2);
		
		if(mPlayer1.getUser().isFacebookUser()){
			ppvPlayer1.setProfileId(mPlayer1.getUser().getProfileID());
		}
		if(mPlayer2.getUser().isFacebookUser()){
			ppvPlayer2.setProfileId(mPlayer2.getUser().getProfileID());
		}
		
		tvPlayer1.setText(mPlayer1.getUser().getFullName());
		tvPlayer2.setText(mPlayer2.getUser().getFullName());
		
		initializeNavigationDrawer();
		initHeadToHeadGame();
	}
	
	
	private void initHeadToHeadGame() {
		CompetitionGame competitionGame = new CompetitionGame(mPlayer1, mPlayer2);
		mCompetitionGameFragment.InitCompetitionGame(competitionGame);
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
	

	
	public String getUserState(){
		return mUserState;
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

	private void getViews() {
		ppvPlayer1 = (ProfilePictureView) findViewById(R.id.ppvCompetitionHost);
		ppvPlayer2 = (ProfilePictureView) findViewById(R.id.ppvCompetitionGuest);
		tvPlayer1 = (TextView) findViewById(R.id.tvCompetePlayer1);
		tvPlayer2 = (TextView) findViewById(R.id.tvCompetePlayer2);
		//--------------------------------------------------------------------------------------
		mPlayer1HeartsIVs[0] = (ImageView) findViewById(R.id.ivPlayer1Heart1);
		mPlayer1HeartsIVs[1] = (ImageView) findViewById(R.id.ivPlayer1Heart2);
		mPlayer1HeartsIVs[2] = (ImageView) findViewById(R.id.ivPlayer1Heart3);
		mPlayer1HeartsIVs[3] = (ImageView) findViewById(R.id.ivPlayer1Heart4);
		mPlayer1HeartsIVs[4] = (ImageView) findViewById(R.id.ivPlayer1Heart5);
		//--------------------------------------------------------------------------------------
		mPlayer2HeartsIVs[0] = (ImageView) findViewById(R.id.ivPlayer2Heart1);
		mPlayer2HeartsIVs[1] = (ImageView) findViewById(R.id.ivPlayer2Heart2);
		mPlayer2HeartsIVs[2] = (ImageView) findViewById(R.id.ivPlayer2Heart3);
		mPlayer2HeartsIVs[3] = (ImageView) findViewById(R.id.ivPlayer2Heart4);
		mPlayer2HeartsIVs[4] = (ImageView) findViewById(R.id.ivPlayer2Heart5);
		//--------------------------------------------------------------------------------------
		sendChatMsgButton = (Button)findViewById(R.id.ButtonSendChatMessage);
		chatLine = (EditText)findViewById(R.id.EditTextChatLine);
		listView = (ListView)findViewById(R.id.left_drawer_chat_listView);
	}
	
	private void showFragments() {
		mFragmentManager
			.beginTransaction()
				.replace(R.id.fragment_game_container, mCompetitionGameFragment, "gameFragment")
				//.replace(R.id.fragment_timer_container, mCompetitionTimerFragment, "timerFragment")
					.commit();
	}

	private void restartCountDownTimer() {
		mCompetitionTimerFragment.RestartCountDownTimer();
	}
	
	@Override
	public void OnPlayersSwitchedTurns(PlayerNumber playerNum) {
		//restartCountDownTimer();
	}
	
	@Override
	public void OnPlayerAnsweredCorrect(PlayerNumber playerNum) {
		showHeart(playerNum);
	}

	@Override
	public void OnPlayerAnsweredWrong(PlayerNumber playerNum) {
		hideHeart(playerNum);
	}
	

	private void showHeart(PlayerNumber playerNum) {
		if(PlayerNumber.Player1 == playerNum){
			int heartIndex = mPlayer1.getNumOfHearts();
			if(heartIndex < mPlayer1HeartsIVs.length) {
				mPlayer1HeartsIVs[heartIndex].setVisibility(View.VISIBLE);
			}
		}
		else{
			int heartIndex = mPlayer2.getNumOfHearts();
			if(heartIndex < mPlayer2HeartsIVs.length) {
				mPlayer2HeartsIVs[heartIndex].setVisibility(View.VISIBLE);
			}
		}
	}
	

	private void hideHeart(PlayerNumber playerNum) {
		if(PlayerNumber.Player1 == playerNum){
			int heartIndex = mPlayer1.getNumOfHearts() - 1;
			if(heartIndex >= 0) {
				mPlayer1HeartsIVs[heartIndex].setVisibility(View.INVISIBLE);
			}
		}
		else{
			int heartIndex = mPlayer2.getNumOfHearts() - 1;
			if(heartIndex >= 0) {
				mPlayer2HeartsIVs[heartIndex].setVisibility(View.INVISIBLE);
			}
		}
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // The action bar home/up action should open or close the drawer.
	    // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        
		//Handle presses on the action bar items
		switch (item.getItemId()) {
	    case android.R.id.home:
//	    	if(mUserState.equals(CompetitionConstants.IntentRoomStateVALUECreated)){
//	    		notifyOtherPlayerAboutLeave();
//	    	}
	    	finish();
	    	return true;
        case R.id.action_logout:
        	new UserSessionManager(this).logOutUser();
//	    	if(mUserState.equals(CompetitionConstants.IntentRoomStateVALUECreated)){
//	    		notifyOtherPlayerAboutLeave();
//	    	}
            return true;
		case R.id.action_settings:
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
	protected void onStart() {
		super.onStart();
		IOCallBackHandler.getInstance().setChatListener(this);
		IOCallBackHandler.getInstance().setGameCloseListener(this);
		showFragments();
	}

	@Override
	protected void onStop() {
		super.onStop();
		//notifyOtherPlayerAboutLeave();
	}
    
//	private void notifyOtherPlayerAboutLeave() {
//		JSONObject jsonToSend = new JSONObject();
//		JSONUtils.setStringValue(jsonToSend, RoomConstants.GameTypeKEY, CompetitionConstants.HeadToHeadQuizGame);
//		ServerController.sendJSONMessage(RoomConstants.playerQuitGameNotification, jsonToSend);
//	}
	
	@Override
	public void onPlayerLeftGameEvent(JSONObject jsonResponse) {
		if(mUserState.equals(SocialGameConstants.IntentRoomStateVALUEJoined)) {
			MyToaster.showToast(CompetitionActivity.this, "The game was closed by the host!", Toast.LENGTH_SHORT);
		} else {
			MyToaster.showToast(CompetitionActivity.this, mPlayer2.getUser().getFullName() + " has left the game!", Toast.LENGTH_SHORT);
		}
		finish();
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
