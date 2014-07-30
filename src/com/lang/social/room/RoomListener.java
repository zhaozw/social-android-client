package com.lang.social.room;

import org.json.JSONObject;

public interface RoomListener {
	public void onPlayerJoinedRoomEvent(JSONObject jsonResponse);
	public void onGameLaunchedRoomEvent(JSONObject jsonResponse);
}
