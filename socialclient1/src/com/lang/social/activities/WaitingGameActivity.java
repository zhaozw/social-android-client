package com.lang.social.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;

public class WaitingGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting_game);
//
//		 if(checkFacebookSession()){
//			 getFacebookLoggedInUser(Session.getActiveSession());
//		 }
//	}
//
//	private void getFacebookLoggedInUser(Session session) {
//	   Request.newMeRequest(session, new Request.GraphUserCallback() {
//        	  @Override
//        	  public void onCompleted(GraphUser user, Response response) {
//        	    if (user != null) {
//        		    ProfilePictureView ppv = (ProfilePictureView) findViewById(R.id.ivUserHostProfileImage);
//        		    ppv.setProfileId(user.getId());
//        	    }
//        	  }
//        }).executeAsync();
//	}
//
//	private boolean checkFacebookSession() {
//		 Session session = Session.getActiveSession();
//		 if(session != null && session.isOpened()){
//			 return true;
//		 }
//		 return false;
//	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.waiting_game_acitivity, menu);
//		return true;
//	}

	}
}
