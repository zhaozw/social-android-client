package com.lang.social.iocallback;

import org.json.JSONObject;

import com.lang.social.activities.SelfLearnMenuActivity;
import com.lang.social.activities.SocialLearnMenuActivity;
import com.lang.social.interfaces.GameCloseListener;
import com.lang.social.interfaces.GamesListListener;
import com.lang.social.picturequizgame.PictureQuizGameConstants;
import com.lang.social.picturequizgame.PictureQuizGameListener;
import com.lang.social.room.RoomActivity;
import com.lang.social.room.RoomConstants;
import com.lang.social.room.RoomListener;

public class GameIOCallBackHandler {
	
	private RoomListener mRoomListener;
	private GamesListListener mGamesListListener; 
	private GameCloseListener mCloseListener; 
	private PictureQuizGameListener pictureQuizGameListener;
	
	public void handleResponse(String eventName, JSONObject jsonResponse) {
		  if(eventName.equals(SocialLearnMenuActivity.userGameHostsResponse)){
				if(mGamesListListener != null){
					mGamesListListener.onGameListRecieved(jsonResponse);
				}
		}  else if(eventName.equals(RoomConstants.playerJoinedGameResponse) || eventName.equals(RoomConstants.teacherJoinedStudentGameResponse) 
				|| eventName.equals(RoomConstants.studentJoinedTeacherGameResponse)){
			if(mRoomListener != null){
				mRoomListener.onPlayerJoinedRoomEvent(jsonResponse);
			}
		} else if(eventName.equals(RoomConstants.gameLaunchedRoomResponseKEY)){
			if(mRoomListener != null){
				mRoomListener.onGameLaunchedRoomEvent(jsonResponse);
			}
		} else if(eventName.equals(RoomConstants.playerQuitGameNotificationResponse)){
			if(mCloseListener != null){
				mCloseListener.onPlayerLeftGameEvent(jsonResponse);
			}
		} else if(eventName.equals(PictureQuizGameConstants.RoundResponse)){
			if(pictureQuizGameListener != null){
				pictureQuizGameListener.OnRoundRecieved(jsonResponse);
			}
		}
	}
	
	public void setGameCloseListener(GameCloseListener i_CloseListener){
		mCloseListener = i_CloseListener;
	}

	public void setGamesListListener(GamesListListener i_gamesListListener) {
		mGamesListListener = i_gamesListListener;
	}
	
	public void setRoomListener(RoomListener roomListener) {
		mRoomListener = roomListener;
	}
	
	public void setPictureQuizGameListener(PictureQuizGameListener pictureQuizGameListener) {
		this.pictureQuizGameListener = pictureQuizGameListener;
	}

}
