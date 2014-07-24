package com.lang.social.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.lang.social.R;
import com.lang.social.adapters.LanguageItemAdapter;
import com.lang.social.interfaces.UserLanguageUpdateListener;
import com.lang.social.items.LanguageItem;
import com.lang.social.utils.JSONUtils;

public class LanguagesDialogFragment extends DialogFragment {

		private UserLanguageUpdateListener mLanguageUpdateListener;
	
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			String allLanguages = this.getArguments().getString("allLanguages");
			Log.d("onCreateDialog", "languages are : " + allLanguages);

			Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Regular.ttf");
			TextView title = new TextView(getActivity());
			title.setText("Please choose your language");
			title.setTypeface(tf);
			title.setPadding(30, 10, 10, 30);

			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			try {
				
				JSONArray languagesJsonArr = new JSONArray(allLanguages);
				final ArrayList<LanguageItem> languageItems = JSONUtils.convertLanguagesJSONArrayToLanguageItemsArray(languagesJsonArr);
				LanguageItemAdapter languagesAdapter = new LanguageItemAdapter(getActivity(), R.layout.listview_language_item_row, languageItems);
			    builder.setCustomTitle(title)
			           .setAdapter(languagesAdapter, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int which) {
				        	   mLanguageUpdateListener.OnLanguageUpdate(which);
				           }
			    });
			    
			} catch (JSONException e) {
				Log.d("onCreateDialog", "error in parsing language items");
				e.printStackTrace();
			}
			
			 return builder.create();
		} 
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			mLanguageUpdateListener = (UserLanguageUpdateListener) activity;
		}
		
		
}
