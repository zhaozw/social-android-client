package com.lang.social.iocallback;

import org.json.JSONObject;

import com.lang.social.competition.HeadToHeadListener;
import com.lang.social.competition.SocialGameConstants;
import com.lang.social.competition.SocialLearnCreateMenuListener;
import com.lang.social.competition.SocialLearnJoinMenuListener;
import com.lang.social.memorygame.MemoryGameActivity;
import com.lang.social.memorygame.MemoryGameListener;
import com.lang.social.room.RoomConstants;

public class SocialIOCallBackHandler {

	private SocialLearnJoinMenuListener socialLearnMenuJoinGameListener;
	private SocialLearnCreateMenuListener socialLearnMenuCreateGameListener;
	private HeadToHeadListener mHeadListener;
	private MemoryGameListener memoryGameListener;
	
	public void handleResponse(String eventName, JSONObject jsonResponse) {
		 if (eventName.equals(RoomConstants.startNewGameResponse)){
			if(socialLearnMenuCreateGameListener != null){
				socialLearnMenuCreateGameListener.OnGameStartResponse(jsonResponse);
			}
		} else if(eventName.equals(RoomConstants.playerJoinedGameResponse)){
			if(socialLearnMenuJoinGameListener != null){
				socialLearnMenuJoinGameListener.OnJoinGameResponse(jsonResponse);
			}
		} else if(eventName.equals(SocialGameConstants.QuestionAndAnswersResponse)){
			if(mHeadListener != null){
				mHeadListener.onQuestionAndAnswersRecieved(jsonResponse);
			}
		}  else if(eventName.equals(SocialGameConstants.HeadToHeadAnswerResponseKey)){
			if(mHeadListener != null){
				mHeadListener.onAnswerResponse(jsonResponse);
			}
		}   else if(eventName.equals(MemoryGameActivity.GameRoundResponseKey)){
			if(memoryGameListener != null){
				memoryGameListener.OnGameRoundRecieved(jsonResponse);
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

	public void setMemoryGameListener(MemoryGameListener memoryGameListener) {
		this.memoryGameListener = memoryGameListener;
	}
}
