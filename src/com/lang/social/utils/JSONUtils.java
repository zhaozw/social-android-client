package com.lang.social.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.lang.social.competition.SocialGameListItem;
import com.lang.social.items.LanguageItem;

public class JSONUtils {
	
	private static String TAG = "JSON_ERROR";
	private static String hostKEY = "host";
	private static String gameRoomNumberKEY = "GameRoomID";
	
	
	public static  ArrayList<LanguageItem> convertLanguagesJSONArrayToLanguageItemsArray(JSONArray languagesArray) 
	{
		ArrayList<LanguageItem> languagesArrayList = new ArrayList<LanguageItem>();
		try {
				for (int i = 0; i < languagesArray.length(); i++) {
					JSONObject langJsonObj = languagesArray.getJSONObject(i);
					int resId = langJsonObj.getInt("resIconId");
					String language = langJsonObj.getString("language");
					languagesArrayList.add(new LanguageItem(resId, language));
				}
		} catch (JSONException e) {
			throw new RuntimeException("Error in convertLanguagesJSONArrayToLanguageItemsArray");
		}
		return languagesArrayList;
	}
	
    public static ArrayList<SocialGameListItem> ParseJsonHostsUsers(JSONArray jsonHosts) {	
    	ArrayList<SocialGameListItem> hosts = new ArrayList<SocialGameListItem>();
        try {
        	
			 for (int i = 0; i < jsonHosts.length(); i++)
			 { 
			 	JSONObject HostAndGameNumberJson = jsonHosts.getJSONObject(i);
			 	
			 	JSONObject hostPlayer = HostAndGameNumberJson.getJSONObject(hostKEY);
			 	int gameRoomNumber = HostAndGameNumberJson.getInt(gameRoomNumberKEY);
			 	
				hosts.add(new SocialGameListItem(hostPlayer, gameRoomNumber));
			 }
			 
		} catch (JSONException e) {
			throw new RuntimeException("Error in ParseJsonHostsUsers");
		}
		return hosts; 
	}
    
    public static String getStringFromJSON(JSONObject json, String key, String errMsg){
    	String result = null;
    	try {
			result = json.getString(key);
		} catch (JSONException e) {
			if(errMsg != null){
				Log.e(TAG, errMsg);
			}
			throw new RuntimeException("Error in getStringFromJSON");
		}
    	
    	return result;
    }
    
    public static boolean getBooleanFromJSON(JSONObject json, String key, String errMsg){
    	boolean result = false;
    	try {
			result = json.getBoolean(key);
		} catch (JSONException e) {
			if(errMsg != null){
				Log.e(TAG, errMsg);
			}
			throw new RuntimeException("Error in getBooleanFromJSON");
		}
    	
    	return result;
    }
	
   
    public static void setStringValue(JSONObject json, String key, String value) {
    	try {
    		json.put(key, value);
		} catch (JSONException e) {
			throw new RuntimeException();
		}
    }
    
    public static void setJSONArray(JSONObject json, String key, JSONArray array) {
    	try {
    		json.put(key, array);
		} catch (JSONException e) {
			throw new RuntimeException();
		}
    }
    
    public static JSONArray getJSONArray(JSONObject json, String key) {
    	JSONArray jsonArray = null;
    	try {
    		jsonArray = json.getJSONArray(key);
		} catch (JSONException e) {
			Log.d(TAG, json.toString());
			throw new RuntimeException("Error in getJSONArray");
		}
    	return jsonArray;
    }
   
    public static JSONObject getJSONObject(JSONArray jsonArray, int index) {
    	JSONObject json = null;
    	try {
    		json = jsonArray.getJSONObject(index);
		} catch (JSONException e) {
			Log.d(TAG, jsonArray.toString());
			throw new RuntimeException("Error parsing json object from jsonArray");
		}
    	return json;
    }
   
 
    public static JSONObject getJSONObject(JSONObject json, String key) {
    	JSONObject newJson = null;
    	try {
    		newJson = json.getJSONObject(key);
		} catch (JSONException e) {
			Log.d(TAG, json.toString());
			throw new RuntimeException("Error in getJSONObject");
		}
		return newJson;
    }
    
    public static JSONObject newJSONObject(String value) {
    	JSONObject newJson = null;
    	try {
    		newJson =  new JSONObject(value);
		} catch (JSONException e) {
			Log.d(TAG, value);
			throw new RuntimeException("Error in newJSONObject");
		}
		return newJson;
    }
    
    public static JSONObject CreateJSON(String key, String value) {
    	JSONObject newJson = new JSONObject();
    	try {
    		newJson.put(key, value);
		} catch (JSONException e) {
			throw new RuntimeException("Error in CreateJSON");
		}
		return newJson;
    }
    
    public static JSONObject CreateJSON(String key, int value) {
    	JSONObject newJson = new JSONObject();
    	try {
    		newJson.put(key, value);
		} catch (JSONException e) {
			throw new RuntimeException("Error in CreateJSON");
		}
		return newJson;
    }

	public static String[] getStringArrayFromJSON(JSONObject json, String key, String errormsg) {
		String [] arr = null;
		try {
			JSONArray jsonArray = json.getJSONArray(key);
			int length = jsonArray.length();
			if (length > 0) {
				arr = new String [length];
			    for (int i = 0; i < length; i++) {
			    	arr[i] = jsonArray.getString(i);
			    }
			}
		} catch (JSONException e) {
			throw new RuntimeException(errormsg);
		}
		return arr;
	}
}
