package com.lang.social.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.interfaces.ProfilePageListener;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;

public class UserProfileActivity extends Activity implements ProfilePageListener {
	
	private TextView tvUserName;
	private TextView tvMotherTongue;
	private TextView tvLearning;
	private TextView tvScore;
	private TextView tvActiveSince;
	private ImageView ivUserFlag;
	
	private TextView tvOnlineLabel;
	private TextView tvAmountOfFriendsREquests;
	private TextView tvAmountOfPointsMissing;
	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_user_profile);
	    
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setHomeButtonEnabled(true);
	    getActionBar().setIcon(getResources().getDrawable(R.drawable.userprofileicon));
	    setTitle(" Profile");	//Activity Title
	    
	    tvUserName = (TextView)findViewById(R.id.tvUserName);
	    tvLearning = (TextView)findViewById(R.id.tvLearn);
	    tvScore = (TextView)findViewById(R.id.tvScore);
	    ivUserFlag = (ImageView)findViewById(R.id.ivUserFlag);
	    tvOnlineLabel = (TextView)findViewById(R.id.tvOnlineLabel);
	    tvAmountOfFriendsREquests = (TextView)findViewById(R.id.tvFriendsRequestsAmount);
	    tvAmountOfPointsMissing = (TextView)findViewById(R.id.tvAmountOfPointsMissing);
	    
	    //Animations!
	    Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
	    animation.setDuration(1000);
	    tvOnlineLabel.setAnimation(animation);
	    tvAmountOfFriendsREquests.setAnimation(animation);
	    tvAmountOfPointsMissing.setAnimation(animation);
	    
	    ProfilePictureView ppv = (ProfilePictureView)findViewById(R.id.ivUserProfileImage);
	    User currentUser = UserController.getUser();
	    if(currentUser.getProfileID() != null){
		    ppv.setProfileId(currentUser.getProfileID());
		}
	    tvUserName.setText(currentUser.getFullName());
	    tvLearning.setText("Learning: " + currentUser.getLearningLanguage());
	    
	    if(currentUser.getLearningLanguage().equals("sp") || currentUser.getLearningLanguage().equals("Spanish")) {
	    	ivUserFlag.setImageResource(R.drawable.spain1);
		}
		else {	//Assuming there are only 2 languages, and the second is Israel
			ivUserFlag.setImageResource(R.drawable.israel1);
		}
	    
	    
	    
	     
	    getDetailsFromServer();
	}

	private void getDetailsFromServer() {
		JSONObject jsonToSend = new JSONObject();
		ServerController.sendJSONMessage("ProfilePage", jsonToSend);
		
		
		
	}

	@Override
	public void onProfileDetailsRecieved(JSONObject json){
		try {
			tvUserName.setText(json.getString("firstName") + " "+ json.getString("lastName"));
			tvMotherTongue.setText(json.getString("motherLanguage"));
			tvLearning.setText(json.getString("learningLanguage"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
