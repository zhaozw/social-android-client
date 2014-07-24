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

public class MessagesAdapter extends ArrayAdapter<Message> {
	
	private Context context;
	private int layoutResourceId;
	private ArrayList<Message> messages;
	
	public MessagesAdapter(Context context, int layoutResourceId, ArrayList<Message> messages){
		super(context, layoutResourceId, messages);
        this.layoutResourceId = layoutResourceId;
        this.messages = messages;
        this.context = context;    
	}
	

	public void remove(int position) {
		messages.remove(position);
		notifyDataSetChanged();
	} 
	
    public void insert(int position, Message message) {
    	messages.add(position, message);
        notifyDataSetChanged();
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		
		if(itemView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			itemView = inflater.inflate(layoutResourceId, parent, false);
		}
		
		Message message = messages.get(position);
		String profileId = message.getProfileId();
		String senderName = message.getSenderFullName();
		String content = message.getContent();
		String date = message.getDate();
		
		TextView tvPlayerName = (TextView) itemView.findViewById(R.id.tvProfileMessageItemSenderFullName);
		tvPlayerName.setText(senderName);
		Typeface tf = Typeface.createFromAsset(((Activity) context).getAssets(),"fonts/Roboto-LightItalic.ttf");
		tvPlayerName.setTypeface(tf);
		
		((TextView) itemView.findViewById(R.id.tvProfileMsgItemDateTitle)).setTypeface(tf);
		
		TextView tvContent = (TextView) itemView.findViewById(R.id.tvProfileMessageItemContent);
		tvContent.setTypeface(tf);
		String shortTextContent = content;
		if(content.length() > 40) {
			shortTextContent = shortTextContent.substring(0, 40) + "...";
		}
		tvContent.setText(shortTextContent);
		
		TextView tvDate = (TextView) itemView.findViewById(R.id.tvProfileMessageItemDate);
		tvDate.setTypeface(tf);
		tvDate.setText(date);
		
		ProfilePictureView picSender = (ProfilePictureView) itemView.findViewById(R.id.ivProfileMessageItemSender);
		picSender.setProfileId(profileId);

		return itemView;
	}

}
