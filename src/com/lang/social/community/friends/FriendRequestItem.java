package com.lang.social.community.friends;

import org.json.JSONObject;

import com.lang.social.logic.User;

public class FriendRequestItem {
	private User RequestingFriendshipUser;
	
	public FriendRequestItem(JSONObject userJson) {
		RequestingFriendshipUser = new User(userJson);
	}
	
	public FriendRequestItem(User user) {
		RequestingFriendshipUser = user;
	}

	public User getRequestingFriendshipUserd() {
		return RequestingFriendshipUser;
	}
}
