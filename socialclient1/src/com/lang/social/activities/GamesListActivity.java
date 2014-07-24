package com.lang.social.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.lang.social.R;
import com.lang.social.adapters.UserHostsAdapter;
import com.lang.social.competition.SocialGameListItem;
import com.lang.social.competition.SocialGameConstants;
import com.lang.social.competition.SocialLearnCreateMenuListener;
import com.lang.social.competition.SocialLearnJoinMenuListener;
import com.lang.social.controllers.ServerController;
import com.lang.social.interfaces.GamesListListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.GameType;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.room.RoomActivity;
import com.lang.social.room.RoomConstants;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.JSONUtils;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class GamesListActivity extends Activity 
	implements GamesListListener, SocialLearnCreateMenuListener, SocialLearnJoinMenuListener {
	
	//----------------------------------------------------------------------------------------------------------
	private static final String TAG = "Competition";
	private static final String roomIDKey = "GameRoomID";
	//----------------------------------------------------------------------------------------------------------
	private ArrayList<SocialGameListItem> mGameItems = new ArrayList<SocialGameListItem>();
	private PullToRefreshListView pullToRefreshView;
	private UserHostsAdapter AdapterHostGames;
	private ProgressDialog mProgressDialog;
	private GameType mGameType = GameType.HeadToHeadQuizGame;
	//----------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);
        
        createProgressDialog();
        setListViewListeners();
       
        String gameType = getIntent().getStringExtra(SocialLearnMenuActivity.GameTypeKey);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        if(gameType.equals(GameType.HeadToHeadQuizGame.toString())){
    	   getActionBar().setTitle("Head To Head");
    	   mGameType = GameType.HeadToHeadQuizGame;
    	   getActionBar().setIcon(getResources().getDrawable(R.drawable.timericon3));
        } else  if(gameType.equals(GameType.MemoryGame.toString())) {
    		getActionBar().setTitle("Memory Game");
    		mGameType = GameType.MemoryGame;
    		getActionBar().setIcon(getResources().getDrawable(R.drawable.memorygameicon3));
        }

    	String jsonStrUserHosts = getIntent().getStringExtra(SocialGameConstants.jsonUserHostsKEY);
    	JSONObject jsonHosts = JSONUtils.newJSONObject(jsonStrUserHosts);
		JSONArray jsonUserHosts = JSONUtils.getJSONArray(jsonHosts, SocialGameConstants.jsonUserHostsKEY);
		mGameItems = JSONUtils.ParseJsonHostsUsers(jsonUserHosts);
		AdapterHostGames = new UserHostsAdapter(this, R.layout.game_list_row, mGameItems);
		pullToRefreshView.setAdapter(AdapterHostGames);
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	getUpdatedGamesList();
    	pullToRefreshView.refreshDrawableState();
    }
    
	private void setListViewListeners() {
		pullToRefreshView = (PullToRefreshListView) findViewById(R.id.lvList);
        pullToRefreshView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				getUpdatedGamesList();
			}
        });
        pullToRefreshView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int which, long arg3) {
				requestJoinGame(which);
			}
		}); 
	}

	private void getUpdatedGamesList() {
		JSONObject jsonToSend = new JSONObject();
		IOCallBackHandler.getInstance().setGamesListListener(this);
		JSONUtils.setStringValue(jsonToSend, RoomConstants.GameTypeKEY, mGameType.toString());
		ServerController.sendJSONMessage(SocialLearnMenuActivity.userGameHostsRequest, jsonToSend);
	}

	@Override
	public void onGameListRecieved(JSONObject JSONHostGames) {
		Log.d(TAG, JSONHostGames.toString());
		List<String> jsonKeys = Arrays.asList(new String[]{SocialGameConstants.jsonUserHostsKEY});
		ServerResponseParser srp = new ServerResponseParser(JSONHostGames, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult())
		{
			JSONArray jsonUserHosts = JSONUtils.getJSONArray(JSONHostGames, SocialGameConstants.jsonUserHostsKEY);
			mGameItems = JSONUtils.ParseJsonHostsUsers(jsonUserHosts);
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AdapterHostGames.clear();
				AdapterHostGames.addAll(mGameItems);
				AdapterHostGames.notifyDataSetChanged();
				pullToRefreshView.onRefreshComplete();
			}
		});
	}
	
	
	//REQUESTS
	//--------------------------------------------------------------------------
	private void requestJoinGame(int which) {
		SocialGameListItem gameItem = mGameItems.get(which);
		JSONObject jsonToSend = JSONUtils.CreateJSON(roomIDKey, gameItem.getRoomID());
		
		IOCallBackHandler.getInstance().setSocialLearnCreateMenuListener(null);
		IOCallBackHandler.getInstance().setSocialLearnJoinMenuListener(this);
		
		JSONUtils.setStringValue(jsonToSend, "GameType", mGameType.toString());
		ServerController.sendJSONMessage(RoomConstants.joinGameRequest, jsonToSend);
	}

	private void requestOpenNewGameFromServer() {
		Log.d(TAG, "Sending startNewGame Request");
		
		IOCallBackHandler.getInstance().setSocialLearnJoinMenuListener(null);
		IOCallBackHandler.getInstance().setSocialLearnCreateMenuListener(this);
		
		JSONObject json = new JSONObject();
		JSONUtils.setStringValue(json, SocialLearnMenuActivity.GameTypeKey, mGameType.toString());
		ServerController.sendJSONMessage(RoomConstants.startGameRequest, json);
		mProgressDialog.show();
	}
	//---------------------------------------------------------------------------
	
	//RESPONSES
	//---------------------------------------------------------------------------
	@Override
	public void OnJoinGameResponse(JSONObject jsonResponse) { 
		Log.d(TAG, jsonResponse.toString());
		List<String> jsonKeys = null;
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult())
		{
			User playerHost = new User(JSONUtils.getJSONObject(jsonResponse, SocialGameConstants.IntentPlayer1Key));
	    	Intent intent = new Intent(this, RoomActivity.class);
	    	intent.putExtra(SocialGameConstants.IntentRoomStateKEY, "Joined");
	    	intent.putExtra(SocialGameConstants.IntentPlayer1Key, playerHost);
	    	intent.putExtra(RoomConstants.GameTypeKEY, mGameType);
	    	intent.putExtra(SocialGameConstants.IntentPlayer2Key, UserController.getUser());
	    	startActivity(intent);
		}
	}
	

	@Override
	public void OnGameStartResponse(JSONObject jsonResponse) {
		Log.d(TAG, "in OnGameStartResponse");
		mProgressDialog.dismiss();
		List<String> jsonKeys = null;
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult())
		{
	    	Intent intent = new Intent(this, RoomActivity.class);
	    	intent.putExtra(SocialGameConstants.IntentRoomStateKEY, "Created");
	    	intent.putExtra(RoomConstants.GameTypeKEY, mGameType);
	    	User currUser = UserController.getUser();
	    	intent.putExtra(SocialGameConstants.IntentPlayer1Key, currUser);
	    	startActivity(intent);
		}
	}
	//---------------------------------------------------------------------------
	
	private void createProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(true);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		finish();
	    		return true;
	        case R.id.action_new_game:
	        	requestOpenNewGameFromServer();
	            return true;
	        case R.id.action_logout:
	        	new UserSessionManager(this).logOutUser();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.find_game, menu);
	    return super.onCreateOptionsMenu(menu);
	}


}







