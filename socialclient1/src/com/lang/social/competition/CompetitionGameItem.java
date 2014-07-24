package com.lang.social.competition;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.lang.social.logic.User;


@SuppressWarnings("serial")
public class CompetitionGameItem implements Serializable {
	
	private User mPlayer1;
	private int mRoomID;
	
	public CompetitionGameItem(JSONObject userJson, int roomID) {
		mPlayer1 = new User(userJson);
		this.mRoomID = roomID;
	}

	public User getPlayer1() {
		return mPlayer1;
	}

	public int getRoomID() {
		return mRoomID;
	}
}
