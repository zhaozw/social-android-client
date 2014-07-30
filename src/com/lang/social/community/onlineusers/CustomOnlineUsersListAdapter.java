package com.lang.social.community.onlineusers;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
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
import com.lang.social.community.friends.Friend;
import com.lang.social.community.friends.FriendItem;
import com.lang.social.controllers.ServerController;
import com.lang.social.logic.User;
import com.lang.social.utils.JSONUtils;

public class CustomOnlineUsersListAdapter extends ArrayAdapter<OnlineUserItem> {
	private Context context;
	private int layoutResourceId;
	private ArrayList<OnlineUserItem> onlineUsersList;
	
	public CustomOnlineUsersListAdapter(Context context, int layoutResourceId, ArrayList<OnlineUserItem> friendsList){
		super(context, R.layout.online_user_list_row, friendsList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.onlineUsersList = friendsList;
	}
	
	@Override
	public OnlineUserItem getItem(int position) {
		return onlineUsersList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return onlineUsersList.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		
		if(itemView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			itemView = inflater.inflate(layoutResourceId, parent, false);
		}
		
		final View view = itemView; //Inner Classes Use Only
		
		OnlineUserItem onlineUserItem = onlineUsersList.get(position);
		final int pos = position;
		final OnlineUser onlineUser = onlineUserItem.getUser();
		

		if(onlineUser.getProfileID() != null){
		    ProfilePictureView ppv = (ProfilePictureView) itemView.findViewById(R.id.ivListItemOnlineUserProfileImage);
		    ppv.setProfileId(onlineUser.getProfileID());
		}
		
		final String fName = onlineUser.getFirstName();
		final String lName = onlineUser.getLastName();
		TextView tvPlayerName = (TextView) itemView.findViewById(R.id.tvListItemOnlineUserName);
		tvPlayerName.setText(fName + " " + lName);
		
		ImageView ivFlagLearning = (ImageView) itemView.findViewById(R.id.ivOnlineUserFlag);
		ivFlagLearning.setImageResource(onlineUser.getFlagImageRes());
		
		TextView tvPlayerLevel = (TextView) itemView.findViewById(R.id.tvListItemOnlineUserLevel);
		tvPlayerLevel.setText(onlineUser.getCurrentLanguageLevel());
		
		ImageView ivSendMessage = (ImageView) itemView.findViewById(R.id.ivSendMessage);
		ivSendMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopUpMessageDialog(view, onlineUser);
			}
		});
		
		
		return itemView;
	}
	
	private void showPopUpMessageDialog(View anchorView, final User onlineUser) {
		final View popupView = ((Activity) context).getLayoutInflater().inflate(R.layout.send_new_message_view, null);

	    final PopupWindow popupWindow = new PopupWindow(popupView, 
	                           LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	    // Example: If you have a TextView inside `popup_layout.xml`  
	    
	    if(onlineUser.getProfileID() != null){
		    ProfilePictureView ppv = (ProfilePictureView) popupView.findViewById(R.id.ivReciverProfileImage);
		    ppv.setProfileId(onlineUser.getProfileID());
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
				handleMessageSent(popupView, onlineUser);
				popupWindow.dismiss();
			}
		});
	    
	    TextView ttReciverName = (TextView) popupView.findViewById(R.id.tvReciverName);
	    ttReciverName.setText(onlineUser.getFullName());

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
	
	private void handleMessageSent(View view, User onlineUser)
	{
		JSONObject msgDetails = new JSONObject();
		String isFacebookUser;
		String friendProfileID = null;
		String friendUserName = null;
		//Handling the receiver details
		if(onlineUser.isFacebookUser() == true) {
			friendProfileID = onlineUser.getProfileID();
			isFacebookUser = "true";
		}
		else {
			friendUserName = onlineUser.getUserName();
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

}
