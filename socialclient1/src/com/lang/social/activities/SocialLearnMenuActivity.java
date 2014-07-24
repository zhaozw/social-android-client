package com.lang.social.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lang.social.R;
import com.lang.social.adapters.LearnFeatureListAdapter;
import com.lang.social.controllers.ServerController;
import com.lang.social.exceptions.JSONExceptionHandler;
import com.lang.social.interfaces.GamesListListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.items.LearnFeatureMenuItem;
import com.lang.social.logic.GameType;
import com.lang.social.memorygame.MemoryGameActivity;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.room.RoomConstants;
import com.lang.social.teachstudy.StudentTeacherActivity;
import com.lang.social.usermanager.UserSessionManager;

public class SocialLearnMenuActivity extends Activity implements GamesListListener{
	
	public static final String userGameHostsRequest = "userGameHostsRequest";
	public static final String userGameHostsResponse = "userGameHostsResponse";
	public static final String jsonUserHosts = "jsonUserHosts";
	
	private LearnFeatureListAdapter socialLearnListAdapter;
	private ArrayList<LearnFeatureMenuItem> socialLearnItems;
	
	private GameType mGameType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_learn_menu_layout);
		
		socialLearnItems = new ArrayList<LearnFeatureMenuItem>();
		socialLearnItems.add(new LearnFeatureMenuItem(R.drawable.timericon3, "Competition", "1 VS 1 Match!"));
		socialLearnItems.add(new LearnFeatureMenuItem(R.drawable.studentteacher, "Teacher/Student", "Teach/Study With Other Learner!"));
		socialLearnItems.add(new LearnFeatureMenuItem(R.drawable.teamicon, "Team Power", "Solve Questions Together!"));
		socialLearnItems.add(new LearnFeatureMenuItem(R.drawable.timericon1, "Memory Game", "Test your memory skills!"));
		createListItems(socialLearnItems);
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("Social Learn");
        getActionBar().setIcon(getResources().getDrawable(R.drawable.learnmenuactivityicon));
	
	}



	private void createListItems(List<LearnFeatureMenuItem> selfLearnItems) {
		socialLearnListAdapter = new LearnFeatureListAdapter(this, R.layout.learn_feature_list_row, selfLearnItems);
        ListView listViewSocialLearn = (ListView)findViewById(R.id.listViewLearnFeatures);
        listViewSocialLearn.setAdapter(socialLearnListAdapter);
        listViewSocialLearn.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int which,
					long arg3) {
				switch(which){
					case 0:
						mGameType = GameType.HeadToHeadQuizGame;
						sendGamesListFetchRequestToServer(mGameType);
						break;
					case 1:
						mGameType = GameType.StudentTeacher;
						sendGamesListFetchRequestToServer(mGameType);
						Intent intent = new Intent(SocialLearnMenuActivity.this, StudentTeacherActivity.class);
//						intent.putExtra(jsonUserHosts, jsonHosts.toString());
						startActivity(intent);
						break;
					case 3:
						mGameType = GameType.MemoryGame;
						//sendGamesListFetchRequestToServer(mGameType);
						Intent i = new Intent(SocialLearnMenuActivity.this, MemoryGameActivity.class);
//						intent.putExtra(jsonUserHosts, jsonHosts.toString());
						startActivity(i);
						break;
				}
			}
		});
	}
	
	@Override
	public void onGameListRecieved(JSONObject jsonHosts) {
		Log.d("HOSTS", jsonHosts.toString());
		List<String> jsonKeys = Arrays.asList(new String[]{RoomConstants.JsonUserHostsKEY});
		ServerResponseParser srp = new ServerResponseParser(jsonHosts, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult())
		{
			Intent intent = new Intent(this, GamesListActivity.class);
			intent.putExtra(jsonUserHosts, jsonHosts.toString());
			startActivity(intent);
		}
	}


	private void sendGamesListFetchRequestToServer(GameType mGameType) {
		JSONObject jsonToSend = new JSONObject();
		if(mGameType == GameType.HeadToHeadQuizGame) {
			try {
				
				IOCallBackHandler.getInstance().setGamesListListener(this);
				jsonToSend.put("GameType", mGameType.toString());
				ServerController.sendJSONMessage(userGameHostsRequest, jsonToSend);

			} catch (JSONException ex) {
				new JSONExceptionHandler()
					.handleException(ex)
					.setErrorMessage("HOSTS","Error in sendGamesListFetchRequestToServer");
			}
		}
		else if(mGameType == GameType.StudentTeacher) {
			try {
				
				IOCallBackHandler.getInstance().setGamesListListener(this);
				jsonToSend.put("GameType", mGameType.toString());
				ServerController.sendJSONMessage(userGameHostsRequest, jsonToSend);

			} catch (JSONException ex) {
				new JSONExceptionHandler()
					.handleException(ex)
					.setErrorMessage("HOSTS","Error in sendGamesListFetchRequestToServer");
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		finish();
	    		return true;
	        case R.id.action_settings:
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}


}
