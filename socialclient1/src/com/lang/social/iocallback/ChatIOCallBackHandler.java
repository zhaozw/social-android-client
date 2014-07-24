package com.lang.social.iocallback;

import org.json.JSONObject;

import com.lang.social.chat.ChatListener;

public class ChatIOCallBackHandler {

	private  ChatListener mChatListener;
	
	public void setChatListener(ChatListener chatListener){
		mChatListener = chatListener;
	}
	
	public void handleResponse(String eventName, JSONObject jsonResponse) {
		if (eventName.equals("newChatMessage")){
			if(mChatListener != null){
				mChatListener.onMsgRecived(jsonResponse);
			}
		}
	}

}
