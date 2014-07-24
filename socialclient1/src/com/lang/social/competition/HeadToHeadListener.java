package com.lang.social.competition;

import org.json.JSONObject;

public interface HeadToHeadListener {
	void onAnswerResponse(JSONObject response);
	void onQuestionAndAnswersRecieved(JSONObject json);
}
