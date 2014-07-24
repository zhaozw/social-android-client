package com.lang.social.teachstudy;

import java.io.Serializable;

import org.json.JSONObject;

import com.lang.social.logic.User;

@SuppressWarnings("serial")
public class StudentTeacherGameItem implements Serializable {
	private User mStudent;
	private int mRoomID;
	
	public StudentTeacherGameItem(User user, int roomID) {
		mStudent = user;
		this.mRoomID = roomID;
	}
	
	public StudentTeacherGameItem(JSONObject userJson, int roomID) {
		mStudent = new User(userJson);
		this.mRoomID = roomID;
	}

	public User getPlayer1() {
		return mStudent;
	}

	public int getRoomID() {
		return mRoomID;
	}
}
