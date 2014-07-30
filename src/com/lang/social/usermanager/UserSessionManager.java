package com.lang.social.usermanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.Session;
import com.lang.social.activities.LoginActivity;
import com.lang.social.logic.User;

public class UserSessionManager {
	
	private Context mActivityContext;
	private static String mAccessToken;
	
	public UserSessionManager(Context context) {
		mActivityContext = context;
	}
	
	public void logOutUser(){
		Session session = Session.getActiveSession();
		if (session != null) {
			session.closeAndClearTokenInformation();
		}
		mActivityContext.startActivity(new Intent(mActivityContext, LoginActivity.class));
		((Activity) mActivityContext).finish();
	}
	
	public static boolean IsLoggedInByFacebook(){
		Session session = Session.getActiveSession();
		if (session != null) {
			return session.isOpened();
		}
		return false;
	}

	public static void SetAccessToken(String accessToken) {
		mAccessToken = accessToken;
	}

	public static String GetAccessToken() {
		return mAccessToken;
	}
	
}
