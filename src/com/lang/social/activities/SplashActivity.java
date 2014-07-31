package com.lang.social.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.facebook.Session;
import com.lang.social.R;
import com.lang.social.facebook.FacebookController;

public class SplashActivity extends Activity {
	
	private static final int SPLASH_DISPLAY_LENGHT = 1000;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
	    new Handler().postDelayed(new Runnable(){
	        @Override
	        public void run() { 
	        	Intent mainIntent;
	    		if(FacebookController.isFacebookSessionOpen()){
		           mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
	    		}
	    		else{
	    		   mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
	    		}
	    		SplashActivity.this.startActivity(mainIntent);
	            SplashActivity.this.finish();
	        }
	    }, SPLASH_DISPLAY_LENGHT);
	}

	
}
