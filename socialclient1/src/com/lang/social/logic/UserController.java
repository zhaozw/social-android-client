package com.lang.social.logic;

import com.lang.social.R;



public class UserController {
	
	private static User mUser;
	
	private UserController() {}
	
	public static User getUser(){
		return mUser;
	}
	
	public static void setCurrentAppUser(User user){
		mUser = user;
	}
	
	public static String getLearningLanguageText() {
		 if(mUser.getLearningLanguage().equals("Spanish") || mUser.getLearningLanguage().equals("sp")){
			 return "learning spanish";
		 } 
		 else if(mUser.getLearningLanguage().equals("Hebrew") || mUser.getLearningLanguage().equals("he")) {
			 return "learning hebrew";
		 }
		 else if(mUser.getLearningLanguage().equals("German") || mUser.getLearningLanguage().equals("de")) {
			 return "learning german";
		 }
		 else if(mUser.getLearningLanguage().equals("Dutch") || mUser.getLearningLanguage().equals("nl")) {
			 return "learning dutch";
		 }
		 else if(mUser.getLearningLanguage().equals("French") || mUser.getLearningLanguage().equals("fr")) {
			 return "learning french";
		 }
		 else if(mUser.getLearningLanguage().equals("Italian") || mUser.getLearningLanguage().equals("it")) {
			 return "learning italian";
		 }
		 
		 else return "not learning";
	}
	
	 public static int getFlagImageRes(){
		 if(mUser.getLearningLanguage().equals("Spanish") || mUser.getLearningLanguage().equals("sp")){
			 return R.drawable.spain1;
		 } 
		 else if(mUser.getLearningLanguage().equals("Hebrew") || mUser.getLearningLanguage().equals("he")) {
		    return R.drawable.israel1;
		 }
		 else if(mUser.getLearningLanguage().equals("German") || mUser.getLearningLanguage().equals("de")) {
			    return R.drawable.germany1;
		 }
		 else if(mUser.getLearningLanguage().equals("Dutch") || mUser.getLearningLanguage().equals("nl")) {
			    return R.drawable.netherlands1;
		 }
		 else if(mUser.getLearningLanguage().equals("French") || mUser.getLearningLanguage().equals("fr")) {
			    return R.drawable.france1;
		 }
		 else if(mUser.getLearningLanguage().equals("Italian") || mUser.getLearningLanguage().equals("it")) {
			    return R.drawable.italy1;
		 }
		 
		 else return R.drawable.droid;
	 }
}
