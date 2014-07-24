
package com.lang.social.iocallback;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

import org.json.JSONObject;

import android.util.Log;

import com.lang.social.chat.ChatListener;
import com.lang.social.community.chat.CommunityChatTabFragment;
import com.lang.social.community.friends.FriendsServerListener;
import com.lang.social.community.general.GeneralCommunityServerListener;
import com.lang.social.community.messages.MessagesServerListener;
import com.lang.social.community.onlineusers.OnlineUsersServerListener;
import com.lang.social.competition.HeadToHeadListener;
import com.lang.social.competition.SocialLearnCreateMenuListener;
import com.lang.social.competition.SocialLearnJoinMenuListener;
import com.lang.social.hangman.HangmanListener;
import com.lang.social.interfaces.GameCloseListener;
import com.lang.social.interfaces.GamesListListener;
import com.lang.social.interfaces.HomeActivityListener;
import com.lang.social.interfaces.ProfilePageListener;
import com.lang.social.interfaces.UserLanguageUpdateListener;
import com.lang.social.interfaces.UserLoginListener;
import com.lang.social.interfaces.UserProfileListener;
import com.lang.social.interfaces.UserRegisterListener;
import com.lang.social.picturequizgame.PictureQuizGameListener;
import com.lang.social.profile.FriendDetailsResponseListener;
import com.lang.social.quizgame.QuizActivityListener;
import com.lang.social.quizgame.QuizIOCallBackHandler;
import com.lang.social.room.RoomListener;
import com.lang.social.teachstudy.StudentTeacherServerListener;
import com.lang.social.teachstudy.lesson.LessonFragmentListener;
import com.lang.social.teachstudy.lesson.LessonGameListener;
import com.lang.social.teachstudy.lesson.PhotosTwoWayFragmentListener;
import com.lang.social.teachstudy.students.StudentsServerListener;
import com.lang.social.teachstudy.teachers.TeachersServerListener;

public class IOCallBackHandler implements IOCallback {

	private IOCallBackHandler() {}
	
	private static IOCallBackHandler ioCallBackHandlerInstance;
	
	private static UserIOCallBackHandler userIOCallBackHandler;
	private static ActivitiesIOCallBackHandler activitiesIOCallBackHandler;
	private static GameIOCallBackHandler gameIOCallBackHandler;
	private static QuizIOCallBackHandler quizGameIOCallBackHandler;
	private static CompetitionIOCallBackHandler competitionIOCallBackHandler;
	private static ChatIOCallBackHandler chatIOCallBackHandler;
	private static CommunityIOCallBackHandler communityIOCallBackHandler;
	private static StudentTeacherIOCallBackHandler studentTeacherIOCallBackHandler;
	private static LessonIOCallBackHandler lessonIOCallBackHandler;
	private static HangmanIOCallBackHandler hangmanIOCallBackHandler;
	
	public void setHangmanListener(HangmanListener hangmanListener){
		  hangmanIOCallBackHandler.setHangmanListener(hangmanListener);
	}
	
	public void setLoginListener(UserLoginListener i_loginListener){
		userIOCallBackHandler.setLoginListener(i_loginListener);
	}
	
	public void setStudentTeacherListener(StudentTeacherServerListener i_studentTeacherListener){
		studentTeacherIOCallBackHandler.setStudentTeacherServerListener(i_studentTeacherListener);
	}
	
	
	public void setStudentsTabFragmentListener(StudentsServerListener i_studentListener){
		studentTeacherIOCallBackHandler.setStudentsServerListener(i_studentListener);
	}
	
	public void setTeachersTabFragmentListener(TeachersServerListener i_teacherListener){
		studentTeacherIOCallBackHandler.setTeachersServerListener(i_teacherListener);
	}
	
	public void setRegisterListener(UserRegisterListener i_registerListener){
		userIOCallBackHandler.setRegisterListener(i_registerListener);
	}
	
	public void setProfileUpdateListener(UserProfileListener i_profileUpdateListener){
		userIOCallBackHandler.setProfileUpdateListener(i_profileUpdateListener);
	}

	public void setHomeActivityLoadListener(HomeActivityListener i_HomeActivityListener){
		activitiesIOCallBackHandler.setHomeActivityLoadListener(i_HomeActivityListener);
	}

	public void setGamesListListener(GamesListListener i_gamesListListener) {
		gameIOCallBackHandler.setGamesListListener(i_gamesListListener);
	}
	
	public void setLanguageUpdateListener(UserLanguageUpdateListener i_languageUpdateListener){
		userIOCallBackHandler.setLanguageUpdateListener(i_languageUpdateListener);
	}
	
	public void setSocialLearnJoinMenuListener(SocialLearnJoinMenuListener socialLearnJoinMenuListener){
		competitionIOCallBackHandler.setSocialLearnMenuJoinListener(socialLearnJoinMenuListener);
	}
	
	public void setSocialLearnCreateMenuListener(SocialLearnCreateMenuListener socialLearnCreateMenuListener){
		competitionIOCallBackHandler.setSocialLearnMenuCreateListener(socialLearnCreateMenuListener);
	}
	
	public void setQuizActivityListener(QuizActivityListener quizActivityListener){
		quizGameIOCallBackHandler.setQuizActivityListener(quizActivityListener);
	}

	public void setRoomListener(RoomListener roomListener){
		gameIOCallBackHandler.setRoomListener(roomListener);
	}
	
	public void setChatListener(ChatListener chatListener){
		chatIOCallBackHandler.setChatListener(chatListener);
	}
	
	public void setHeadToHeadListener(HeadToHeadListener headToHeadListener) {
		competitionIOCallBackHandler.setHeadToHeadListener(headToHeadListener);
	}
	
	public void setFriendsListener(FriendsServerListener i_firendsServerListener){
		communityIOCallBackHandler.setFriendsListener(i_firendsServerListener);
	}
	
	public void setMessagesListener(MessagesServerListener i_messagesServerListener){
		communityIOCallBackHandler.setMessagesListener(i_messagesServerListener);
	}
	
	public void setGeneralCommunityListener(GeneralCommunityServerListener i_generalCommunityListener){
		communityIOCallBackHandler.setGeneralCommunityListener(i_generalCommunityListener);
	}
	
	public void setOnlineUsersListener(OnlineUsersServerListener i_onlineUsersListener){
		communityIOCallBackHandler.setOnlineUsersServerListener(i_onlineUsersListener);
	}
	
	public void setGameCloseListener(GameCloseListener i_CloseListener){
		gameIOCallBackHandler.setGameCloseListener(i_CloseListener);
	}
	
	public void setCloseActiveLessonGame(LessonGameListener i_CloseLessonListener){
		lessonIOCallBackHandler.setCloseActiveGameListener(i_CloseLessonListener);
	}
	
	public void SetPictureQuizGameListener(PictureQuizGameListener pictureQuizGameListener) {
		gameIOCallBackHandler.setPictureQuizGameListener(pictureQuizGameListener);
	}
	
	public void setProfileDetailsListener(ProfilePageListener profilePageListener) {
		userIOCallBackHandler.setProfileDetailsListener(profilePageListener);
	}
	
	public void setProfileFriendReseponseListener(FriendDetailsResponseListener friendDetailsResponseListener) {
		userIOCallBackHandler.setFriendDetailsResponseListener(friendDetailsResponseListener);
	}
	
	public void setCommunityChatServerListener(CommunityChatTabFragment i_chatServerListener) {
		communityIOCallBackHandler.setCommunityChatServerListener(i_chatServerListener);
	}
	
	 public void setLessonFragmentListener(LessonFragmentListener i_LessonFragmentListener){
		 lessonIOCallBackHandler.setLessonFragmentListener(i_LessonFragmentListener);
	}
	 
	 public void setPhotosTwoWayFragmentListener(PhotosTwoWayFragmentListener i_PhotosTwoWayFragmentListener){
		  lessonIOCallBackHandler.setPhotosTwoWayFragmentListener(i_PhotosTwoWayFragmentListener);
	}


	public static IOCallBackHandler getInstance(){
		if(ioCallBackHandlerInstance == null){
			ioCallBackHandlerInstance = new IOCallBackHandler();
			userIOCallBackHandler = new UserIOCallBackHandler();
			gameIOCallBackHandler = new GameIOCallBackHandler();
			quizGameIOCallBackHandler = new QuizIOCallBackHandler();
			competitionIOCallBackHandler = new CompetitionIOCallBackHandler();
			activitiesIOCallBackHandler = new ActivitiesIOCallBackHandler();
			chatIOCallBackHandler = new ChatIOCallBackHandler();
			communityIOCallBackHandler = new CommunityIOCallBackHandler();
			studentTeacherIOCallBackHandler = new StudentTeacherIOCallBackHandler();
			lessonIOCallBackHandler = new LessonIOCallBackHandler();
			hangmanIOCallBackHandler = new HangmanIOCallBackHandler();
		}

		return ioCallBackHandlerInstance;
	}

	@Override
	public void on(String eventName, IOAcknowledge arg1, Object... arguments) {
		 Log.d("IOCallBackHandler", "KEY : " + eventName);
		 JSONObject jsonResponse = (JSONObject) arguments[0];
		 Log.d("IOCallBackHandler", "VALUE : " + jsonResponse.toString());
		 
		userIOCallBackHandler.handleResponse(eventName, jsonResponse);
		gameIOCallBackHandler.handleResponse(eventName, jsonResponse);
		quizGameIOCallBackHandler.handleResponse(eventName, jsonResponse);
		activitiesIOCallBackHandler.handleResponse(eventName, jsonResponse);
		competitionIOCallBackHandler.handleResponse(eventName, jsonResponse);
		chatIOCallBackHandler.handleResponse(eventName, jsonResponse);
		communityIOCallBackHandler.handleResponse(eventName, jsonResponse);
		studentTeacherIOCallBackHandler.handleResponse(eventName, jsonResponse);
		lessonIOCallBackHandler.handleResponse(eventName, jsonResponse);
		hangmanIOCallBackHandler.handleResponse(eventName, jsonResponse);

	}

	@Override
	public void onConnect() {
		Log.d("SocketIO", "connected to socket.io server");
	}

	@Override
	public void onDisconnect() {
		Log.d("SocketIO", "disconnected from socket.io server");
	}

	@Override
	public void onError(SocketIOException arg0) {
		Log.d("SocketIO", arg0.getMessage());
		Log.d("SocketIO", "error in socket.io server");
	}

	@Override
	public void onMessage(String arg0, IOAcknowledge arg1) {
		Log.d("SocketIO", "msg from server : " + arg0);
	}

	@Override
	public void onMessage(JSONObject jsonMessage, IOAcknowledge arg1) {
			Log.d("SocketIO", "msg from server : " + jsonMessage.toString());
	}



}

