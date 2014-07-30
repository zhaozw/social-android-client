package com.lang.social.community.onlineusers;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.lang.social.logic.User;

public class OnlineUser extends User {
	public static final String IsOnlineKEY = "online";
	private static final String TAG = "User";
	
	private boolean isOnline = false;
	
	public OnlineUser(JSONObject userJson) {
		super(userJson);
		try{
			if(userJson.has(IsOnlineKEY)){
				isOnline = userJson.getBoolean(IsOnlineKEY);
			}
			else{
				Log.d(TAG, "User json missing userName");
			}
		}
		catch(JSONException ex){
			Log.d(TAG, ex.toString());
			ex.printStackTrace();
		}
	}
	
	public boolean isOnline(){
		return this.isOnline;
	}
}
