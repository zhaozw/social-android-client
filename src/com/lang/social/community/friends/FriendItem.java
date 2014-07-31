package com.lang.social.community.friends;

import org.json.JSONObject;

import com.lang.social.logic.User;

public class FriendItem {
	private Friend friend;
	
	public FriendItem(JSONObject userJson) {
		friend = new Friend(userJson);
	}

	public Friend getFriend() {
		return friend;
	}

}
