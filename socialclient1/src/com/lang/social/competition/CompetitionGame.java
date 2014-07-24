package com.lang.social.competition;

import java.io.Serializable;

import org.json.JSONObject;

import android.util.Log;

import com.lang.social.logic.User;


public class CompetitionGame implements Serializable {
	
	private static final long serialVersionUID = 1073692598588675845L;

	public enum PlayerNumber {
		Player1,
		Player2
	}
	
	private CompetitionPlayer mPlayer1;
	private CompetitionPlayer mPlayer2;
	private PlayerNumber mCurrentPlayerNumber = PlayerNumber.Player1;
	
	private String mQuestion;
	private String[] mAnswers;
	
	public CompetitionGame(CompetitionPlayer player1, CompetitionPlayer player2){
		mPlayer1 = player1;
		mPlayer2 = player2;
	}

	public CompetitionPlayer getPlayer1() {
		return mPlayer1;
	}

	public CompetitionPlayer getPlayer2() {
		return mPlayer2;
	}

	public PlayerNumber getCurrentPlayerNumber() {
		return mCurrentPlayerNumber;
	}
	
	public void setCurrentPlayerNumber(PlayerNumber mCurrentPlayer) {
		this.mCurrentPlayerNumber = mCurrentPlayer;
	}

	public User getCurrentUser() {
		if(mCurrentPlayerNumber == PlayerNumber.Player1){
			return mPlayer1.getUser();
		}
		return mPlayer2.getUser();
	}
	
	public CompetitionPlayer getCurrentPlayer() {
		return mCurrentPlayerNumber == PlayerNumber.Player1 ? mPlayer1 : mPlayer2;
	}
	
	public void setNextRound(String[] answers, String question){
		mQuestion = question;
		mAnswers = answers;
	}
	
	public String GetQuestion(){
		return mQuestion;
	}
	
	public String[] getAnswers(){
		return mAnswers;
	}

	public void switchPlayersTurns() {
		mCurrentPlayerNumber = mCurrentPlayerNumber == PlayerNumber.Player1 ? PlayerNumber.Player2 : PlayerNumber.Player1;
	}

	public void onWrongAnswer() {
		if(PlayerNumber.Player1 == mCurrentPlayerNumber){
			mPlayer1.answeredWrong();
		}
		else{
			mPlayer2.answeredWrong();
		}
	}
	
	
	public void onRightAnswer() {
		if(PlayerNumber.Player1 == mCurrentPlayerNumber){
			mPlayer1.answeredCorrect();
		}
		else{
			mPlayer2.answeredCorrect();
		}
	}
	
}
