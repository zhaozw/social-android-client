package com.lang.social.community.onlineusers;

import org.json.JSONObject;

public class OnlineUserItem {
	private OnlineUser m_OnlineUser;
	
	public OnlineUserItem(JSONObject userJson) {
		m_OnlineUser = new OnlineUser(userJson);
	}

	public OnlineUser getUser() {
		return m_OnlineUser;
	}
}
