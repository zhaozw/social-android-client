package com.lang.social.community.general;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.User;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;

public class UserProfileWindow implements GeneralCommunityServerListener {
	private Context m_Context;
	private View m_AnchorView;
	private User m_User;	//The clicked User profile. NOT THE CURRENT USER USING THE APP!
	private int m_layoutResourceId;
	
	private PopupWindow popupWindow;
	private View popupView;
	public UserProfileWindow(Context i_Context, View i_View, User i_User) {
		m_Context = i_Context;
		m_AnchorView = i_View;
		m_User = i_User;
		m_layoutResourceId = R.layout.user_profile_view;
		
		setServerListener();
	}
	
	private void setServerListener() {
		IOCallBackHandler.getInstance().setGeneralCommunityListener(this);
		
	}

	public void ShowProfileWindow() {
		//Send Server to check if the user is a friend of the current user
		askServerIfFriends();
		
	}
	
	private void askServerIfFriends() {
		JSONObject userDetails = new JSONObject();
		if(m_User.isFacebookUser() == true) {
			JSONUtils.setStringValue(userDetails, "profileid", m_User.getProfileID()); 
			JSONUtils.setStringValue(userDetails, "facebookUser", "true");
		}
		else {
			JSONUtils.setStringValue(userDetails, "username", m_User.getUserName()); 
			JSONUtils.setStringValue(userDetails, "facebookUser", "false");
		}
		ServerController.sendJSONMessage("isFriendsRequest", userDetails);
	}

	@Override
	public void onIsFriendsRespone(JSONObject obj) {
		String result = JSONUtils.getStringFromJSON(obj, "result", null);
		if(result.equals("OK")) {
			final boolean isFriends = JSONUtils.getBooleanFromJSON(obj, "isFriends", null);
			((Activity) m_Context).runOnUiThread(new Runnable() {
				 @Override
				 public void run() {
					if(isFriends == true) {
						createPopUpWithoutAddFriendOption();
					}
					else {
						createPopUpWithAddFriendOption();
					}
					popupWindow.showAtLocation(m_AnchorView, Gravity.CENTER, 0, 0); 
				 }
			 });
		}
		else {
			Log.d("UserProfileWindow", "Error at onIsFriendsRespone");
		}
	}

	private void createPopUpWithAddFriendOption() { 
		createPopUpWithoutAddFriendOption();
		ImageView ivAddFriendIcon = (ImageView) popupView.findViewById(R.id.ivAddFriend);
		ivAddFriendIcon.setImageResource(R.drawable.addfriendicon);
		TextView itvFriendshipStatus = (TextView) popupView.findViewById(R.id.tvFriendshipStatus);
		itvFriendshipStatus.setText("   Add!");
		
		//Implementing the Add Friend Action / OnClickListener
		ivAddFriendIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendFriendRequestToServer();
				popupWindow.dismiss();
			}
		});
		
	}
	
	private void sendFriendRequestToServer() {
		JSONObject userDetails = new JSONObject();
		if(m_User.isFacebookUser() == true) {
			JSONUtils.setStringValue(userDetails, "profileid", m_User.getProfileID()); 
			JSONUtils.setStringValue(userDetails, "facebookUser", "true");
		}
		else {
			JSONUtils.setStringValue(userDetails, "username", m_User.getUserName()); 
			JSONUtils.setStringValue(userDetails, "facebookUser", "false");
		}
		ServerController.sendJSONMessage("friendRequest", userDetails);
	}

	private void createPopUpWithoutAddFriendOption() {
		popupView = ((Activity) m_Context).getLayoutInflater().inflate(m_layoutResourceId, null);
		
	    popupWindow = new PopupWindow(popupView, 
	                           LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	    // Example: If you have a TextView inside `popup_layout.xml` 
	    TextView ivLearningLang = (TextView) popupView.findViewById(R.id.tvLearning);
	    ivLearningLang.setText(m_User.getLearningLanguageText());

	    Button closeButton = (Button) popupView.findViewById(R.id.btCloseUserProfile);
	    closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				
			}
		});
	    TextView tvFriendName = (TextView) popupView.findViewById(R.id.tvUserProfileName);
	    tvFriendName.setText(m_User.getFullName());
	    
	    TextView tvFriendLevel = (TextView) popupView.findViewById(R.id.tvLevel);
	    tvFriendLevel.setText("Level: " + m_User.getCurrentLanguageLevel());
	    
	    
	     
	    
	    if(m_User.getProfileID() != null){
		    ProfilePictureView ppv = (ProfilePictureView) popupView.findViewById(R.id.ivUserFriendProfileImage);
		    ppv.setProfileId(m_User.getProfileID());
		}
	    
	    
	    // If the PopupWindow should be focusable
	    popupWindow.setFocusable(true);

	    // If you need the PopupWindow to dismiss when when touched outside 
	    popupWindow.setBackgroundDrawable(new ColorDrawable());

	    int location[] = new int[2];

	    // Get the View's(the one that was clicked in the Fragment) location
	    m_AnchorView.getLocationOnScreen(location);
	}

	@Override
	public void onFriendRequestRespone(JSONObject obj) {
		String result = JSONUtils.getStringFromJSON(obj, "result", null);
		if(result.equals("OK")) {
			MyToaster.showToast((Activity)m_Context, "Friend Request Sent!", Toast.LENGTH_LONG);
		}
		else {
			MyToaster.showToast((Activity)m_Context, "Error At Sending Request!", Toast.LENGTH_LONG);
		}
	}
}
