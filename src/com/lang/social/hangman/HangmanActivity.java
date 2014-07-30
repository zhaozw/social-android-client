package com.lang.social.hangman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;

public class HangmanActivity extends Activity  {

	private AlertDialog helpAlert;
	public static final String TAG = "Hangman";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hangman);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Hangman");
        getActionBar().setIcon(getResources().getDrawable(R.drawable.icon_hangout));
        
		Button playBtn = (Button)findViewById(R.id.playBtn);	
		playBtn.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/Jura-DemiBold.ttf"));
		playBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			  startActivity(new Intent(HangmanActivity.this, HangmanGameActivity.class));
			}
		});
		

		Animation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(4000);
		
		ImageView ivHangmanSpining = (ImageView) findViewById(R.id.ivHangmanSpining);
		ivHangmanSpining.startAnimation(anim);


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
