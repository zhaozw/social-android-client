package com.lang.social.exceptions;

import org.json.JSONException;

import android.util.Log;

public class JSONExceptionHandler extends Exception {
	
	private JSONExceptionHandler mExceptionHandlerInstance;
	
	public JSONExceptionHandler newInstance(){
		mExceptionHandlerInstance = new JSONExceptionHandler();
		return mExceptionHandlerInstance;
	}
	
	public JSONExceptionHandler handleException(JSONException ex){
		ex.printStackTrace();
		return mExceptionHandlerInstance;
	}
	
	public void setErrorMessage(String TAG, String ErrorMsg){
		Log.d(TAG, ErrorMsg);
	}
	
}
