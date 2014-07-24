
package com.lang.social.activities;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.interfaces.UserRegisterListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.utils.JSONUtils;

public class RegisterActivity extends Activity {
	
	public static final String FormDetailsKey = "formDetailsJson";
	
	private static final String isUserNameUniqueKey = "isUsernameUnique";
	public static final String UniqueUsernameRequest = "checkUniqueUsernameRequest";
	
	private JSONObject mJsonFromDetails = new JSONObject();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setFonts();
        setButtonListeners();  
        
        
        IOCallBackHandler.getInstance().setRegisterListener(new UserRegisterListener() {
			@Override
			public void onRegisterEvent(JSONObject json) {
				formCheckEventHandler(json);
			}
		});
        
	}
	

	private void formCheckEventHandler(JSONObject json) {
		List<String> jsonKeys = Arrays.asList(isUserNameUniqueKey);
		ServerResponseParser srp = new ServerResponseParser(json, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult())
		{
			boolean isUsernameUnique = JSONUtils.getBooleanFromJSON(json, isUserNameUniqueKey, null);
			if(isUsernameUnique){
				Intent i = new Intent(RegisterActivity.this, RegisterPart2Activity.class);
				String jsonFormString = mJsonFromDetails.toString();
				i.putExtra(FormDetailsKey, jsonFormString);
				startActivity(i);
				finish();
			}
		}
	}
	
	private void setButtonListeners() {
        Button btnRegister = (Button) findViewById(R.id.btnRegisterNext);
        btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				parseRegisterFormDetails();
				boolean isValidForm = checkValidRegisterForm();
				if(isValidForm)
				{
					JSONObject json = new JSONObject();
					String email = JSONUtils.getStringFromJSON(mJsonFromDetails, User.UsernameKey, null);
					JSONUtils.setStringValue(json ,User.UsernameKey, email);
					ServerController.sendJSONMessage(UniqueUsernameRequest, json);
				}
			}
        });
        
        TextView linkLogin = (TextView) findViewById(R.id.link_to_login);
        linkLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));	
			}
		});
	}


	private void setFonts() {
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Italic.ttf");
        ((TextView)findViewById(R.id.tvRegisterUsername)).setTypeface(tf);
        ((TextView)findViewById(R.id.tvRegisterFName)).setTypeface(tf);
        ((TextView)findViewById(R.id.tvRegisterLName)).setTypeface(tf);
        ((TextView)findViewById(R.id.tvRegisterPassword)).setTypeface(tf);
        ((Button) findViewById(R.id.btnRegisterNext)).setTypeface(tf);
        
        Typeface tf0 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-LightItalic.ttf");
        ((TextView)findViewById(R.id.link_to_login)).setTypeface(tf0);
	}
	
	private boolean checkValidRegisterForm() {
		boolean isValidForm = true;
		String errorMsg = "Error Occured!";
		try {
			if(mJsonFromDetails.getString(User.UsernameKey).isEmpty()){
				errorMsg = "Please Insert A Valid Email!";
				isValidForm = false;
			}
			else if(mJsonFromDetails.getString(User.firstNameKEY).isEmpty()){
				errorMsg = "Please Insert A Valid First Name!";
				isValidForm = false;
			}
			else if(mJsonFromDetails.getString(User.lastNameKEY).isEmpty()){
				errorMsg = "Please Insert A Valid Last Name!";
				isValidForm = false;
			}
			else if( mJsonFromDetails.getString(User.PasswordKey).isEmpty()){
				errorMsg = "Please Insert A Valid Password!";
				isValidForm = false;
			}
			if(!isValidForm){
				Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			throw new RuntimeException();
		}
		return isValidForm;
	}
	
	private void parseRegisterFormDetails(){

		EditText UsernameET = (EditText) findViewById(R.id.etRegisterUsername);
		EditText PasswordET = (EditText) findViewById(R.id.etRegisterPassword);
		EditText FirstNameET = (EditText) findViewById(R.id.etRegisterFName);
		EditText LastNameET = (EditText) findViewById(R.id.etRegisterLName);

		String Email = UsernameET.getText().toString();
		String PassWord = PasswordET.getText().toString();
		String FirstName = FirstNameET.getText().toString();
		String LastName = LastNameET.getText().toString();

		try {
			mJsonFromDetails.put(User.UsernameKey, Email);
			mJsonFromDetails.put(User.firstNameKEY, FirstName);
			mJsonFromDetails.put(User.lastNameKEY, LastName);
			mJsonFromDetails.put(User.PasswordKey, PassWord);

		} catch (JSONException e) {
			throw new RuntimeException();
		}
	}



}
