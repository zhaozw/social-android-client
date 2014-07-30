package com.lang.social.community.friends;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.logic.User;
import com.lang.social.utils.JSONUtils;

public class CustomFriendsRequestsListAdapter extends ArrayAdapter<FriendRequestItem> {
	private Context context;
	private int layoutResourceId;
	private ArrayList<FriendRequestItem> friendsRequestsList;
	
	
	private int removedFriendRequestPos = -1;
	
//	private AlertDialogFragment UnfriendButtonDialog;
	
	public CustomFriendsRequestsListAdapter(Context context, int layoutResourceId, ArrayList<FriendRequestItem> friendsRequestsList){
		super(context, R.layout.friend_request_row, friendsRequestsList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.friendsRequestsList = friendsRequestsList;
	}
	
	@Override
	public FriendRequestItem getItem(int position) {
		return friendsRequestsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return friendsRequestsList.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		
		if(itemView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			itemView = inflater.inflate(layoutResourceId, parent, false);
		}
		
		final View view = itemView;	//just in order to pass it as final parameter
		
		FriendRequestItem friendRequestItem = friendsRequestsList.get(position);
		final int pos = position;
		final User friendRequester = friendRequestItem.getRequestingFriendshipUserd();
		

		if(friendRequester.getProfileID() != null){
		    ProfilePictureView ppv = (ProfilePictureView) itemView.findViewById(R.id.ivListItemFriendRequestProfileImage);
		    ppv.setProfileId(friendRequester.getProfileID());
		}

		
		final String fName = friendRequester.getFirstName();
		final String lName = friendRequester.getLastName();
		TextView tvPlayerName = (TextView) itemView.findViewById(R.id.tvListItemFriendRequestName);
		tvPlayerName.setText(fName + " " + lName);
		
		TextView tvPlayerLevel = (TextView) itemView.findViewById(R.id.tvListItemFriendRequestLevel);
		tvPlayerLevel.setText(friendRequester.getCurrentLanguageLevel());
		
		ImageView ivFlagLearning = (ImageView) itemView.findViewById(R.id.ivFriendRequestFlag);
		ivFlagLearning.setImageResource(friendRequester.getFlagImageRes());
		
		
		ImageView ivIgnoreFriendRequest = (ImageView) itemView.findViewById(R.id.ivIgnoreFriendRequest);
		//Setting Ignore Friend Request OnclickListener
		ivIgnoreFriendRequest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlertDialog(friendRequester, pos);
			}
		});
		
		//Setting Accept Friend Request On ClickListener
		ImageView ivAcceptFriendRequest = (ImageView) itemView.findViewById(R.id.ivAcceptFriendRequest);
		ivAcceptFriendRequest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleAcceptFriendRequest(friendRequester, pos);
			}
		});
		
		return itemView;
	}
	
	public void showAlertDialog(final User friendRequester, final int pos) {
		new AlertDialog.Builder(context)
	    .setTitle("Ignore Friend Request")
	    .setMessage("Ignore Request From: " + " " + friendRequester.getFullName() + "?")
	    .setPositiveButton("Ignore!", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
				handleIgnoreFriendRequestEvent(friendRequester, pos);
	        }
	     })
	    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
	}
	
	public void handleIgnoreFriendRequestEvent(User friendRequester, int pos) {
		JSONObject friendRequestToIgnore = new JSONObject();
		if(friendRequester.isFacebookUser() == true) {
			JSONUtils.setStringValue(friendRequestToIgnore, "profileid", friendRequester.getProfileID());
			JSONUtils.setStringValue(friendRequestToIgnore, "facebookUser", "true");
			ServerController.sendJSONMessage("ignoreFriendRequest", friendRequestToIgnore);
		}
		else {	//regular user
			JSONUtils.setStringValue(friendRequestToIgnore, "username", friendRequester.getUserName());
			JSONUtils.setStringValue(friendRequestToIgnore, "facebookUser", "false");
			ServerController.sendJSONMessage("ignoreFriendRequest", friendRequestToIgnore);
		}
		removedFriendRequestPos = pos;
	}
	
	private void handleAcceptFriendRequest(User friendRequester, int pos) {
		JSONObject friendRequestToAccept = new JSONObject();
		if(friendRequester.isFacebookUser() == true) {
			JSONUtils.setStringValue(friendRequestToAccept, "profileid", friendRequester.getProfileID());
			JSONUtils.setStringValue(friendRequestToAccept, "facebookUser", "true");
			ServerController.sendJSONMessage("acceptFriendRequest", friendRequestToAccept);
		}
		else {	//regular user
			JSONUtils.setStringValue(friendRequestToAccept, "username", friendRequester.getUserName());
			JSONUtils.setStringValue(friendRequestToAccept, "facebookUser", "false");
			ServerController.sendJSONMessage("acceptFriendRequest", friendRequestToAccept);
		}
		removedFriendRequestPos = pos;
	}
	
	
//	public void showAlertDialog(final User friend, final int pos) {
//		new AlertDialog.Builder(context)
//	    .setTitle("Unfriend Request")
//	    .setMessage("Unfriend" + " " + friend.getFullName() + "?")
//	    .setPositiveButton("Unfriend!", new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//				handleUnfriendEvent(friend, pos);
//	        }
//	     })
//	    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//	        public void onClick(DialogInterface dialog, int which) { 
//	            // do nothing
//	        }
//	     })
//	    .setIcon(android.R.drawable.ic_dialog_alert)
//	    .show();
//	}
	
	public int getFriendRequestToRemovePos() {
		if (removedFriendRequestPos != -1) {
			int temp = removedFriendRequestPos;
			removedFriendRequestPos = -1;
			return temp;
		}
		else {	//returns -1
			return removedFriendRequestPos;
		}
	}
	
}
