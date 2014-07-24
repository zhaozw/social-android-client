package com.lang.social.profile;

import java.util.ArrayList;

import com.lang.social.R;
import com.lang.social.logic.UserController;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusAdapter extends ArrayAdapter<StatusItem> {
	
	private Context context;
	private int layoutResourceId;
	private ArrayList<StatusItem> statusItems;
	
	public StatusAdapter(Context context, int layoutResourceId, ArrayList<StatusItem> statusItems){
		super(context, layoutResourceId, statusItems);
        this.layoutResourceId = layoutResourceId;
        this.statusItems = statusItems;
        this.context = context;    
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View itemView = convertView;
		
		if(itemView == null){
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			itemView = inflater.inflate(layoutResourceId, parent, false);
		}
		
		StatusItem status = statusItems.get(position);
		int iconRes = status.getIconResLanguage();
		String language = status.getLanguage();
		String level = status.getLevel();
		String points = status.getPoints();

		Typeface tf = Typeface.createFromAsset(((Activity) context).getAssets(),"fonts/Roboto-LightItalic.ttf");
		
		((TextView)itemView.findViewById(R.id.ivStatusItemPoints)).setText(String.valueOf("Points: " + points));
		((TextView)itemView.findViewById(R.id.ivProfileLanguageName)).setText(language);
		((TextView)itemView.findViewById(R.id.tvStatusItemLevel)).setText("Level: " + level);
		
		((TextView)itemView.findViewById(R.id.ivStatusItemPoints)).setTypeface(tf);
		((TextView)itemView.findViewById(R.id.ivProfileLanguageName)).setTypeface(tf);
		((TextView)itemView.findViewById(R.id.tvStatusItemLevel)).setTypeface(tf);
		
		((ImageView)itemView.findViewById(R.id.ivStatusLanguageFlag))
			.setImageDrawable(context.getResources().getDrawable(iconRes));

		return itemView;
	} 
}
