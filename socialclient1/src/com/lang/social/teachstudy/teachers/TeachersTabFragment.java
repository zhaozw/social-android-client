
package com.lang.social.teachstudy.teachers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.lang.social.R;
import com.lang.social.competition.SocialGameConstants;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.GameType;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.room.RoomActivity;
import com.lang.social.room.RoomConstants;
import com.lang.social.teachstudy.StudentTeacherConstants;
import com.lang.social.teachstudy.StudentTeacherGameItem;
import com.lang.social.utils.JSONUtils;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TeachersTabFragment extends Fragment implements TeachersServerListener {
private PullToRefreshListView pullToRefreshView;
	
	private TeachersHostsAdapter AdapterHostGames;
	private ArrayList<StudentTeacherGameItem> mStudentTeacherGameItems = new ArrayList<StudentTeacherGameItem>();
//	-------------------------------------------------------------------------
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_teachers_tab_layout, container, false);
        
//      ------Pull To Refresh Initialization------
      setListViewListeners(rootView);
      
      AdapterHostGames = new TeachersHostsAdapter(getActivity(), R.layout.student_teacher_game_list_row, mStudentTeacherGameItems);
      pullToRefreshView.setAdapter(AdapterHostGames);
      IOCallBackHandler.getInstance().setTeachersTabFragmentListener(this);
      
//      mStudentTeacherGameItems.add(new StudentTeacherGameItem(UserController.getUser(), 1));
//      AdapterHostGames.notifyDataSetChanged();
        
        return rootView;
	}
	
	private void setListViewListeners(View rootView) {
		pullToRefreshView = (PullToRefreshListView) rootView.findViewById(R.id.lvTeachersList);
        pullToRefreshView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				JSONObject jsonToSend = new JSONObject();
				try {
					jsonToSend.put("GameType", (GameType.StudentTeacher).toString());
					ServerController.sendJSONMessage("userGameHostsRequest", jsonToSend);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        });
        pullToRefreshView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int which, long arg3) {
				requestJoinGame(which);
			}
		}); 
	}
	
	private void requestJoinGame(int which) {
		
		StudentTeacherGameItem gameItem = mStudentTeacherGameItems.get(which);
		JSONObject jsonToSend = JSONUtils.CreateJSON("GameRoomID", gameItem.getRoomID());
		
//		IOCallBackHandler.getInstance().setStudentTeacherListener(null);
//		IOCallBackHandler.getInstance().setSocialLearnJoinMenuListener(this);
		
		JSONUtils.setStringValue(jsonToSend, "GameType", (GameType.TeacherGame).toString());
		ServerController.sendJSONMessage(RoomConstants.joinGameRequest, jsonToSend);
	}

	@Override
	public void onFetchTeachersGamesHosts(JSONObject jsonRespone) {
		mStudentTeacherGameItems.clear();	//For some reason, I need to clear the list each time, otherwise I get duplicates
		Log.d("TeachersTab", jsonRespone.toString());
		 JSONArray waitingTeachersArray = JSONUtils.getJSONArray(jsonRespone, "jsonUserHosts");
		 for (int i = 0; i < waitingTeachersArray.length(); i++) {
			 try {
				 JSONObject HostAndGameNumberJson = waitingTeachersArray.getJSONObject(i);
				 JSONObject hostTeacher = HostAndGameNumberJson.getJSONObject("host");
				 int GameRoomID = HostAndGameNumberJson.getInt("GameRoomID");
				 StudentTeacherGameItem teacherGameItem = new StudentTeacherGameItem(hostTeacher, GameRoomID);
				 mStudentTeacherGameItems.add(teacherGameItem);
			 } catch (JSONException e) {
				 Log.d("OnlineUsersTab", "Error parsing onlineUser" +
				 		"");
				 throw new RuntimeException();
			 }
		 }
		 updateProgressDialogAndAdapter();
		
	}
	
	private void updateProgressDialogAndAdapter() {
		 getActivity().runOnUiThread(new Runnable() {
			 @Override
			 public void run() {
				 AdapterHostGames.notifyDataSetChanged();
				 pullToRefreshView.onRefreshComplete(); //Action complete
			 }
		 }); 
	 }

	@Override
	public void OnStudentJoinTeacherGameResponse(JSONObject jsonResponse) {
		Log.d("StudentTabFragment", "in OnStudentJoinGameResponse");
		if(JSONUtils.getStringFromJSON(jsonResponse, "result", null).equals("OK"))
		{
			User playerTeacher = new User(JSONUtils.getJSONObject(jsonResponse, SocialGameConstants.IntentPlayer1Key));
	    	Intent intent = new Intent(getActivity(), RoomActivity.class);
	    	intent.putExtra(StudentTeacherConstants.IntentRoomStateKEY, "Joined");	
	    	intent.putExtra(SocialGameConstants.IntentPlayer1Key, playerTeacher);	
	    	intent.putExtra(RoomConstants.GameTypeKEY, (GameType.TeacherGame));	
	    	intent.putExtra(SocialGameConstants.IntentPlayer2Key, UserController.getUser());	
	    	intent.putExtra(StudentTeacherConstants.IsTeacherHost, StudentTeacherConstants.TeacherHost);
	    	startActivity(intent);
		}
		
	}
}
