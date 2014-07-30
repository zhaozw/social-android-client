package com.lang.social.activities;

import java.util.ArrayList;
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
import com.lang.social.fragments.DifficultyDialogFragment;
import com.lang.social.hangman.HangmanActivity;
import com.lang.social.interfaces.DifficultyListener;
import com.lang.social.items.LearnFeatureMenuItem;
import com.lang.social.logic.UserController;
import com.lang.social.photoguess.PhotoGuessActivity;
import com.lang.social.picturequizgame.PictureQuizGameActivity;
import com.lang.social.quizgame.QuizActivity;
import com.lang.social.screenslide.ScreenSlideActivity;
import com.lang.social.usermanager.UserSessionManager;

public class SelfLearnMenuActivity extends Activity {

	public static final String startNewQuizGameResponse = "startNewQuizGameResponse";
	public static final String startNewQuizGameRequest = "startNewQuizGame";
	
	private LearnFeatureListAdapter selfLearnListAdapter;
	private ArrayList<LearnFeatureMenuItem> selfLearnItems;

	private DifficultyDialogFragment mDifficultyDialogFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_learn_menu_layout);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(" Self Learn");
        getActionBar().setIcon(getResources().getDrawable(R.drawable.selflearnicon));
		
		
		selfLearnItems = new ArrayList<LearnFeatureMenuItem>();
		selfLearnItems.add(new LearnFeatureMenuItem(R.drawable.selflearnning, "Quiz Game", "Multi Choice Answers"));
		selfLearnItems.add(new LearnFeatureMenuItem(R.drawable.icon_hangout, "Hangout Game", "Letters Combination"));
		selfLearnItems.add(new LearnFeatureMenuItem(R.drawable.album, "Photos Game", "Photos Quiz"));
		selfLearnItems.add(new LearnFeatureMenuItem(R.drawable.questmark,"Photo Guess", "Guess The Photo"));
		selfLearnItems.add(new LearnFeatureMenuItem(R.drawable.timericon1, "Demo Of Slideshow", "Demo Of Slideshow"));
		createListItems(selfLearnItems);

	
	}

	private void createListItems(List<LearnFeatureMenuItem> selfLearnItems) {
        selfLearnListAdapter = new LearnFeatureListAdapter(this, R.layout.learn_feature_list_row, selfLearnItems);
        ListView listViewSelfLearn = (ListView)findViewById(R.id.listViewLearnFeatures);
        listViewSelfLearn.setAdapter(selfLearnListAdapter);
        listViewSelfLearn.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int which, long arg3) {
				switch(which){
				case 0: 
					startActivity(new Intent(SelfLearnMenuActivity.this, QuizActivity.class));	
					break;	
				case 1:
					startActivity(new Intent(SelfLearnMenuActivity.this, HangmanActivity.class));
					break;
				case 2:
					startActivity(new Intent(SelfLearnMenuActivity.this, PictureQuizGameActivity.class));
					break;
				case 3:
					startActivity(new Intent(SelfLearnMenuActivity.this, PhotoGuessActivity.class));
					break;
				case 4:
					startActivity(new Intent(SelfLearnMenuActivity.this, ScreenSlideActivity.class));
					break;
				}
			}
		});
	}

	private void showDifficultyDialogFragment() {
	 	mDifficultyDialogFragment = DifficultyDialogFragment.newInstance(R.string.difficulty_alert_dialog_fragment_title);
	 	mDifficultyDialogFragment.setDifficultyListener(new DifficultyListener() {
			@Override
			public void onDifficultyChoosen(String difficulty) {
				//sendStartGameRequest(difficulty);
			}
		});
	 	mDifficultyDialogFragment.show(getFragmentManager(), "DifficulyDialogFragment");
	}

	private JSONObject createStartQuizGameJsonRequest(String difficulty) {
		JSONObject jsonToSend = new JSONObject();
		try {
			
			jsonToSend.put("language", UserController.getUser().getLearningLanguage());
			jsonToSend.put("difficulty", difficulty);
			
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("JSON", "Error in createStartQuizGameJsonRequest");
		}
		
		return jsonToSend;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
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
