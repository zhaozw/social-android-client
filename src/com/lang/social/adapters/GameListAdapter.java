package com.lang.social.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.competition.SocialGameListItem;
import com.lang.social.logic.User;


public class GameListAdapter extends ArrayAdapter<SocialGameListItem> {
	
	private Context context;
	private int layoutResourceId;
	private ArrayList<SocialGameListItem> gamesList;
	
	public GameListAdapter(Context context, int layoutResourceId, ArrayList<SocialGameListItem> gamesList){
		super(context, R.layout.game_list_row, gamesList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.gamesList = gamesList;
	}

	@Override
	public SocialGameListItem getItem(int position) {
		return gamesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return gamesList.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		
		if(itemView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			itemView = inflater.inflate(layoutResourceId, parent, false);
		}
		
		SocialGameListItem gameItem = gamesList.get(position);
		User player1 = gameItem.getPlayer1();
		

		if(player1.getProfileID() != null){
		    ProfilePictureView ppv = (ProfilePictureView) itemView.findViewById(R.id.ivListItemProfileImage);
		    ppv.setProfileId(player1.getProfileID());
		}

		String fName = player1.getFirstName();
		String lName = player1.getLastName();
	
		TextView tvPlayerName = (TextView) itemView.findViewById(R.id.tvListItemName);
		tvPlayerName.setText(fName + " " + lName);

		return itemView;
	}
}