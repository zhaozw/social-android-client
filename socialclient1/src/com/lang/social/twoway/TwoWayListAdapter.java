package com.lang.social.twoway;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.lang.social.R;

public class TwoWayListAdapter extends ArrayAdapter<TwoWayPhotoItem> {

	private final Context mContext;
	private int layoutResourceId;
	private ArrayList<TwoWayPhotoItem> photosList;
	
	private int mLastPosition;
	
	public TwoWayListAdapter(Context context, int layoutResourceId, ArrayList<TwoWayPhotoItem> photosList){
		super(context, R.layout.game_list_row, photosList);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.photosList = photosList;
	}

	@Override
	public TwoWayPhotoItem getItem(int position) {
		return photosList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return photosList.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;

		View itemView = convertView;
		
		if(itemView == null)
		{
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			itemView = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			
			//holder.title = (TextView) itemView.findViewById(R.id.twoWayImagetitle);
			holder.image = (ImageView) itemView.findViewById(R.id.twoWayImage);
			
			itemView.setTag(holder);
		} 
		else
		{
		    holder = (ViewHolder) itemView.getTag();
		}

        //holder.title.setText(photosList.get(position).getTitle());
        
		Bitmap bitmap = photosList.get(position).getImageRes();
		holder.image.setImageBitmap(bitmap);
        
        float initialTranslation = (mLastPosition <= position ? 500f : -500f);
        itemView.setTranslationX(initialTranslation);
        itemView.animate()
        .setInterpolator(new DecelerateInterpolator(1.0f))
        .translationX(0f)
        .setDuration(300l)
        .setListener(null);
        
        mLastPosition = position;
        
//        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
//        animation.setDuration(300);
//        itemView.startAnimation(animation);

		return itemView;
	}

	class ViewHolder {
	    //public TextView title;
	    public ImageView image;
	}
}
