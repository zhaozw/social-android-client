package com.lang.social.competition;

import java.io.Serializable;

import com.lang.social.logic.User;


public class CompetitionPlayer implements Serializable {
	
	private static final long serialVersionUID = 1844867154426811593L;
	
	public static final int kTotalNumOfHeartsPerUser = 5;
	private int mNumOfHearts = kTotalNumOfHeartsPerUser;
	private User mUser;
	
	public CompetitionPlayer(User user) {
		mUser = user;
	}

	public void answeredWrong() {
		if(mNumOfHearts > 0){
			mNumOfHearts--;
		}
	}
	
	public void answeredCorrect() {
		if(mNumOfHearts < kTotalNumOfHeartsPerUser){
			mNumOfHearts++;
		}
	}

	public int getNumOfHearts() {
		return mNumOfHearts;
	}

	public User getUser() {
		return mUser;
	}
}
