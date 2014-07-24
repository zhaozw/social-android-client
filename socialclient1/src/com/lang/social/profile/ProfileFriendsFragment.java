package com.lang.social.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.lang.social.R;
import com.lang.social.community.friends.FriendsServerListener;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.User;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.quickactions.ActionItem;
import com.lang.social.quickactions.QuickAction;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;

public class ProfileFriendsFragment extends Fragment implements FriendsServerListener {

	private ListView listViewFriends;
	private FriendAdapter friendAdapter;
	private ArrayList<User> friends = new ArrayList<User>();
	private FriendFragmentListener friendFragmentListener; 
	
	//action id for quick actions
	private static final int ID_PROFILE     = 1;
	private static final int ID_MESSAGE     = 2;
	private static final int ID_UNFRIEND    = 3;
	
	private QuickAction quickAction;
	
	private User friendPressed;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	     // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment_layout, container, false);
        
        setUpQuickAction();
        
        setFonts(view);

		Bundle bundle = getArguments();
		String friendsString = bundle.getString(ProfileConstants.UserFriends);
		JSONArray friendsJsonArray = null;
		try{
			friendsJsonArray = new JSONArray(friendsString);
		} catch(JSONException ex) {
			ex.printStackTrace();
		}
		
        
        //---------------------------------------------------------------------------------------------------
        friendAdapter = new FriendAdapter(getActivity(), R.layout.profile_friend_item, friends);
        friendAdapter.clear();
        listViewFriends = (ListView) view.findViewById(R.id.profileFragmentList);
        populateFriends(friendsJsonArray);
        listViewFriends.setAdapter(friendAdapter);
        //---------------------------------------------------------------------------------------------------
      
        listViewFriends.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int which, long arg3) {
				friendPressed = friends.get(which);
				if(friendFragmentListener != null){
					quickAction.show(view);
				}
			}
		}); 
        
		return view;
	}

	private void setUpQuickAction() {
		
		ActionItem profileItem 	= new ActionItem(ID_PROFILE, "Profile", getResources().getDrawable(R.drawable.iconfriendsmaller));
		ActionItem messageItem 	= new ActionItem(ID_MESSAGE, "Message", getResources().getDrawable(R.drawable.iconmessagessmallerorange));
        ActionItem unfriendItem = new ActionItem(ID_UNFRIEND, "Unfriend", getResources().getDrawable(R.drawable.iconunfriendsmaller));
        
        //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
        profileItem.setSticky(false);
        messageItem.setSticky(false);
        unfriendItem.setSticky(false);
		
		//create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout 
        //orientation
		quickAction = new QuickAction(getActivity(), QuickAction.HORIZONTAL);
		
		//add action items into QuickAction
        quickAction.addActionItem(profileItem);
		quickAction.addActionItem(messageItem);
        quickAction.addActionItem(unfriendItem);

        
        //Set listener for action item clicked
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {				
				ActionItem actionItem = quickAction.getActionItem(pos);
				//here we can filter which action item was clicked with pos or actionId parameter
				if (actionId == ID_PROFILE) {
					if(friendPressed != null && friendFragmentListener != null){
						friendFragmentListener.OnFriendProfileViewRequestClick(friendPressed);
					}
					friendPressed = null;
				} else if (actionId == ID_MESSAGE) {
					if(friendFragmentListener != null){
						friendFragmentListener.OnMessagesViewRequestClick();
					}
				} else if(actionId == ID_UNFRIEND) {
					if(friendFragmentListener != null){
						UnfriendRequest(friendPressed);
					}
				}
			}
		});
		
			//set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
					//by clicking the area outside the dialog.
			quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {			
				@Override
				public void onDismiss() {
					
				}
			});
			
	}
	

	public void UnfriendRequest(User friend) {
		JSONObject jsonFriendDetails = new JSONObject();
		if(friend.isFacebookUser()){
			JSONUtils.setStringValue(jsonFriendDetails, "facebookUser", "true");
			JSONUtils.setStringValue(jsonFriendDetails, "profileid", friend.getProfileID());
		} else{
			JSONUtils.setStringValue(jsonFriendDetails, "facebookUser", "false");
			JSONUtils.setStringValue(jsonFriendDetails, "username", friend.getUserName());
		}
		
		IOCallBackHandler.getInstance().setFriendsListener(this);
		ServerController.sendJSONMessage("unfriendRequest", jsonFriendDetails);
	}

	@Override
	public void onUnfriedRespone(JSONObject jsonResponse) {
		List<String> jsonKeys = Arrays.asList("result");
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()) {
			MyToaster.showToast(getActivity(), "you are not friend anymore!", Toast.LENGTH_SHORT);
		}
	}

	private void populateFriends(JSONArray friends2) {
		if(friends2 != null) {
			for(int i=0; i < friends2.length(); i++){
				User userFriend = new User(JSONUtils.getJSONObject(friends2, i));
				friends.add(userFriend);
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		friendFragmentListener = (ProfileActivity)activity;
	}
	
	private void setFonts(View view) {
        TextView textHeader = (TextView) view.findViewById(R.id.messageHeader);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-LightItalic.ttf");
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-ThinItalic.ttf");
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf");
		Typeface tf3 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Regular.ttf");
		Typeface tf4 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Thin.ttf");
        textHeader.setTypeface(tf);
        textHeader.setText("Your Friends");
	}

	@Override
	public void onFriendsListRespone(JSONObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFriendsRequestListRespone(JSONObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIgnoreFriendRequest(JSONObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAcceptFriendRequestRespone(JSONObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageSentRespone(JSONObject obj) {
		// TODO Auto-generated method stub
		
	}
}
