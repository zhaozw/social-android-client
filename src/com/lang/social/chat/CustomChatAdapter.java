package com.lang.social.chat;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lang.social.R;

public class CustomChatAdapter extends ArrayAdapter<ChatLineItem> {

	  Context context;
	  int layoutResID;
	  List<ChatLineItem> mChatLineItems;
	
	public CustomChatAdapter(Context context, int layoutResourceID, List<ChatLineItem> chatLineItems) {
        super(context, layoutResourceID, chatLineItems);
        this.context = context;
        this.mChatLineItems = chatLineItems;
        this.layoutResID = layoutResourceID;
	}
	
	 @Override
     public View getView(int position, View convertView, ViewGroup parent) {

			View row = convertView;
			DrawerItemHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(layoutResID, parent, false);

				holder = new DrawerItemHolder();
				holder.tvMessage = (TextView) row.findViewById(R.id.tvitemChatMessage);
				holder.tvSender = (TextView) row.findViewById(R.id.tvitemChatName);
				row.setTag(holder);
			} else {
				holder = (DrawerItemHolder) row.getTag();
			}

			ChatLineItem item = mChatLineItems.get(position);
			holder.tvSender.setText(item.getSender() + " : ");
			holder.tvMessage.setText(item.getMsg());
			holder.tvSender.setTextColor(((Activity)context).getResources().getColor(item.getResColor()));
			return row;

		}

     private static class DrawerItemHolder {
		TextView tvSender;
		TextView tvMessage;
     }
}
