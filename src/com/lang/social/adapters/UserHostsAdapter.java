package com.lang.social.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.competition.SocialGameListItem;
import com.lang.social.logic.User;

public class UserHostsAdapter extends ArrayAdapter<SocialGameListItem> {
	
	private Context context;
	private int layoutResourceId;
	private ArrayList<SocialGameListItem> competitionGameItems;
	
	public UserHostsAdapter(Context context, int layoutResourceId, ArrayList<SocialGameListItem> competitionGameItems){
		super(context, R.layout.game_list_row, competitionGameItems);
        this.layoutResourceId = layoutResourceId;
        this.competitionGameItems = competitionGameItems;
        this.context = context;    
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		
		if(itemView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			itemView = inflater.inflate(layoutResourceId, parent, false);
		}
		
		SocialGameListItem host = competitionGameItems.get(position);
		User player1 = host.getPlayer1();
		
		if(player1.isFacebookUser()) {
		    ProfilePictureView ppv = (ProfilePictureView) itemView.findViewById(R.id.ivListItemProfileImage);
		    ppv.setProfileId(player1.getProfileID());
		}

		String fName = player1.getFirstName();
		String lName = player1.getLastName();
	
		TextView tvPlayerName = (TextView) itemView.findViewById(R.id.tvListItemName);
		tvPlayerName.setText(fName + " " + lName);
		
		TextView tvPlayerLevel = (TextView) itemView.findViewById(R.id.tvListItemLevel);
		tvPlayerLevel.setText(player1.getCurrentLanguageLevel());
		
		Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-LightItalic.ttf");
		Typeface tf1 = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-ThinItalic.ttf");
		Typeface tf2 = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Light.ttf");
		Typeface tf3 = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Regular.ttf");
		Typeface tf4 = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Thin.ttf");
		TextView tvJoin = (TextView) itemView.findViewById(R.id.tvJoin);
		tvJoin.setTypeface(tf);
		
		ImageView tvFlag = (ImageView) itemView.findViewById(R.id.ivFlag);
		tvFlag.setImageResource(player1.getFlagImageRes());

		
		return itemView;
	}
}
