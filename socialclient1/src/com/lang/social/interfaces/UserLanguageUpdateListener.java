package com.lang.social.interfaces;

import org.json.JSONObject;

public interface UserLanguageUpdateListener {
	public void OnLanguageUpdate(int position);
	public void OnLanguageUpdateResponse(JSONObject json);
}
