//package com.lang.social.activities;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.facebook.Session;
//import com.lang.social.R;
//import com.lang.social.adapters.LanguageItemAdapter;
//import com.lang.social.controllers.ServerController;
//import com.lang.social.fragments.MyLanguagesDialogFragment;
//import com.lang.social.interfaces.UserLanguagesListListener;
//import com.lang.social.interfaces.UserLanguageUpdateListener;
//import com.lang.social.iocallback.IOCallBackHandler;
//import com.lang.social.iocallback.UserIOCallBackHandler;
//import com.lang.social.items.LanguageItem;
//import com.lang.social.utils.JSONUtils;
//import com.lang.social.utils.MyToaster;
//
//public class MyLanguagesActivity extends Activity implements UserLanguageUpdateListener {
//
//	private LanguageItemAdapter languageItemsAdapter;
//	private ArrayList<LanguageItem> userLanguages;
//	private ArrayList<LanguageItem> allLanguages;
//	private JSONArray allLanguagesArray;
//	
//	private String languagedToRemove;
//	private String languageToAdd;
//	
//	private LanguagesDialogFragment languagesDialogFragment;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_my_languages);
//		
//		IOCallBackHandler.getInstance().setLanguagesUpdateListener(this);
//		
//		languagesDialogFragment = new MyLanguagesDialogFragment();
//		languagesDialogFragment.setLanguageUpdateListener(this);
//
//		try {	
//				Bundle extras = getIntent().getExtras();
//				if (extras != null) {
//				    String languagesJsonStr = extras.getString("languagesJSON");
//				    JSONObject languagesJson = new JSONObject(languagesJsonStr);
//				    
//					JSONArray userLanguagesArray = languagesJson.getJSONArray("userLanguages");
//					allLanguagesArray = languagesJson.getJSONArray("allLanguages");
//					
//					userLanguages = JSONUtils.convertLanguagesJSONArrayToLanguageItemsArray(userLanguagesArray);
//					allLanguages = JSONUtils.convertLanguagesJSONArrayToLanguageItemsArray(allLanguagesArray);
//					
//					createListItems(userLanguages);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		setTextFonts();
//		setButtonListeners();
//	}
//
//	private void showLanguagesFragment() {
//	    Bundle args = new Bundle();
//	    args.putString("allLanguages", allLanguagesArray.toString());
//	    languagesDialogFragment.setArguments(args);	
//	    languagesDialogFragment.show(getFragmentManager(), "MyLanguagesDialogFragment");
//	}
//
//	private void setButtonListeners() {
//		Button btnAddALanguage = (Button) findViewById(R.id.btnAddALanguage);
//		btnAddALanguage.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				showLanguagesFragment();
//			}
//		});
//	}
//
//	private void setTextFonts() {
//		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Jura-DemiBold.ttf");
//		Button btnAddALanguage = (Button) findViewById(R.id.btnAddALanguage);
//		btnAddALanguage.setTypeface(tf);
//	}
//
//	private void createListItems(List<LanguageItem> languageItems) {
//        this.languageItemsAdapter = new LanguageItemAdapter(this, 
//                R.layout.listview_language_item_row, languageItems);
//        ListView ListViewWantToLearn = (ListView)findViewById(R.id.lvMyLanguages);
//        View headerLearn = (View)getLayoutInflater().inflate(R.layout.listview_mylanguages_header, null);
//        ListViewWantToLearn.addHeaderView(headerLearn, null, false);
//        ListViewWantToLearn.setAdapter(languageItemsAdapter);
//        ListViewWantToLearn.setOnItemClickListener(new MyLanguagesItemClickListener());
//	}
//
//	
//	private class MyLanguagesItemClickListener implements OnItemClickListener{
//
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//				long arg3) {
//			final int pos = position;
//			AlertDialog.Builder builder1 = new AlertDialog.Builder(MyLanguagesActivity.this);
//	        builder1.setMessage("Remove Language?");
//	        builder1.setCancelable(true);
//	        builder1.setPositiveButton("Remove",
//	        		new DialogInterface.OnClickListener() {
//	            public void onClick(DialogInterface dialog, int id) {
//	            	languagedToRemove = userLanguages.get(pos-1).title;
//	            	Log.d("userLanguages", "trying to remove : " + languagedToRemove);
//	            	ServerController.sendStringMessage("removeUserLanguage", languagedToRemove);
//	            }
//	        });
//	        builder1.setNegativeButton("Cancel",
//	                new DialogInterface.OnClickListener() {
//	            public void onClick(DialogInterface dialog, int id) {
//	                dialog.cancel();
//	            }
//	        });
//
//	        AlertDialog alert11 = builder1.create();
//	        alert11.show();
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.main_activity_actions, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//
//	private void parseAndUpdateLanguagesAdapter(JSONObject languagesJSON) throws JSONException{
//		//JSONArray userLanguagesArray = languagesJSON.getJSONArray("userLanguages");
//		//userLanguages = JSONUtils.convertLanguagesJSONArrayToLanguageItemsArray(userLanguagesArray);
//		//
//		//for testing
////		for (int i = 0; i < userLanguages.size(); i++) {
////			Log.d("userLanguages", userLanguages.get(i).title);
////		}
//	//	
//		//languageItemsAdapter.clear();
//		//languageItemsAdapter.addAll(userLanguages);
//		//languageItemsAdapter.notifyDataSetChanged();
//	}
//	
//	@Override
//	public void onLanguageAdded(JSONObject jsonLanguages) {
//		try {	
//			boolean success = jsonLanguages.getBoolean("success");			
//			if(success){
//				parseAndUpdateLanguagesAdapter(jsonLanguages.getJSONObject("data"));
//				MyToaster.showToast(MyLanguagesActivity.this, "Added " + this.languageToAdd + " to your profile", Toast.LENGTH_SHORT);
//				Log.d("userLanguages", "after adding language and notifying adapter");
//				Log.d("userLanguages", "msg from server : " + jsonLanguages.getString("msg"));
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Override
//	public void onLanguageRemoved(JSONObject jsonLanguages) {
//		try {	
//			boolean success = jsonLanguages.getBoolean("success");			
//			if(success){
//				parseAndUpdateLanguagesAdapter(jsonLanguages.getJSONObject("data"));
//				MyToaster.showToast(MyLanguagesActivity.this, "Removed " + this.languagedToRemove + " from your profile", Toast.LENGTH_SHORT);
//				Log.d("userLanguages", "after removing language and notifying adapter");
//				Log.d("userLanguages", "msg from server : " + jsonLanguages.getString("msg"));
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Override
//	public void onLanguageAddPress(String languageToAdd) {
//		this.languageToAdd = languageToAdd;
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle presses on the action bar items
//		switch (item.getItemId()) {
//		case R.id.action_settings:
//			openSettings();
//			return true;
//		case R.id.action_logout:
//			logOut();
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
//
//	private void logOut() {
//		Session session = Session.getActiveSession();
//		if (session != null) {
//			session.closeAndClearTokenInformation();
//		}
//		startActivity(new Intent(this, LoginActivity.class));
//	}
//
//	private void openSettings() {
//		// TODO Auto-generated method stub
//	}
//
//	
//}
//
//
//
//
//
//
//
//
