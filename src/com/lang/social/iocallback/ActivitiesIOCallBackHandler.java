package com.lang.social.iocallback;

import org.json.JSONObject;

import com.lang.social.interfaces.HomeActivityListener;
import com.lang.social.interfaces.ProfilePageListener;

public class ActivitiesIOCallBackHandler {
	
	private  HomeActivityListener homeActivityListener;
	
	
	public void setHomeActivityLoadListener(HomeActivityListener i_HomeActivityListener){
		homeActivityListener = i_HomeActivityListener;
	}
	
	public void handleResponse(String eventName, JSONObject jsonResponse) {
		if (eventName.equals("LoadHomeResponse")){
			if(homeActivityListener != null){
				homeActivityListener.onHomeActivityLoad(jsonResponse);
			}
		} else if(eventName.equals("LoadHomeResponse")){
			if(homeActivityListener != null){
				homeActivityListener.onHomeActivityLoad(jsonResponse);
			}
		}
	}

}
