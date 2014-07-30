package com.lang.social.quizgame;

import org.json.JSONObject;

public interface QuizActivityListener {
	public void onQuestionRecived(JSONObject obj);
	public void onAnswerRecived(JSONObject obj);
	public void onStartNewQuizGameResponse(JSONObject obj);
}
