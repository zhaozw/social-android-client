package com.lang.social.community.friends;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.logic.User;
import com.lang.social.utils.JSONUtils;

public class CustomFriendsListAdapter extends ArrayAdapter<FriendItem>  {
	private Context context;
	private int layoutResourceId;
	private ArrayList<FriendItem> friendsList;
	
	
	private int removedFriendPos = -1;
	
	//private AlertDialogFragment UnfriendButtonDialog;
	
	public CustomFriendsListAdapter(Context context, int layoutResourceId, ArrayList<FriendItem> friendsList){
		super(context, R.layout.friend_list_row, friendsList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.friendsList = friendsList;
	}
	
	@Override
	public FriendItem getItem(int position) {
		return friendsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return friendsList.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		
		if(itemView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			itemView = inflater.inflate(layoutResourceId, parent, false);
		}
		
		final View view = itemView;	//just in order to pass it as final parameter
		
		FriendItem friendItem = friendsList.get(position);
		final int pos = position;
		final Friend friend = friendItem.getFriend();
		

		if(friend.getProfileID() != null){
		    ProfilePictureView ppv = (ProfilePictureView) itemView.findViewById(R.id.ivListItemFriendProfileImage);
		    ppv.setProfileId(friend.getProfileID());
		}

		final boolean isOnline  = friend.isOnline();
		TextView tvConnectedStatus = (TextView) itemView.findViewById(R.id.tvConnectedStatus);
		ImageView ivConnectedStatus = (ImageView) itemView.findViewById(R.id.ivConnectedStatus);
		
		if(isOnline){
			tvConnectedStatus.setText("Online");
			ivConnectedStatus.setImageResource(R.drawable.online);
		} else {
			tvConnectedStatus.setText("Offline");
			ivConnectedStatus.setImageResource(R.drawable.offlineicon);
		}
		
		final String fName = friend.getFirstName();
		final String lName = friend.getLastName();
		TextView tvPlayerName = (TextView) itemView.findViewById(R.id.tvListItemFriendName);
		tvPlayerName.setText(fName + " " + lName);
		
		TextView tvPlayerLevel = (TextView) itemView.findViewById(R.id.tvListItemFriendLevel);
		tvPlayerLevel.setText(friend.getCurrentLanguageLevel());
		
		
		ImageView ivFlagLearning = (ImageView) itemView.findViewById(R.id.ivFriendFlag);
		ivFlagLearning.setImageResource(friend.getFlagImageRes());
		
		
		ImageView ivUnfriend = (ImageView) itemView.findViewById(R.id.ivDeleteFriend);
		//Setting Delete Friend OnclickListener
		ivUnfriend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlertDialog(friend, pos);
			}
		});
		
		//Setting 'Send Message' On ClickListener
		ImageView ivSendNewMessage = (ImageView) itemView.findViewById(R.id.ivSendMessage);
		ivSendNewMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopUpMessageDialog(view, friend);
				
			}
		});
		
		return itemView;
	}
	
	
	private void showPopUpMessageDialog(View anchorView, final User friend) {
		final View popupView = ((Activity) context).getLayoutInflater().inflate(R.layout.send_new_message_view, null);

	    final PopupWindow popupWindow = new PopupWindow(popupView, 
	                           LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	    // Example: If you have a TextView inside `popup_layout.xml`  
	    
	    if(friend.getProfileID() != null){
		    ProfilePictureView ppv = (ProfilePictureView) popupView.findViewById(R.id.ivReciverProfileImage);
		    ppv.setProfileId(friend.getProfileID());
		}
	    
	    Button btCancel = (Button) popupView.findViewById(R.id.btCancelMessage);
	    btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	    
	    Button btSendMessage = (Button) popupView.findViewById(R.id.btSendMessage);
	    btSendMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleMessageSent(popupView, friend);
				popupWindow.dismiss();
			}
		});
	    
	    TextView ttReciverName = (TextView) popupView.findViewById(R.id.tvReciverName);
	    ttReciverName.setText(friend.getFullName());

	    // If the PopupWindow should be focusable
	    popupWindow.setFocusable(true);

	    // If you need the PopupWindow to dismiss when when touched outside 
	    popupWindow.setBackgroundDrawable(new ColorDrawable());

	    int location[] = new int[2];

	    // Get the View's(the one that was clicked in the Fragment) location
	    anchorView.getLocationOnScreen(location);

	    // Using location, the PopupWindow will be displayed right under anchorView
	    popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
	}
	
	private void handleMessageSent(View view, User friend)
	{
		JSONObject msgDetails = new JSONObject();
		String isFacebookUser;
		String friendProfileID = null;
		String friendUserName = null;
		//Handling the receiver details
		if(friend.isFacebookUser() == true) {
			friendProfileID = friend.getProfileID();
			isFacebookUser = "true";
		}
		else {
			friendUserName = friend.getUserName();
			isFacebookUser = "false";
		}
		EditText subjectText = (EditText) view.findViewById(R.id.etSubjectText);
		String subject = subjectText.getText().toString();
		
		EditText contentText = (EditText) view.findViewById(R.id.etContentText);
		String content = contentText.getText().toString();
		
		JSONUtils.setStringValue(msgDetails, "facebookUser", isFacebookUser);
		JSONUtils.setStringValue(msgDetails, "profileid", friendProfileID);
		JSONUtils.setStringValue(msgDetails, "username", friendUserName);
		JSONUtils.setStringValue(msgDetails, "subject", subject);
		JSONUtils.setStringValue(msgDetails, "content", content);
		
		ServerController.sendJSONMessage("sendNewMessageRequest", msgDetails);
		
		
	}
	public void showAlertDialog(final User friend, final int pos) {
		new AlertDialog.Builder(context)
	    .setTitle("Unfriend Request")
	    .setMessage("Unfriend" + " " + friend.getFullName() + "?")
	    .setPositiveButton("Unfriend!", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
				handleUnfriendEvent(friend, pos);
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
	
	public void handleUnfriendEvent(User friend, int pos) {
		JSONObject friendToDelete = new JSONObject();
		if(friend.isFacebookUser() == true) {
			JSONUtils.setStringValue(friendToDelete, "profileid", friend.getProfileID());
			JSONUtils.setStringValue(friendToDelete, "facebookUser", "true");
			ServerController.sendJSONMessage("unfriendRequest", friendToDelete);
		}
		else {	//regular user
			JSONUtils.setStringValue(friendToDelete, "username", friend.getUserName());
			JSONUtils.setStringValue(friendToDelete, "facebookUser", "false");
			ServerController.sendJSONMessage("unfriendRequest", friendToDelete);
		}
		removedFriendPos = pos;
	}

	public int getFriendToRemovePos() {
		if (removedFriendPos != -1) {
			int temp = removedFriendPos;
			removedFriendPos = -1;
			return temp;
		}
		else {	//returns -1
			return removedFriendPos;
		}
	}

	
}
