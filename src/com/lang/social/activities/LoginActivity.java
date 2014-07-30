
package com.lang.social.activities;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.LoginButton;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.facebook.FacebookFragment;
import com.lang.social.helpers.FormHelper;
import com.lang.social.interfaces.UserLoginListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.teachstudy.StudentTeacherActivity;
import com.lang.social.teachstudy.lesson.StudentTeacherLessonActivity;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;

public class LoginActivity extends FragmentActivity implements UserLoginListener {
	
	private static final String UserKEY = "User";
	private static final String LoginRequest = "LoginRequest";
	private static final String TAG = "login";
	
	private FacebookFragment facebookFragment;
	public ProgressDialog progressDialog;

	private String mUsername;
	private String mPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		try {
			initSociaLang();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
/*		
		try {
			if(isWifiEnabled()) {
				initSociaLang();
			} else {
				MyToaster.showToast(this, "Turn on Wifi connection!", Toast.LENGTH_SHORT);
			}
		} catch(MalformedURLException e){
			Log.e(TAG, "Failed to connect to server - Server URL Not OK!");
		}*/
	}

	private void initSociaLang() throws MalformedURLException {
		ServerController.connectToServer(this);
		setAsLoginListener();
		createProgressDialog();
        setTextFonts();
        setButtonListeners();
        initFacebookFragment();
	}

	private void initFacebookFragment(){
        facebookFragment = new FacebookFragment();
        facebookFragment = (FacebookFragment) getSupportFragmentManager().findFragmentById(R.id.login_facebook_fragment);
	}
	
	private void setAsLoginListener() {
		IOCallBackHandler.getInstance().setLoginListener(this);
	}

	 private boolean isWifiEnabled() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
	}

	 
	public void initIntentAndMoveToHomeActivity(JSONObject json) {
		Intent homeIntent = new Intent(this, HomeActivity.class);
		JSONObject userJson = JSONUtils.getJSONObject(json, UserKEY);
		UserController.setCurrentAppUser(new User(userJson));
		startActivity(homeIntent);
		finish();
	}
	
	@Override
	public void onLoginEvent(JSONObject jsonResponse) {
		Log.d(TAG, jsonResponse.toString());
		try {
			
			String date = jsonResponse.getString("date");
			ISO8601DateFormat df = new ISO8601DateFormat();
			Date d = df.parse(date);
			Log.d(TAG, d.toString());
			
			JSONObject jsonTestDate = new JSONObject();
			JSONUtils.setStringValue(jsonTestDate, "date", d.toString());
			ServerController.sendJSONMessage("testDateRequest", jsonTestDate);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

  		progressDialog.dismiss();
		ArrayList<String> jsonKeys = null;
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()) {
			initIntentAndMoveToHomeActivity(jsonResponse);
		}
		else
		{
			MyToaster.showToast(this,
					"Error In Login!",
					Toast.LENGTH_SHORT);
		}
	}


	private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
	}


	private void setButtonListeners() {
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v) {
    			parseLoginFormDetails();
    			FormHelper formHelper = new FormHelper(mUsername, mPassword);
    			boolean isFormValid = formHelper.checkFormDetails().isFormValid();
    			if(isFormValid)
    			{
    				JSONObject loginJson = createRequestJson();
    				ServerController.sendJSONMessage(LoginRequest, loginJson);
    				progressDialog.show();
    			}
    			else
    			{
    				MyToaster.showToast(LoginActivity.this,
    						formHelper.getErrorMessage(),
    						Toast.LENGTH_SHORT);
    			}
    		}
        });
        
        TextView linkToRegister = (TextView) findViewById(R.id.link_to_register);
        linkToRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), RegisterActivity.class));	
			}
		});	 
	}
	
	private JSONObject createRequestJson() {
		JSONObject json = new JSONObject();
		JSONUtils.setStringValue(json, User.UsernameKey , mUsername);
		JSONUtils.setStringValue(json, User.PasswordKey , mPassword);
		return json;
	}
	
	private void parseLoginFormDetails(){
		EditText etUsername = (EditText) findViewById(R.id.etUsername);
		EditText etPassword = (EditText) findViewById(R.id.etPassword);
		mUsername = etUsername.getText().toString();
		mPassword = etPassword.getText().toString();
	}
	
	private void setTextFonts() {
		
		 Typeface tf2 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Italic.ttf");
        ((TextView)findViewById(R.id.tvLoginUsername)).setTypeface(tf2);
        ((TextView)findViewById(R.id.tvLoginPassword)).setTypeface(tf2);
        ((Button)findViewById(R.id.btnLogin)).setTypeface(tf2);
        
        Typeface tf0 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-LightItalic.ttf");
        ((TextView)findViewById(R.id.link_to_register)).setTypeface(tf0);
        
        Typeface tf1 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-BlackItalic.ttf");
        ((LoginButton)findViewById(R.id.authButton)).setTypeface(tf1);
       
	}


	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	facebookFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

	
}

