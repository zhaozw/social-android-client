package com.lang.social.teachstudy.teachers;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.logic.User;
import com.lang.social.teachstudy.StudentTeacherGameItem;

public class TeachersHostsAdapter extends ArrayAdapter<StudentTeacherGameItem> {
	private Context context;
	private int layoutResourceId;
	private ArrayList<StudentTeacherGameItem> studentTeacherGameItems;
	
	public TeachersHostsAdapter(Context context, int layoutResourceId, ArrayList<StudentTeacherGameItem> studentTeacherGameItems){
		super(context, R.layout.student_teacher_game_list_row, studentTeacherGameItems);
        this.layoutResourceId = layoutResourceId;
        this.studentTeacherGameItems = studentTeacherGameItems;
        this.context = context;    
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		
		if(itemView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			itemView = inflater.inflate(layoutResourceId, parent, false);
		}
		
		StudentTeacherGameItem host = studentTeacherGameItems.get(position);
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
		
		ImageView tvFlag = (ImageView) itemView.findViewById(R.id.ivFlag);
		tvFlag.setImageResource(player1.getFlagImageRes());

		
		return itemView;
	}
}
