package com.lang.social.community.friends;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Toast;

import com.lang.social.R;
import com.lang.social.community.general.UserProfileWindow;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.UserController;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;

public class FriendsTabFragment extends Fragment implements FriendsServerListener {
	private ListView friendsListView;
	private CustomFriendsListAdapter adapter;
	private ArrayList<FriendItem> listItems = new ArrayList<FriendItem>();
	
	private ListView friendsRequestsListView;
	private CustomFriendsRequestsListAdapter friendsRequestsAdapter;
	private ArrayList<FriendRequestItem> listFriendsRequestsItem = new ArrayList<FriendRequestItem>();
	
	public ProgressDialog progressDialog;
	
	private SlidingDrawer slidingDrawer;
	
	private UserProfileWindow userProfileWindow;
	
	 @SuppressWarnings("deprecation")
	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        final View rootView = inflater.inflate(R.layout.fragment_friends_tab_layout, container, false);
	        friendsListView = (ListView) rootView.findViewById(R.id.friends_list_listView);
	        adapter = new CustomFriendsListAdapter(getActivity(), R.layout.friend_list_row, listItems);
	        friendsListView.setAdapter(adapter);
	        
	        friendsRequestsListView = (ListView) rootView.findViewById(R.id.friends_requests_list_listView);
	        friendsRequestsAdapter = new CustomFriendsRequestsListAdapter(getActivity(), R.layout.friend_request_row, listFriendsRequestsItem);
	        friendsRequestsListView.setAdapter(friendsRequestsAdapter);
	        
	        final Button slideHandleButton = (Button) rootView.findViewById(R.id.slideHandleButton);
			slidingDrawer = (SlidingDrawer) rootView.findViewById(R.id.SlidingDrawer);
	        
			slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				slideHandleButton.setBackgroundResource(R.drawable.downarrowslide);
				}
			});
			
			slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
				@Override
				public void onDrawerClosed() {
				slideHandleButton.setBackgroundResource(R.drawable.uparrowslide);
				}
			});
			
			
	        //DEBUGGING
//	        JSONObject obj = new JSONObject();
//	        listFriendsRequestsItem.add(new FriendRequestItem(UserController.getUser()));
//	        friendsRequestsAdapter.notifyDataSetChanged();
	        
//	        progressDialog = LoadingDialog.getProgressDialog(getActivity());
//	        if(progressDialog.isShowing() == false) {
//	        	progressDialog.show();
//	        }
	        
	        setFriendsServerListener();
	        ServerController.sendJSONMessage("friendsRequest", new JSONObject());
	        ServerController.sendJSONMessage("friendsRequestsListRequest", new JSONObject());
	       
	        
	        friendsListView.setOnItemClickListener(new OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
	        		FriendItem friendItem = listItems.get(position);
	        		//showPopupUserProfile(rootView, friendItem);
	       			userProfileWindow = new UserProfileWindow(getActivity(), rootView, friendItem.getFriend());
		        	userProfileWindow.ShowProfileWindow();	
	        	}
	        });        
	        return rootView;
	 }
	 
	 private void setFriendsServerListener() {
		IOCallBackHandler.getInstance().setFriendsListener(this);
		
	}

	
	 @Override
	 public void onFriendsListRespone(JSONObject responseJson) {
		 listItems.clear();
		 Log.d("Friend", responseJson.toString());
		 JSONArray friendsArray = JSONUtils.getJSONArray(responseJson, "friends");
		 for (int i = 0; i < friendsArray.length(); i++) {
			 try {
				 JSONObject friendJson = friendsArray.getJSONObject(i);
				 FriendItem friendItem = new FriendItem(friendJson);
				 listItems.add(friendItem);
			 } catch (JSONException e) {
				 Log.d("Friend", "Error parsing friend");
				 throw new RuntimeException();
			 }
		 }
		 updateProgressDialogAndAdapter();
	 }

	 private void updateProgressDialogAndAdapter() {
		 getActivity().runOnUiThread(new Runnable() {
			 @Override
			 public void run() {
//				 if(progressDialog.isShowing() == true) {
//			        	progressDialog.dismiss();
//			        }
				 adapter.notifyDataSetChanged();
			 }
		 }); 
	 }
	 
//	 private void showPopupUserProfile(View anchorView, FriendItem friendItem) {
//
//		    View popupView = getActivity().getLayoutInflater().inflate(R.layout.user_profile_view, null);
//
//		    final PopupWindow popupWindow = new PopupWindow(popupView, 
//		                           LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//
//		    User friend = friendItem.getFriend();
//		    
//		    // Example: If you have a TextView inside `popup_layout.xml` 
//		    TextView ivLearningLang = (TextView) popupView.findViewById(R.id.tvLearning);
//		    if(friend.getLearningLanguage().equals("sp") || friend.getLearningLanguage().equals("Spanish")) {
//		    	ivLearningLang.setText("Learning: " + "Spanish");
//		    }
//		    else {	//Assuming there are only 2 languages, and the second is Israel
//		    	ivLearningLang.setText("Learning: " + "Hebrew");
//		    }
//		    
//		    
//		    
//		    Button closeButton = (Button) popupView.findViewById(R.id.btCloseUserProfile);
//		    closeButton.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					popupWindow.dismiss();
//					
//				}
//			});
//		    TextView tvFriendName = (TextView) popupView.findViewById(R.id.tvUserProfileName);
//		    tvFriendName.setText(friendItem.getFriend().getFullName());
//		     
//		    
//		    if(friend.getProfileID() != null){
//			    ProfilePictureView ppv = (ProfilePictureView) popupView.findViewById(R.id.ivUserFriendProfileImage);
//			    ppv.setProfileId(friend.getProfileID());
//			}
//
//		    // If the PopupWindow should be focusable
//		    popupWindow.setFocusable(true);
//
//		    // If you need the PopupWindow to dismiss when when touched outside 
//		    popupWindow.setBackgroundDrawable(new ColorDrawable());
//
//		    int location[] = new int[2];
//
//		    // Get the View's(the one that was clicked in the Fragment) location
//		    anchorView.getLocationOnScreen(location);
//
//		    // Using location, the PopupWindow will be displayed right under anchorView
//		    popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
//
//		}

	@Override
	public void onUnfriedRespone(JSONObject obj) {
		String result = JSONUtils.getStringFromJSON(obj, "result", null);
		if(result.equals("OK")) {
			listItems.remove(adapter.getFriendToRemovePos());
			updateProgressDialogAndAdapter();
			MyToaster.showToast(getActivity(), "Friend Was Successfully Removed!", Toast.LENGTH_LONG);
		}
		else {
			MyToaster.showToast(getActivity(), "Error At Removing Friend!", Toast.LENGTH_LONG);
		}
		
		
	}

	@Override
	public void onMessageSentRespone(JSONObject obj) {
		String result = JSONUtils.getStringFromJSON(obj, "result", null);
		if(result.equals("OK")) {
			MyToaster.showToast(getActivity(), "Message Was Successfully Sent!", Toast.LENGTH_LONG);
		}
		else {
			MyToaster.showToast(getActivity(), "Error At Sending Message!", Toast.LENGTH_LONG);
		}
	}

	@Override
	public void onFriendsRequestListRespone(JSONObject responseJson) {
		listFriendsRequestsItem.clear();
		Log.d("Friend", responseJson.toString());
		 JSONArray friendsRequestListArray = JSONUtils.getJSONArray(responseJson, "friendsRequestList");
		 for (int i = 0; i < friendsRequestListArray.length(); i++) {
			 try {
				 JSONObject friendshipRequesterJson = friendsRequestListArray.getJSONObject(i);
				 FriendRequestItem friendRequestItem = new FriendRequestItem(friendshipRequesterJson);
				 listFriendsRequestsItem.add(friendRequestItem);
			 } catch (JSONException e) {
				 Log.d("Friend", "Error parsing friend");
				 throw new RuntimeException();
			 }
		 }
		 updateFriendsRequestListAdapter();
	}
	
	private void updateFriendsRequestListAdapter() {
		 getActivity().runOnUiThread(new Runnable() {
			 @Override
			 public void run() {
				 friendsRequestsAdapter.notifyDataSetChanged();
			 }
		 }); 
	 }

	@Override
	public void onIgnoreFriendRequest(JSONObject obj) {
		String result = JSONUtils.getStringFromJSON(obj, "result", null);
		if(result.equals("OK")) {
			listFriendsRequestsItem.remove(friendsRequestsAdapter.getFriendRequestToRemovePos());
			updateFriendsRequestListAdapter();

			MyToaster.showToast(getActivity(), "Removed Friend Request!", Toast.LENGTH_LONG);
		}
		else {
			MyToaster.showToast(getActivity(), "Error At Removing Friend Request!", Toast.LENGTH_LONG);
		}
		
	}

	@Override
	public void onAcceptFriendRequestRespone(JSONObject obj) {
		String result = JSONUtils.getStringFromJSON(obj, "result", null);
		if(result.equals("OK")) {
			listFriendsRequestsItem.remove(friendsRequestsAdapter.getFriendRequestToRemovePos());
			updateFriendsRequestListAdapter();
			ServerController.sendJSONMessage("friendsRequest", new JSONObject());
			MyToaster.showToast(getActivity(), "Friend Has Been Successfully Added To Your List!", Toast.LENGTH_LONG);
		}
		else {
			MyToaster.showToast(getActivity(), "Error At Adding Friend!", Toast.LENGTH_LONG);
		}
		
	}
}


