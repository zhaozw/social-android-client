package com.lang.social.quizgame;

import org.json.JSONObject;

public class QuizIOCallBackHandler {
	
	private QuizActivityListener quizActivityListener;
	
	private static final String LoadHomeResponse = "LoadHomeResponse";
	private static final String onQuestion = "onQuestion";
	private static final String Answer = "Answer";
	private static final String restartGameResponse = "restartGameResponse";
	private static final String onLoadAnswer = "onLoadAnswer";
	private static final String onStartNewGameResponse = "startNewQuizGameResponse"; 
	private static final String onUpdatePointsResponse = "updatePointsResponse";
	
	public void setQuizActivityListener(QuizActivityListener quizActivityListener){
		this.quizActivityListener = quizActivityListener;
	}
	
	public void handleResponse(String eventName, JSONObject jsonResponse) {
		if (eventName.equals(LoadHomeResponse)){
			if(quizActivityListener != null){
				quizActivityListener.onAnswerRecived(jsonResponse);
			}
		} else if (eventName.equals(onQuestion)) {
		   if (quizActivityListener != null) {
			    quizActivityListener.onQuestionRecived(jsonResponse);
		  }
		} else if (eventName.equals(Answer)) {
		   if (quizActivityListener != null) {
			    quizActivityListener.onAnswerRecived(jsonResponse);
		   }
		}
		else if (eventName.equals(onStartNewGameResponse)) {
			   if (quizActivityListener != null) {
				    quizActivityListener.onStartNewQuizGameResponse(jsonResponse);
			   }
			}
	}
}
	
	

