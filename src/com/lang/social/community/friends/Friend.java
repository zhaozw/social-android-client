package com.lang.social.community.friends;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.lang.social.logic.User;

public class Friend extends User {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -5195405384281604551L;
	public static final String IsOnlineKEY = "online";
	private static final String TAG = "User";
	
	private boolean isOnline = false;
	
	public Friend(JSONObject userJson) {
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
