package com.lang.social.activities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.interfaces.UserRegisterListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.LanguageMap;
import com.lang.social.utils.MyToaster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterPart2Activity extends Activity {
	
	private static final String LearningLanguageKey = "learningLanguage";
	private static final String SignupRequestKey = "SignupRequest";
	private static final String EmailInUseMsg = "This Email Already Exists!";
	
	private JSONObject mFormDetailsJson;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_part2);

		String formJson = getIntent().getStringExtra(RegisterActivity.FormDetailsKey);
		mFormDetailsJson = JSONUtils.newJSONObject(formJson);

		IOCallBackHandler.getInstance().setRegisterListener(new UserRegisterListener() {
			@Override
			public void onRegisterEvent(JSONObject json) {
				registerEventHandler(json);
			}
		});
		
        initSpinner();
        setButtonListeners();
        setFonts();
        
	}
	
	private void initSpinner() {
	     Spinner spinner = (Spinner) findViewById(R.id.spinner_languags);
	     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
	    		 R.array.languages_array, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     spinner.setAdapter(adapter);	
	     
	}
	
	private void setFonts() {
		 Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        ((TextView)findViewById(R.id.tvLanguageToLearn)).setTypeface(tf);
	}
	
	
	private void setButtonListeners() {
        Button btnRegister = (Button) findViewById(R.id.btnRegisterSubmit);
        btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				parseRegisterFormDetails();
				ServerController.sendJSONMessage(SignupRequestKey, mFormDetailsJson);
			}
        });
        
        TextView linkLogin = (TextView) findViewById(R.id.link_to_login);
        linkLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(RegisterPart2Activity.this, LoginActivity.class));	
				finish();
			}
		});
	}

	private void parseRegisterFormDetails() {
		int which = ((Spinner) findViewById(R.id.spinner_languags)).getSelectedItemPosition();
		String [] languages = getResources().getStringArray(R.array.languages_array);
		JSONUtils.setStringValue(mFormDetailsJson, LearningLanguageKey, languages[which]);
	}
	

	public void registerEventHandler(JSONObject json) {
		ArrayList<String> jsonKeys = null;
		ServerResponseParser srp = new ServerResponseParser(json, jsonKeys);
		srp.checkLegalResponseJSON();
		 if(srp.isOkResult())
		 {
			 startActivity(new Intent(this, LoginActivity.class));
		 }
		 else{
	        MyToaster.showToast(RegisterPart2Activity.this, 
	        		EmailInUseMsg,
	        		Toast.LENGTH_SHORT);
		 }
	}
	
	




}
