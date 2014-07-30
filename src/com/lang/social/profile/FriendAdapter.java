package com.lang.social.profile;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.logic.User;

public class FriendAdapter extends ArrayAdapter<User> {
	
	private Context context;
	private int layoutResourceId;
	private ArrayList<User> friends;
	
	public FriendAdapter(Context context, int layoutResourceId, ArrayList<User> friends){
		super(context, layoutResourceId, friends);
        this.layoutResourceId = layoutResourceId;
        this.friends = friends;
        this.context = context;    
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		
		if(itemView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			itemView = inflater.inflate(layoutResourceId, parent, false);
		}
		
		User friend = friends.get(position);
		if(friend.isFacebookUser()){
			String profileId = friend.getProfileID();
			ProfilePictureView picSender = (ProfilePictureView) itemView.findViewById(R.id.ivProfileFriendItem);
			picSender.setProfileId(profileId);
		}
		
		String senderName = friend.getFullName();
		
		TextView tvPlayerName = (TextView) itemView.findViewById(R.id.tvProfileFriendItem);
		tvPlayerName.setText(senderName);
		Typeface tf = Typeface.createFromAsset(((Activity) context).getAssets(),"fonts/Roboto-LightItalic.ttf");
		tvPlayerName.setTypeface(tf);
		

		return itemView;
	} 
}
