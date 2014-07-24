package com.lang.social.logic;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.lang.social.R;


public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4571530552998322748L;

	public static enum Levels {
		Newbie,
		Rookie,
		Beginner,
		Skilled,
		Advanced,
		Expert
	}
	
	private static final int NumberOfLanaguages = 6;
	
	//------------------------------------------------------------------------------
	private static final String TAG = "User";
	public static final String lastNameKEY = "lastName";
	public static final String firstNameKEY = "firstName";
	public static final String isFacebookUserKEY = "isFacebookUser";
	public static final String profileidKEY = "profileid";
	public static final String learningLanguageKEY = "learningLanguage";
	public static final String UsernameKey = "username";
	public static final String PasswordKey = "password";
	public static final String StatsKey = "stats";
	//------------------------------------------------------------------------------
	private String firstName;
	private String lastName;
	private String profileID;
	private String learningLanguage;
	private String userName;
	private boolean isFacebookUser;
	private boolean hasLanguage = true;
	private Stats[] stats;
	//------------------------------------------------------------------------------
	
	public User(JSONObject userJson) {
		Log.d(TAG, userJson.toString());
		try 
		{
			if(userJson.has(firstNameKEY)){
				this.firstName = userJson.getString(firstNameKEY);
			}
			else{
				Log.d(TAG, "User json missing first name");
			}
			if(userJson.has(lastNameKEY)){
				this.lastName = userJson.getString(lastNameKEY);
			}
			else{
				Log.d(TAG, "User json missing last name");
			}
			if(userJson.has(isFacebookUserKEY)) {
				this.isFacebookUser = userJson.getBoolean(isFacebookUserKEY);
			}
			else{
				Log.d(TAG, "User json missing isFacebookUserKEY");
			}
			if(userJson.has(profileidKEY)) {
				this.profileID =  userJson.getString(profileidKEY);
				this.isFacebookUser = true;
			}
			if(userJson.has(learningLanguageKEY)){
				this.learningLanguage = userJson.getString(learningLanguageKEY);
			}
			else{
				Log.d(TAG, "User json missing learningLanguage");
				this.hasLanguage = false; 
			}
			if(userJson.has(UsernameKey)){
				this.userName = userJson.getString(UsernameKey);
			}
			else{
				Log.d(TAG, "User json missing userName");
			}
			if(userJson.has(StatsKey)){
				stats = new Stats[NumberOfLanaguages];
				JSONArray obj = userJson.getJSONArray("stats");
				for(int i = 0; i < NumberOfLanaguages; i++) {
					JSONObject JsonStats = obj.getJSONObject(i);
					String Language = JsonStats.getString("language");
					String Level = JsonStats.getString("level");
					int points = (int)JsonStats.getDouble("points");
					this.stats[i] = new Stats(Language, Level, points);
				}
			}
			else{
				Log.d(TAG, "User json missing StatsKey");
			}
		} catch (JSONException e) {
			throw new RuntimeException("Error creating User from json");
		}

		
	}
	
	public String getCurrentLanguageLevel() {
		for(int i = 0; i < NumberOfLanaguages; i++) {
			if(getLearningLanguage().equals(stats[i].getLanguage())) {
				return stats[i].getLevel();
			}
		}
		
		return null; //Not Think Will Get Here!
	}
	
	public int getPointsOfCurrentLearningLanguage(){
		for (int i = 0; i < stats.length; i++) {
			if(stats[i].getLanguage().equals(learningLanguage)){
				return stats[i].getPoints();
			}
		}
		return 0;
	}
	
	public String getLevelOfCurrentLearningLanguage(){
		for (int i = 0; i < stats.length; i++) {
			if(stats[i].getLanguage().equals(learningLanguage)){
				return stats[i].getLevel();
			}
		}
		return "";
	}

	public void setHaveLanguage(boolean hasLanguage) {
		this.hasLanguage = hasLanguage;
	}

	public void setLearningLanguage(String learningLanguage) {
		this.learningLanguage = learningLanguage;
	}

	public boolean HaveLanguage() {
		return hasLanguage;
	}

	public String getLearningLanguage() {
		return learningLanguage;
	}

	public boolean isFacebookUser() {
		return isFacebookUser;
	}

	public String getProfileID() {
		return profileID;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getLearningLanguageText() {
		 if(getLearningLanguage().equals("Spanish") || getLearningLanguage().equals("sp")){
			 return "learning: Spanish";
		 } 
		 else if(getLearningLanguage().equals("Hebrew") || getLearningLanguage().equals("he")) {
			 return "learning: Hebrew";
		 }
		 else if(getLearningLanguage().equals("German") || getLearningLanguage().equals("de")) {
			 return "learning: German";
		 }
		 else if(getLearningLanguage().equals("Dutch") || getLearningLanguage().equals("nl")) {
			 return "learning: Dutch";
		 }
		 else if(getLearningLanguage().equals("French") || getLearningLanguage().equals("fr")) {
			 return "learning: French";
		 }
		 else if(getLearningLanguage().equals("Italian") || getLearningLanguage().equals("it")) {
			    return "learning: Italian";
		 }
		 
		 else return "not learning";
	}
	
	 public int getFlagImageRes(){
		 if(getLearningLanguage().equals("Spanish") || getLearningLanguage().equals("sp")){
			 return R.drawable.spain1;
		 } 
		 else if(getLearningLanguage().equals("Hebrew") || getLearningLanguage().equals("he")) {
		    return R.drawable.israel1;
		 }
		 else if(getLearningLanguage().equals("German") || getLearningLanguage().equals("de")) {
			    return R.drawable.germany1;
		 }
		 else if(getLearningLanguage().equals("Dutch") || getLearningLanguage().equals("nl")) {
			    return R.drawable.netherlands1;
		 }
		 else if(getLearningLanguage().equals("French") || getLearningLanguage().equals("fr")) {
			    return R.drawable.france1;
		 }
		 else if(getLearningLanguage().equals("Italian") || getLearningLanguage().equals("it")) {
			    return R.drawable.italy1;
		 }
		 
		 else return R.drawable.droid;
	 }
	
}
