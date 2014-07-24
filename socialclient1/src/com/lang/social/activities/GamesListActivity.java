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
import com.lang.social.competition.CompetitionConstants;
import com.lang.social.competition.CompetitionErrorHandler;
import com.lang.social.competition.CompetitionGameItem;
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
import com.lang.social.teachstudy.StudentTeacherConstants;
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
	private ArrayList<CompetitionGameItem> mCompetitionGameItems = new ArrayList<CompetitionGameItem>();
	private PullToRefreshListView pullToRefreshView;
	private UserHostsAdapter AdapterHostGames;
	private ProgressDialog mProgressDialog;
	private GameType mGameType = GameType.HeadToHeadQuizGame;
	private CompetitionErrorHandler mCompetitionErrorHandler = new CompetitionErrorHandler(); 
	//----------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);
        
        createProgressDialog();
        setListViewListeners();
       
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("Competition");
        
    	String jsonStrUserHosts = getIntent().getStringExtra(CompetitionConstants.jsonUserHostsKEY);
    	JSONObject jsonHosts = JSONUtils.newJSONObject(jsonStrUserHosts);
		JSONArray jsonUserHosts = JSONUtils.getJSONArray(jsonHosts, CompetitionConstants.jsonUserHostsKEY);
		mCompetitionGameItems = JSONUtils.ParseJsonHostsUsers(jsonUserHosts);
		AdapterHostGames = new UserHostsAdapter(this, R.layout.game_list_row, mCompetitionGameItems);
		
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
		
		List<String> jsonKeys = Arrays.asList(new String[]{CompetitionConstants.jsonUserHostsKEY});
		ServerResponseParser srp = new ServerResponseParser(JSONHostGames, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult())
		{
			JSONArray jsonUserHosts = JSONUtils.getJSONArray(JSONHostGames, CompetitionConstants.jsonUserHostsKEY);
			mCompetitionGameItems = JSONUtils.ParseJsonHostsUsers(jsonUserHosts);
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AdapterHostGames.clear();
				AdapterHostGames.addAll(mCompetitionGameItems);
				AdapterHostGames.notifyDataSetChanged();
				pullToRefreshView.onRefreshComplete();
			}
		});
	}
	
	
	//REQUESTS
	//--------------------------------------------------------------------------
	private void requestJoinGame(int which) {
		CompetitionGameItem gameItem = mCompetitionGameItems.get(which);
		JSONObject jsonToSend = JSONUtils.CreateJSON(roomIDKey, gameItem.getRoomID());
		
		IOCallBackHandler.getInstance().setSocialLearnCreateMenuListener(null);
		IOCallBackHandler.getInstance().setSocialLearnJoinMenuListener(this);
		
		JSONUtils.setStringValue(jsonToSend, "GameType", mGameType.toString());
		ServerController.sendJSONMessage(RoomConstants.joinGameRequest, jsonToSend);
	}

	private void requestOpenNewCompetitionGameFromServer() {
		Log.d(TAG, "Sending startNewCompetitionGame Request");
		
		IOCallBackHandler.getInstance().setSocialLearnJoinMenuListener(null);
		IOCallBackHandler.getInstance().setSocialLearnCreateMenuListener(this);
		
		JSONObject json = new JSONObject();
		JSONUtils.setStringValue(json, "GameType", mGameType.toString());
		ServerController.sendJSONMessage(RoomConstants.startGameRequest, json);
		mProgressDialog.show();
	}
	//---------------------------------------------------------------------------
	
	//RESPONSES
	//---------------------------------------------------------------------------
	@Override
	public void OnCompetitionJoinGameResponse(JSONObject jsonResponse) { 
		Log.d(TAG, jsonResponse.toString());
		List<String> jsonKeys = null;
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult())
		{
			User playerHost = new User(JSONUtils.getJSONObject(jsonResponse, CompetitionConstants.IntentPlayer1Key));
	    	Intent intent = new Intent(this, RoomActivity.class);
	    	intent.putExtra(CompetitionConstants.IntentRoomStateKEY, "Joined");
	    	intent.putExtra(CompetitionConstants.IntentPlayer1Key, playerHost);
	    	intent.putExtra(RoomConstants.GameTypeKEY, mGameType);
	    	intent.putExtra(CompetitionConstants.IntentPlayer2Key, UserController.getUser());
	    	startActivity(intent);
		}
	}
	

	@Override
	public void OnCompetitionGameStartResponse(JSONObject jsonResponse) {
		Log.d(TAG, "in OnCompetitionGameStartResponse");
		mProgressDialog.dismiss();
		List<String> jsonKeys = null;
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult())
		{
	    	Intent intent = new Intent(this, RoomActivity.class);
	    	intent.putExtra(CompetitionConstants.IntentRoomStateKEY, "Created");
	    	intent.putExtra(RoomConstants.GameTypeKEY, mGameType);
	    	User currUser = UserController.getUser();
	    	intent.putExtra(CompetitionConstants.IntentPlayer1Key, currUser);
	    	startActivity(intent);
		}
		else 
		{
			mCompetitionErrorHandler.OnFailedToCreateNewGame();
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
	        	requestOpenNewCompetitionGameFromServer();
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







