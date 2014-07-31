package com.lang.social.community.onlineusers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.lang.social.R;
import com.lang.social.community.friends.FriendItem;
import com.lang.social.community.general.UserProfileWindow;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.UserController;
import com.lang.social.utils.JSONUtils;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class OnlineUsersTabFragment extends Fragment implements OnlineUsersServerListener {
	private PullToRefreshListView pullToRefreshView;
	private CustomOnlineUsersListAdapter adapter;
	private ArrayList<OnlineUserItem> m_OnlineUsersList = new ArrayList<OnlineUserItem>();
	private UserProfileWindow userProfileWindow;
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	     final View rootView = inflater.inflate(R.layout.fragment_online_users_tab_layout, container, false);
	     setListViewListeners(rootView);
	     setServerListener();
	     
	     adapter = new CustomOnlineUsersListAdapter(getActivity(), R.layout.online_user_list_row, m_OnlineUsersList);
	     pullToRefreshView.setAdapter(adapter);
	     fetchOnlineUsersFromServer();
	     return rootView;
	 }
	 
	 private void setServerListener() {
		IOCallBackHandler.getInstance().setOnlineUsersListener(this);
		
	}

	private void fetchOnlineUsersFromServer() {
		ServerController.sendJSONMessage("onlineUsersRequest", new JSONObject());
		
	}

	private void setListViewListeners(final View rootView) {
			pullToRefreshView = (PullToRefreshListView) rootView.findViewById(R.id.lvOnlineUsersList);
	        pullToRefreshView.setOnRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh() {
					fetchOnlineUsersFromServer();
					pullToRefreshView.onRefreshComplete();
				}
	        });
	        
	        pullToRefreshView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
					OnlineUserItem onlineUserItem = m_OnlineUsersList.get(position);
					userProfileWindow = new UserProfileWindow(getActivity(), rootView, onlineUserItem.getUser());
		        	userProfileWindow.ShowProfileWindow();
				}
			}); 
		}

	@Override
	public void onOnlineUserListRespone(JSONObject responseJson) {
		m_OnlineUsersList.clear();	//For some reason, I need to clear the list each time, otherwise I get duplicates
		Log.d("OnlineUsersTab", responseJson.toString());
		 JSONArray onlineUsersArray = JSONUtils.getJSONArray(responseJson, "onlineUsersArray");
		 for (int i = 0; i < onlineUsersArray.length(); i++) {
			 try {
				 JSONObject onlineUserJson = onlineUsersArray.getJSONObject(i);
				 OnlineUserItem onlineUserItem = new OnlineUserItem(onlineUserJson);
				 if(onlineUserItem.getUser().isFacebookUser()) {
					 if(onlineUserItem.getUser().getProfileID().equals(UserController.getUser().getProfileID()) == true) {
						 //continue; //Disbaling to for now..
					 }
				 }
				 else {
					 if(onlineUserItem.getUser().getUserName().equals(UserController.getUser().getUserName()) == true) {
						 //continue; //Disbaling to for now..
					 }
				 }
				 m_OnlineUsersList.add(onlineUserItem);
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
				 adapter.notifyDataSetChanged();
			 }
		 }); 
	 }
	 
}
