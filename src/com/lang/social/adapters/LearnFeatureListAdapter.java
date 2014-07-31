package com.lang.social.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lang.social.R;
import com.lang.social.items.LearnFeatureMenuItem;

public class LearnFeatureListAdapter extends ArrayAdapter<LearnFeatureMenuItem> {

    Context context; 
    int layoutResourceId;    
    List<LearnFeatureMenuItem> selfLearnMenuItems;
    
    public LearnFeatureListAdapter(Context context, int layoutResourceId, List<LearnFeatureMenuItem> selfLearnMenuItems) {
        super(context, layoutResourceId, selfLearnMenuItems);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.selfLearnMenuItems = selfLearnMenuItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SelfLearnItemHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
     
            holder = new SelfLearnItemHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.ivQuiz);
            holder.featureTitle = (TextView)row.findViewById(R.id.tvFeatureTitle);
            holder.featureDescription = (TextView)row.findViewById(R.id.tvFeatureDescription);
            row.setTag(holder);
        }
        else
        {
            holder = (SelfLearnItemHolder)row.getTag();
        }
        
        LearnFeatureMenuItem selfLearnItem = selfLearnMenuItems.get(position);
        holder.imgIcon.setImageResource(selfLearnItem.icon);
        holder.featureTitle.setText(selfLearnItem.title);
        holder.featureDescription.setText(selfLearnItem.description);
        
        return row;
    }
    
    static class SelfLearnItemHolder
    {
        ImageView imgIcon;
        TextView featureTitle;
        TextView featureDescription;
    }
}
