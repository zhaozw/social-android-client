package com.lang.social.iocallback;

import org.json.JSONObject;

import com.lang.social.hangman.HangmanActivity;
import com.lang.social.hangman.HangmanGameActivity;
import com.lang.social.hangman.HangmanListener;

public class HangmanIOCallBackHandler {

	private HangmanListener hangmanListener;
	
	public void handleResponse(String eventName, JSONObject jsonResponse) {
		 if(eventName.equals(HangmanGameActivity.HangmanWordResponse)){
			if(hangmanListener != null){
				hangmanListener.OnWordResponse(jsonResponse);
			}
		 }
	}
	
	public void setHangmanListener(HangmanListener hangmanListener){
		this.hangmanListener = hangmanListener;
	}
	
}
