package com.lang.social.community.general;

import org.json.JSONObject;

public interface GeneralCommunityServerListener {
	public void onIsFriendsRespone(JSONObject obj);
	public void onFriendRequestRespone(JSONObject obj);
}
