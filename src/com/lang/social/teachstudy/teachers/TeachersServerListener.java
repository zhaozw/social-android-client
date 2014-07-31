package com.lang.social.teachstudy.teachers;

import org.json.JSONObject;

public interface TeachersServerListener {
	public void onFetchTeachersGamesHosts(JSONObject jsonRespone);
	public void OnStudentJoinTeacherGameResponse(JSONObject jsonResponse);
}
