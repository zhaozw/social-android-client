package com.lang.social.iocallback;

import org.json.JSONObject;

import android.util.Log;

import com.lang.social.competition.CompetitionConstants;
import com.lang.social.competition.HeadToHeadListener;
import com.lang.social.competition.SocialLearnCreateMenuListener;
import com.lang.social.competition.SocialLearnJoinMenuListener;
import com.lang.social.room.RoomConstants;

public class CompetitionIOCallBackHandler {

	private SocialLearnJoinMenuListener socialLearnMenuJoinGameListener;
	private SocialLearnCreateMenuListener socialLearnMenuCreateGameListener;
	private HeadToHeadListener mHeadListener;
	
	public void handleResponse(String eventName, JSONObject jsonResponse) {
		 if (eventName.equals(RoomConstants.startNewGameResponse)){
			if(socialLearnMenuCreateGameListener != null){
				socialLearnMenuCreateGameListener.OnCompetitionGameStartResponse(jsonResponse);
			}
		} else if(eventName.equals(RoomConstants.playerJoinedGameResponse)){
			if(socialLearnMenuJoinGameListener != null){
				socialLearnMenuJoinGameListener.OnCompetitionJoinGameResponse(jsonResponse);
			}
		} else if(eventName.equals(CompetitionConstants.QuestionAndAnswersResponse)){
			if(mHeadListener != null){
				mHeadListener.onQuestionAndAnswersRecieved(jsonResponse);
			}
		}  else if(eventName.equals(CompetitionConstants.HeadToHeadAnswerResponseKey)){
			if(mHeadListener != null){
				mHeadListener.onAnswerResponse(jsonResponse);
			}
		}  
	}
	
	public void setSocialLearnMenuJoinListener(SocialLearnJoinMenuListener socialLearnMenuJoinGameListener) {
		this.socialLearnMenuJoinGameListener = socialLearnMenuJoinGameListener;
	}
	
	public void setSocialLearnMenuCreateListener(SocialLearnCreateMenuListener socialLearnMenuCreateGameListener) {
		this.socialLearnMenuCreateGameListener = socialLearnMenuCreateGameListener;
	}
	
	public void setHeadToHeadListener(HeadToHeadListener headToHeadListener) {
		this.mHeadListener = headToHeadListener;
	}
}
