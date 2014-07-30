package com.lang.social.iocallback;

import org.json.JSONObject;

import android.util.Log;

import com.lang.social.interfaces.ProfilePageListener;
import com.lang.social.interfaces.UserLanguageUpdateListener;
import com.lang.social.interfaces.UserLoginListener;
import com.lang.social.interfaces.UserProfileListener;
import com.lang.social.interfaces.UserRegisterListener;
import com.lang.social.profile.FriendDetailsResponseListener;

public class UserIOCallBackHandler {
	
	private  UserRegisterListener registerListener;
	private  UserLoginListener loginListener;
	private  UserProfileListener profileUpdateListener;
	private  UserLanguageUpdateListener languageUpdateListener; 
	private  ProfilePageListener profilePageListener; 
	private  FriendDetailsResponseListener friendDetailsResponseListener; 
	
	public static final String RegisterResponse = "RegisterResponse";
	
	public void setLoginListener(UserLoginListener i_loginListener){
		loginListener = i_loginListener;
	}
	
	public void setRegisterListener(UserRegisterListener i_registerListener){
		registerListener = i_registerListener;
	}
	
	public void setProfileUpdateListener(UserProfileListener i_profileUpdateListener){
		profileUpdateListener = i_profileUpdateListener;
	}
	
	public void setLanguageUpdateListener(UserLanguageUpdateListener i_languageUpdateListener){
		languageUpdateListener = i_languageUpdateListener;
	}
	
	public void setProfileDetailsListener(ProfilePageListener profilePageListener) {
		this.profilePageListener = profilePageListener;
	}

	public void setFriendDetailsResponseListener(FriendDetailsResponseListener friendDetailsResponseListener) {
		this.friendDetailsResponseListener = friendDetailsResponseListener;
	}


	public void handleResponse(String eventName, JSONObject jsonResponse) {
		 if (eventName.equals(RegisterResponse)) {
			if (registerListener != null) {
				registerListener.onRegisterEvent(jsonResponse);
			}
		} else if (eventName.equals("LoginResponse")) {
			if (loginListener != null) {
				loginListener.onLoginEvent(jsonResponse);
			}
		} else if (eventName.equals("profileUpdateResponse")) {
			if (profileUpdateListener != null) {
				profileUpdateListener.onProfileUpdated(jsonResponse);
			}
		} else if (eventName.equals("languageUpdateResponse")) {
			if (languageUpdateListener != null) {
				languageUpdateListener.OnLanguageUpdateResponse(jsonResponse);
			}
		} else if (eventName.equals("ProfileDetailsResponse")) {
			if (profilePageListener != null) {
				profilePageListener.onProfileDetailsRecieved(jsonResponse);
			}
		} else if(eventName.equals("friendUserDetailsResponse")){
			if(friendDetailsResponseListener != null){
				friendDetailsResponseListener.OnFriendDetailsResponse(jsonResponse);
			}
		}
	}



	
}


