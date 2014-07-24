package com.lang.social.competition;

import org.json.JSONObject;

import com.lang.social.utils.JSONUtils;

public class Round {
	
	private String mQuestion;
	private String[] mAnswers;
	private String mCorrectAnswer;
	
	public Round(JSONObject roundJson) {
		mQuestion = JSONUtils.getStringFromJSON(roundJson, SocialGameConstants.QuestionKey, "could not get question");
		mAnswers = JSONUtils.getStringArrayFromJSON(roundJson, SocialGameConstants.AnswersKey, "could not get answers");
		mCorrectAnswer = JSONUtils.getStringFromJSON(roundJson, SocialGameConstants.AnswersIndexKey, "could not get corrent answer index");
	}

	public String getmQuestion() {
		return mQuestion;
	}

	public void setmQuestion(String mQuestion) {
		this.mQuestion = mQuestion;
	}

	public String[] getmAnswers() {
		return mAnswers;
	}

	public void setmAnswers(String[] mAnswers) {
		this.mAnswers = mAnswers;
	}

	public String getmCorrectAnswer() {
		return mCorrectAnswer;
	}

	public void setmCorrectAnswer(String mCorrectAnswer) {
		this.mCorrectAnswer = mCorrectAnswer;
	}
	
	
}
