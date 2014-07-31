package com.lang.social.community.friends;

import org.json.JSONObject;

public interface FriendsServerListener {
	public void onFriendsListRespone(JSONObject obj);
	public void onFriendsRequestListRespone(JSONObject obj);
	public void onIgnoreFriendRequest(JSONObject obj);
	public void onAcceptFriendRequestRespone(JSONObject obj);
	public void onUnfriedRespone(JSONObject obj);
	public void onMessageSentRespone(JSONObject obj);
}
