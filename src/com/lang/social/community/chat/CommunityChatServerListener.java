package com.lang.social.community.chat;

import org.json.JSONObject;

public interface CommunityChatServerListener {
	public void onChatMsgRecived(JSONObject obj);
}
