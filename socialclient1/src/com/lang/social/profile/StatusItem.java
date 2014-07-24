package com.lang.social.profile;

import org.json.JSONObject;

import com.lang.social.logic.UserController;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.LanguageMap;

public class StatusItem {
	
	private String language;
	private int iconResLanguage;
	private String level;
	private String points;
	
	public StatusItem(String language, int iconResLanguage, String level, String points) {
		this.language = language;
		this.iconResLanguage = iconResLanguage;
		this.level = level;
		this.points = points;
	}
	
	public StatusItem(JSONObject json) {
		this.language = JSONUtils.getStringFromJSON(json, ProfileConstants.UserLanguage, "err parsing language");
		this.iconResLanguage = LanguageMap.getLanguageResHashMap().get(language);
		this.level = JSONUtils.getStringFromJSON(json, ProfileConstants.UserStatsLevel, "err parsing level");
		this.points = JSONUtils.getStringFromJSON(json, ProfileConstants.UserStatsPoints, "err parsing points");;
	}

	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public int getIconResLanguage() {
		return iconResLanguage;
	}
	
	public void setIconResLanguage(int iconResLanguage) {
		this.iconResLanguage = iconResLanguage;
	}
	
	public String getLevel() {
		return level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}
	
	public String getPoints() {
		return points;
	}
	
	public void setPoints(String points) {
		this.points = points;
	}
	
	
	
}