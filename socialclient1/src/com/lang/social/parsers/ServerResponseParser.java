package com.lang.social.parsers;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServerResponseParser {

	protected String responseMsg;
	protected JSONObject jsonResponse;
	
    private static final String OK_RESPONSE = "OK";
    private static final String TAG = "RESPONSE";
    private static final String RESPONSE_KEY = "result";

    protected  List<String> mKeysArrayList;
    
    public ServerResponseParser(JSONObject json, List<String> jsonKeys) {
    	jsonResponse = json;
    	mKeysArrayList = jsonKeys;
    }
    
    public void checkLegalResponseJSON() {
    	checkKeysExistence();
    }
    
    public boolean isOkResult() {
    	this.ParseResultResponse();
    	boolean isOkResponse = responseMsg.equals(OK_RESPONSE);
    	if(!isOkResponse){
    		Log.d(TAG, "RESPONSE FAILED : Server Result : Failed");
    		return false;
    	}
    	return true;
    }
    
	private String ParseResultResponse()
	{
		responseMsg = null;
	  	try {  		
	  		
	  		responseMsg = jsonResponse.getString(RESPONSE_KEY);
	  		
		} catch (JSONException e) {
			Log.d(TAG, "PARSE ERROR : RESULT KEY MISSING");
			throw new RuntimeException("PARSE ERROR : RESULT KEY MISSING");
		}
	  	
	  	return responseMsg;
	}
	

	private void checkKeysExistence() {
		if(mKeysArrayList != null) {
			for (String key : mKeysArrayList) {
				if(!isKeyInJSON(key)) {
					Log.e(TAG, "JSON MISSING KEY : " + key);
					throw new RuntimeException("PARSE ERROR : RESULT KEY MISSING : " + key);
				}
			}
		}
	}

	private boolean isKeyInJSON(String key) {
		return jsonResponse.has(key);
	}
	
}
