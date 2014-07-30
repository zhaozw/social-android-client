package com.lang.social.teachstudy.lesson;

import org.json.JSONObject;

public interface LessonGameListener {
	public void onActiveGameClosed(JSONObject jsonResponse);
	public void onAudioMessageRecived(JSONObject jsonResponse);	//If it will work, I will make this in a new interface!
	public void onImageResponse(JSONObject jsonResponse);
}
