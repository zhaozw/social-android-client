package com.lang.social.quizgame;

public class Question {
	private String wordToTranslate;
	private String[] possibleAnswers;
	private int correctAnswerIndex;
	
	public Question(String question, String[] possibleAnswers)
	{
		this.wordToTranslate = question;
		this.possibleAnswers = possibleAnswers;
	}
	
	public void setCurrentAnswerIndex(int i) {correctAnswerIndex = i;}
	
	public String GetQuestion() {return wordToTranslate;}
	
	public String[] GetPossibleAnswers() {return possibleAnswers;}
	public String GetPossibleAnswerByIndex(int i) {return possibleAnswers[i];}
	
	public int GetCorrectAnswerIndex() {return correctAnswerIndex;}
}
