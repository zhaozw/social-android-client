//package com.lang.social.parsers;
//
//import java.util.ArrayList;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import com.lang.social.logic.User;
//
//public class CompetitionServerResponseParser extends ServerResponseParser {
//
//	public CompetitionServerResponseParser(JSONObject json, 
//			ArrayList<String> keysArrayList) {
//		super(json, keysArrayList);
//	}
//
//	@SuppressWarnings("serial")
//	public ArrayList<User> ParsePlayersFromResponse(){
//		ArrayList<User> users = null;
//		try {
//			JSONObject user1Json = jsonResponse.getJSONObject("Player1");
//			JSONObject user2Json = jsonResponse.getJSONObject("Player2");
//			User user1 = new User(user1Json);
//			User user2 = new User(user2Json);
//			users = new ArrayList<User>(){};
//			users.add(user1);
//			users.add(user2);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return users;
//	}	
//}
