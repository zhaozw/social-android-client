package com.lang.social.chat;

import org.json.JSONObject;

public interface ChatListener {
	public void onMsgRecived(JSONObject obj);
}
