package com.lang.social.helpers;


public class FormHelper {

	private String mUsername;
	private String mPassword;
	private String mErrorMsg = "Error Occured!";
	private boolean mIsFormValid = true;
	
	public FormHelper(String username, String password){
		mUsername = username;
		mPassword = password;
	}
	
	public FormHelper checkFormDetails() {
		if(mUsername.isEmpty()){
			mErrorMsg = "Please Insert A Valid Email!";
			mIsFormValid = false;
		}
		else if(mPassword.isEmpty()){
			mErrorMsg = "Please Insert A Valid Password!";
			mIsFormValid = false;
		}
		return this;
	}
	
	public boolean isFormValid(){
		return mIsFormValid;
	}
	
	public String getErrorMessage(){
		return  mErrorMsg;
	}
}
