package com.lang.social.adapters;

import java.util.List;

import org.xml.sax.DTDHandler;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.facebook.FacebookController;
import com.lang.social.items.DrawerItem;
import com.lang.social.items.SpinnerItem;
import com.lang.social.logic.UserController;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {
	
		  Context context;
		  int layoutResID;
	      List<DrawerItem> drawerItemList;

	      public CustomDrawerAdapter(Context context, int layoutResourceID, List<DrawerItem> listItems) {
	            super(context, layoutResourceID, listItems);
	            this.context = context;
	            this.drawerItemList = listItems;
	            this.layoutResID = layoutResourceID;
	      }
	 
	      @Override
	      public View getView(int position, View convertView, ViewGroup parent) {
	 
	            DrawerItemHolder drawerHolder;
	            View view = convertView;
	 
	            if (view == null) {
	                  LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	                  drawerHolder = new DrawerItemHolder();
	                  view = inflater.inflate(layoutResID, parent, false);
	                  
	                  drawerHolder.ItemName = (TextView) view.findViewById(R.id.drawer_itemName);
	                  drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_itemIcon);
	                  drawerHolder.profilePic = (ImageView) view.findViewById(R.id.ivProfilePicDrawer);
	                  drawerHolder.profileName = (TextView) view.findViewById(R.id.drawer_profileName);
	                  drawerHolder.ppv = (ProfilePictureView) view.findViewById(R.id.ivDrawerProfileFacebookImage);
	                  drawerHolder.languageFlag = (ImageView) view.findViewById(R.id.drawer_itemLanguageIcon);
	                  drawerHolder.tvLanguage =  (TextView) view.findViewById(R.id.drawer_itemLanguageTitle);
	                  
	                  drawerHolder.itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
	                  drawerHolder.profileImageLayout =  (LinearLayout) view.findViewById(R.id.profileItemDrawerLayout);
	                  drawerHolder.languageItemLayout =  (LinearLayout) view.findViewById(R.id.itemLanguageLayout);
	                  

	                  Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/Jura-DemiBold.ttf");
	                  drawerHolder.ItemName.setTypeface(tf);
	                  drawerHolder.profileName.setTypeface(tf);

	                  view.setTag(drawerHolder);
	                  
	            } else {
	                  drawerHolder = (DrawerItemHolder) view.getTag();
	            }
	 
	            DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

	            
	             if(dItem.isProfileImage()) {
		            drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
		            drawerHolder.languageItemLayout.setVisibility(LinearLayout.INVISIBLE);
	                drawerHolder.profileImageLayout.setVisibility(LinearLayout.VISIBLE);

	                drawerHolder.profileName.setText(UserController.getUser().getFirstName() 
	                		+ " " + UserController.getUser().getLastName());
	                
	                if(dItem.isFacebookImage()){
	                	drawerHolder.profilePic.setVisibility(View.GONE);
	                	drawerHolder.ppv.setProfileId(dItem.getProfileID());
	                }
	                else{
	                	drawerHolder.profilePic.setImageBitmap(dItem.getUserImageBitmap());
	                	drawerHolder.ppv.setVisibility(View.GONE);
	                }
	               
	            }
	            
	             else if(dItem.isLanguageItem()) {
	                  drawerHolder.profileImageLayout.setVisibility(LinearLayout.INVISIBLE);
	                  drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
	                  drawerHolder.languageItemLayout.setVisibility(LinearLayout.VISIBLE);
	                  
	                  Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/Jura-DemiBold.ttf");
	                  drawerHolder.tvLanguage.setTypeface(tf);
	                  
	                  drawerHolder.tvLanguage.setText(dItem.getLanguageItem().title);
	                  drawerHolder.languageFlag.setImageDrawable(view.getResources().getDrawable(dItem.getLanguageItem().icon));
	             }
	             
	             
	            else  {
	            	 
	                  drawerHolder.profileImageLayout.setVisibility(LinearLayout.INVISIBLE);
	                  drawerHolder.languageItemLayout.setVisibility(LinearLayout.INVISIBLE);
	                  drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);
	                  
	                  drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
	                  drawerHolder.ItemName.setText(dItem.getItemName());
	 
	            }
	            
	            return view;
	      }
	      
	      private static class DrawerItemHolder {
	    	LinearLayout itemLayout, profileImageLayout, languageItemLayout;
	    	ProfilePictureView ppv;
	    	ImageView profilePic, icon, languageFlag;
    	  	TextView ItemName, profileName, tvLanguage;
	      }
	      
}