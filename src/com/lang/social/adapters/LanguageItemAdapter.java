package com.lang.social.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lang.social.R;
import com.lang.social.items.LanguageItem;

public class LanguageItemAdapter extends ArrayAdapter<LanguageItem>{

    Context context; 
    int layoutResourceId;    
    List<LanguageItem> languageItems;
    
    public LanguageItemAdapter(Context context, int layoutResourceId, List<LanguageItem> languageItems) {
        super(context, layoutResourceId, languageItems);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.languageItems = languageItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LanguageItemHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new LanguageItemHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            
            row.setTag(holder);
        }
        else
        {
            holder = (LanguageItemHolder)row.getTag();
        }
        
        LanguageItem languageItem = languageItems.get(position);
        holder.txtTitle.setText(languageItem.title);
        holder.imgIcon.setImageResource(languageItem.icon);
        
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-ThinItalic.ttf");
        holder.txtTitle.setTypeface(tf);
        
        return row;
    }
    
    static class LanguageItemHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
