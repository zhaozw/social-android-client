package com.lang.social.community.messages;

import org.json.JSONObject;

public interface MessagesServerListener {
	public void onMessagesListRespone(JSONObject obj);
	public void onMesssageDeleteRespone(JSONObject obj);
	public void OnMessageSentResponse(JSONObject obj);

}
