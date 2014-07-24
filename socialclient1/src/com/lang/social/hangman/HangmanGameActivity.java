package com.lang.social.hangman;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.UserController;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.utils.JSONUtils;

public class HangmanGameActivity extends Activity implements HangmanListener {
	
	public static final String HangmanWordRequest = "hangmanWordRequest";
	public static final String HangmanWordResponse = "hangmanWordResponse";
	public static final String TAG = "Hangman";
	
	private String currWord;
	private String hintWord;
	private LinearLayout wordLayout;
	private TextView[] charViews;
	private GridView letters;
	private LetterAdapter ltrAdapt;
	private AlertDialog helpAlert;
	private TextView hintWordView;
	private TextView scoreView;
	
	private int currScore;
	//body part images
	private ImageView[] bodyParts;
	//number of body parts
	private static final int numParts = 6;
	//current part - will increment when wrong answers are chosen
	private int currPart;
	//number of characters in current word
	private int numChars;
	//number correctly guessed
	private int numCorr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hangman_game);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Hangman");
        getActionBar().setIcon(getResources().getDrawable(R.drawable.icon_hangout));
		
		currWord = "";
		wordLayout = (LinearLayout)findViewById(R.id.word);
		letters = (GridView)findViewById(R.id.letters);
		
		hintWordView = (TextView) findViewById(R.id.hangmanHint);
		scoreView = (TextView) findViewById(R.id.hangmanScore);
		
		bodyParts = new ImageView[numParts];
		bodyParts[0] = (ImageView)findViewById(R.id.head);
		bodyParts[1] = (ImageView)findViewById(R.id.body);
		bodyParts[2] = (ImageView)findViewById(R.id.arm1);
		bodyParts[3] = (ImageView)findViewById(R.id.arm2);
		bodyParts[4] = (ImageView)findViewById(R.id.leg1);
		bodyParts[5] = (ImageView)findViewById(R.id.leg2);
		
		ImageView ivLanguageLearning = (ImageView) findViewById(R.id.ivHangmanLanguage);
		ivLanguageLearning.setImageDrawable(getResources().getDrawable(UserController.getFlagImageRes()));

		TextView tvLearningLanguage = (TextView) findViewById(R.id.tvHangmanLanguage);
		tvLearningLanguage.setText(UserController.getLearningLanguageText());
		
		initNewGameRound();
		
	}
	
	private void requestAnswerFromServer() {
		IOCallBackHandler.getInstance().setHangmanListener(this);
		ServerController.sendJSONMessage(HangmanWordRequest, null);
	}

	@Override
	public void OnWordResponse(JSONObject jsonResponse) {
		Log.d(TAG, jsonResponse.toString());
		List<String> jsonKeys = Arrays.asList("result", "Answer" , "Hint");
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()) {
			final String answer = JSONUtils.getStringFromJSON(jsonResponse, "Answer", "Error Getting hangman word from server");
			final String tanslatedAnswer = JSONUtils.getStringFromJSON(jsonResponse, "Hint", "Error Getting hangman hint word from server");
			Log.d(TAG, "words from server: " + answer + " " + tanslatedAnswer);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					playGame(answer, tanslatedAnswer);
				}
			});
		}
	}
	
	private void playGame(String answer, String hintWord) {
		
		currWord = answer;
		
		this.hintWord = hintWord;
		hintWordView.setText("hint: " + hintWord);
		charViews = new TextView[currWord.length()];
		wordLayout.removeAllViews();
	
		for (int c = 0; c < currWord.length(); c++) {
		  charViews[c] = new TextView(this);
		  charViews[c].setText(""+currWord.charAt(c));
		  
		  charViews[c].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		  charViews[c].setGravity(Gravity.CENTER);
		  charViews[c].setTextColor(Color.WHITE);
		  charViews[c].setBackgroundResource(R.drawable.letter_bg);
		  //add to layout
		  wordLayout.addView(charViews[c]);
		}

		ltrAdapt = new LetterAdapter(this);
		letters.setAdapter(ltrAdapt);
		
		currPart = 0;
		numChars = currWord.length();
		numCorr = 0;
		
		for(int p = 0; p < numParts; p++) {
		  bodyParts[p].setVisibility(View.INVISIBLE);
		}
	}

	private void initNewGameRound() {
		currScore = 360;
		scoreView.setText("score: " + currScore);
		requestAnswerFromServer();
	}
	

	public void letterPressed(View view) {
		  //user has pressed a letter to guess
		String ltr =((TextView)view).getText().toString().toLowerCase();
		char letterChar = ltr.charAt(0);
		
		view.setEnabled(false);
		view.setBackgroundResource(R.drawable.letter_down);
		
		boolean correct = false;
		for(int k = 0; k < currWord.length(); k++) {
		  if(currWord.charAt(k) == letterChar){
		    correct = true;
		    numCorr++;
		    charViews[k].setTextColor(Color.BLACK);
		  }
		}
		
		if (correct) {
			if (numCorr == numChars) {
				gameOver("YAY", "You win!\n\nThe answer was:\n\n" + currWord);
			}
		} else if (currPart < numParts) {
			  //some guesses left
			  currScore -= 60;
			  scoreView.setText("score: " + currScore);
			  bodyParts[currPart].setVisibility(View.VISIBLE);
			  currPart++;
		} else {
			gameOver("OOPS", "You lose!\n\nThe answer was:\n\n" + currWord);
		}
	}
	
	private void gameOver(String title, String message) {
		 // Disable Buttons
		  disableBtns(); 
		  // Display Alert Dialog
		  AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
		  winBuild.setTitle(title);
		  winBuild.setMessage(message);
		  winBuild.setPositiveButton("Play Again",
		    new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int id) {
		        HangmanGameActivity.this.initNewGameRound();
		    }});
		 
		  winBuild.setNegativeButton("Exit",
		    new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int id) {
		    	  HangmanGameActivity.this.finish();
		    }});
		 
		  winBuild.show();		
	}

	public void disableBtns() {
	  int numLetters = letters.getChildCount();
	  for (int l = 0; l < numLetters; l++) {
	    letters.getChildAt(l).setEnabled(false);
	  }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hangman, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	  switch (item.getItemId()) {
	  case android.R.id.home:
	    NavUtils.navigateUpFromSameTask(this);
	    return true;
	  case R.id.action_help:
	    showHelp();
	    return true;
	  }
	 
	  return super.onOptionsItemSelected(item);
	}

	private void showHelp() {
		AlertDialog.Builder helpBuild = new AlertDialog.Builder(this);
		  helpBuild.setTitle("Help");
		  helpBuild.setMessage("Guess the word by selecting the letters.\n\n"
		      + "You only have 6 wrong selections then it's game over!");
		  helpBuild.setPositiveButton("OK",
		    new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int id) {
		        helpAlert.dismiss();
		    }});
		  helpAlert = helpBuild.create();
		  helpBuild.show();
	}
}
