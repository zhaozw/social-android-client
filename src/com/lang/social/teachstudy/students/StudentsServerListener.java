package com.lang.social.teachstudy.students;

import org.json.JSONObject;

public interface StudentsServerListener {
	public void onFetchStudentsGamesHosts(JSONObject jsonRespone);
	public void OnTeacherJoinStudentGameResponse(JSONObject jsonResponse);
}
