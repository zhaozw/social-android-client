package com.lang.social.profile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lang.social.R;
import com.lang.social.activities.HomeActivity;
import com.lang.social.adapters.CustomDrawerAdapter;
import com.lang.social.community.general.CommunityActivity;
import com.lang.social.community.messages.MessagesServerListener;
import com.lang.social.controllers.ServerController;
import com.lang.social.fragments.LanguagesDialogFragment;
import com.lang.social.interfaces.ProfilePageListener;
import com.lang.social.interfaces.UserLanguageUpdateListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.items.DrawerItem;
import com.lang.social.items.LanguageItem;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;
import com.mikhaellopez.circularimageview.CircularImageView;


public class ProfileActivity extends FragmentActivity 
	implements MessageFragmentListener , UserLanguageUpdateListener,
		MessagesServerListener , FriendFragmentListener, FriendDetailsResponseListener, ProfilePageListener {

    private static final int PROGRESS = 0x1;
    private ProgressBar mProgress;
    private static final int PROGRESS_BASE_START = 3000;
	private static final String TAG = "Profile";
    private int mProgressStartPoistion = 0;
    
	//Custom Drawer
	//----------------------------------------------
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
    private CustomDrawerAdapter mDrawerAdapter;
    List<DrawerItem> mDrawerItems;
	//----------------------------------------------
    
	private CircularImageView user_picture;
	 
	private ProfileMessagesFragment profileMessagesFragment = new ProfileMessagesFragment();
	private ProfileStatusFragment profileStatusFragment =  new ProfileStatusFragment();
	private ProfileFriendsFragment profileFriendsFragment = new ProfileFriendsFragment();
	private ProfileMessageDetailsFragment profileMessageDetailsFragment = new ProfileMessageDetailsFragment();
	private ProfileFriendProfileViewFragment friendProfileViewFragment = new ProfileFriendProfileViewFragment();
 
	private FragmentManager fragmentManager = getSupportFragmentManager();
	
	private boolean needToFetchUpdatedUserMessages = false;
	
	private ImageView[] imageButtons = new ImageView[3];
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile3);

		IOCallBackHandler.getInstance().setMessagesListener(this);
		IOCallBackHandler.getInstance().setProfileFriendReseponseListener(this);
		
		initLanguagesJsonArray();
		
		setFonts();

		//parse from intent
		
		Bundle bundle = getIntent().getExtras();

		
		String strCurrentUserPoints = bundle.getString(ProfileConstants.CurrentUserPoints);
		String strNumOfPointsToNextLevel = bundle.getString(ProfileConstants.PointsToNextLevel);
		
		//get user points and number of points to next level
		int currentUserPoints = Integer.parseInt(strCurrentUserPoints);
		int numOfPointsToNextLevel = Integer.parseInt(strNumOfPointsToNextLevel);
		
		//setting the user details recieved from intent
		((TextView)findViewById(R.id.tvProfileFullName)).setText(UserController.getUser().getFullName());
		
		((TextView)findViewById(R.id.tvLanguageLevel)).setText(
				  UserController.getUser().getLearningLanguage() + " "
				+ UserController.getUser().getCurrentLanguageLevel());
		
		((ImageView)findViewById(R.id.ivLanguageFlag))
				.setImageDrawable(getResources().getDrawable(UserController.getFlagImageRes()));
		
		((TextView)findViewById(R.id.tvPointsToNextLevel)).setText("You need  "
				+ String.valueOf(numOfPointsToNextLevel) 
				+ " more points to reach next level");
		
		((TextView)findViewById(R.id.tvProfileLearningLanguage)).setText(UserController.getLearningLanguageText());
		
		///
		mProgress = (ProgressBar) findViewById(R.id.progressBarLevels);
		mProgressStartPoistion = PROGRESS_BASE_START + currentUserPoints;
		mProgress.setMax(15000);
		mProgress.setProgress(mProgressStartPoistion);
		//
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		user_picture = (CircularImageView)findViewById(R.id.profilepicture);
		setProfilePicture();

//		CircularImageView circularImageView = (CircularImageView)findViewById(R.id.profilepicture);
		
		setImagesClickListeners();

		profileStatusFragment.setArguments(bundle);
		
		profileMessagesFragment.setArguments(bundle);
		
		profileFriendsFragment.setArguments(bundle);
		showFragment(profileFriendsFragment);

		initSideDrawer();
	}

	private void initSideDrawer() {
		
		// SideDrawer Implementation Start
		//---------------------------------------------------------------

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        addItemsToDrawerDataList();
        
        mDrawerAdapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, mDrawerItems);
        
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("More");
        
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()     
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
        // SideDrawer Implementation End
		//---------------------------------------------------------------
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // The action bar home/up action should open or close the drawer.
	    // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        
		int itemId = item.getItemId();
		if (itemId == R.id.action_settings) {
			return true;
		} else if (itemId == R.id.action_logout) {
			new UserSessionManager(this).logOutUser();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	
	/* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
	private void selectItem(int position) {
        switch (position) {
        case 1:
			boolean isCancelable = true;
			showLanguagesFragment(isCancelable);
            break;
        case 2:
            startActivity(new Intent(ProfileActivity.this, CommunityActivity.class));
            break;
        default:
              break;
        }

        mDrawerList.setItemChecked(position, false);
        mDrawerLayout.closeDrawer(mDrawerList);
        
	}
	
	
	private LanguagesDialogFragment mLanguagesDialogFragment;
	private JSONArray mLanguagesArray;
	
	private void showLanguagesFragment(boolean isCancelable) {
	    Bundle args = new Bundle();
	    if(mLanguagesDialogFragment == null){
	    	mLanguagesDialogFragment = new LanguagesDialogFragment();
	    }
	    args.putString("allLanguages", mLanguagesArray.toString());
	    mLanguagesDialogFragment.setArguments(args);	
	    mLanguagesDialogFragment.setCancelable(isCancelable);
	    mLanguagesDialogFragment.show(getFragmentManager(), "LanguagesDialogFragment");
	}

	private void initLanguagesJsonArray() {
		mLanguagesArray = new JSONArray();
		try{
			JSONObject lang1 = new JSONObject();
			lang1.put("resIconId", R.drawable.israel1);
			lang1.put("language", "Hebrew");
			
			JSONObject lang2 = new JSONObject();
			lang2.put("resIconId", R.drawable.spain1);
			lang2.put("language", "Spanish");
			
			JSONObject lang3 = new JSONObject();
			lang3.put("resIconId", R.drawable.france1);
			lang3.put("language", "French");
			
			JSONObject lang4 = new JSONObject();
			lang4.put("resIconId", R.drawable.italy1);
			lang4.put("language", "Italian");
			
			JSONObject lang5 = new JSONObject();
			lang5.put("resIconId", R.drawable.germany1);
			lang5.put("language", "German");
			
			JSONObject lang6 = new JSONObject();
			lang6.put("resIconId", R.drawable.netherlands1);
			lang6.put("language", "Dutch");

			mLanguagesArray.put(lang1);
			mLanguagesArray.put(lang2);
			mLanguagesArray.put(lang3);
			mLanguagesArray.put(lang4);
			mLanguagesArray.put(lang5);
			mLanguagesArray.put(lang6);

		}
		catch(JSONException ex){
			ex.printStackTrace();
		}

	}
	
	private void repopulateDrawerDataList(){
		addItemsToDrawerDataList();
		mDrawerAdapter.clear();
		mDrawerAdapter.addAll(mDrawerItems);
		mDrawerAdapter.notifyDataSetChanged();
	}

	//After user chooses language in LanguageFragment
	//--------------------------------------------------
	@Override
	public void OnLanguageUpdate(int position) {
		try {
			
			JSONObject languageJson = mLanguagesArray.getJSONObject(position);
			ServerController.sendJSONMessage("languageLearnUpdate", languageJson);
			UserController.getUser().setLearningLanguage(languageJson.getString("language"));
			UserController.getUser().setHaveLanguage(true);
			repopulateDrawerDataList();
 
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}

	//Server response for updating language
	//-----------------------------------------------------
	@Override
	public void OnLanguageUpdateResponse(JSONObject json) {
		ArrayList<String> jsonKeys = null;
		ServerResponseParser srp = new ServerResponseParser(json, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()){
			MyToaster.showToast(ProfileActivity.this, "Language updated successfuly!", Toast.LENGTH_SHORT);
		}
	}
	
	//-----------------------------------------------------
	
	private void addItemsToDrawerDataList() {
		mDrawerItems = new ArrayList<DrawerItem>();
		if(UserController.getUser().isFacebookUser()){
			mDrawerItems.add(new DrawerItem(UserController.getUser().getProfileID()));
		}
		else{
			mDrawerItems.add(new DrawerItem(BitmapFactory.decodeResource(getResources(), R.drawable.ic_whats_hot)));
		}
		if(UserController.getUser().HaveLanguage()) {
			mDrawerItems.add(new DrawerItem(new LanguageItem(UserController.getFlagImageRes(), UserController.getLearningLanguageText())));
		}

		mDrawerItems.add(new DrawerItem("Community", R.drawable.ic_action_group));
	}
	private void setImagesClickListeners() {
		imageButtons[0] = (ImageView) findViewById(R.id.ivProfileMessages);
		imageButtons[1] = (ImageView) findViewById(R.id.ivProfileStatus);
		imageButtons[2] = (ImageView) findViewById(R.id.ivProfileFriends);
		
		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
		imageButtons[0].startAnimation(animation);
		imageButtons[1].startAnimation(animation);
		imageButtons[2].startAnimation(animation);
		
		imageButtons[0].setOnTouchListener(new CustomTouchListener(profileMessagesFragment, 0));
		imageButtons[1].setOnTouchListener(new CustomTouchListener(profileStatusFragment, 1));	
		imageButtons[2].setOnTouchListener(new CustomTouchListener(profileFriendsFragment, 2));	
	}
	
	private void showOnClickAnimation(int index) {
		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
		imageButtons[index].startAnimation(animation);
	}
	
	private class CustomTouchListener implements View.OnTouchListener {
		
		private Fragment fragmentToShow; 
		private int index;
		
		public CustomTouchListener(Fragment fragment, int index) {
			fragmentToShow = fragment;
			this.index = index;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			  switch (event.getAction()) {
              case MotionEvent.ACTION_DOWN: {
            	  showOnClickAnimation(index);
            	  if(fragmentToShow instanceof ProfileMessagesFragment && needToFetchUpdatedUserMessages){
        			ServerController.sendJSONMessage(ProfileConstants.ProfileDetailsRequest, null);
            	  } else {
            		  showFragment(fragmentToShow);
            	  }
                  ImageView view = (ImageView) v;
                  //overlay is black with transparency of 0x77 (119)
                  view.getDrawable().setColorFilter(0x77000000,PorterDuff.Mode.SRC_ATOP);
                  view.invalidate();
                  break;
              }
              case MotionEvent.ACTION_UP:
              case MotionEvent.ACTION_CANCEL: {
                  ImageView view = (ImageView) v;
                  //clear the overlay
                  view.getDrawable().clearColorFilter();
                  view.invalidate();
                  break;
              }
          }

          return true;
		}
	}

	private void showFragment(Fragment fragment) {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.profileFragmentContainer, fragment);
		fragmentTransaction.commit();
	}	
	
	private void setProfilePicture() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				 try {
					 //img_value = new URL("http://graph.facebook.com/1118208362/picture?type=large");
					 URL MyProfilePicURL = new URL("https://graph.facebook.com/me/picture?type=large&width=100&height=100&method=GET&access_token="+ UserSessionManager.GetAccessToken());
					 Bitmap mIcon1 = BitmapFactory.decodeStream(MyProfilePicURL.openConnection().getInputStream());
					 onProfileImageRecieved(mIcon1);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}


		}).start();
	}
	
	@Override
	public void OnShowMessageDetailsClick(Message message) {
		Log.d("Profile", "user clicked show message details");
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("Message", message);

		profileMessageDetailsFragment.setArguments(bundle);
		
		showFragment(profileMessageDetailsFragment);
		
	}
	
	private void onProfileImageRecieved(final Bitmap icon) {
		 runOnUiThread(new Runnable() {
			public void run() {
				 user_picture.setImageBitmap(icon);
			}
		});
	}


	private void setFonts() {
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Roboto-LightItalic.ttf");
		Typeface tf1 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-ThinItalic.ttf");
		Typeface tf2 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
		Typeface tf3 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
		Typeface tf4 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");

		((TextView)findViewById(R.id.tvProfileFullName)).setTypeface(tf);
		((TextView)findViewById(R.id.tvProfileLearningLanguage)).setTypeface(tf);
		((TextView)findViewById(R.id.tvLanguageLevel)).setTypeface(tf);
		
		((TextView)findViewById(R.id.tvUserProfileFriends)).setTypeface(tf);
		((TextView)findViewById(R.id.tvUserProfileMessages)).setTypeface(tf);
		((TextView)findViewById(R.id.tvUserProfileStatus)).setTypeface(tf);
		
		((TextView)findViewById(R.id.tvLevel1)).setTypeface(tf);
		((TextView)findViewById(R.id.tvLevel2)).setTypeface(tf);
		((TextView)findViewById(R.id.tvLevel3)).setTypeface(tf);
		((TextView)findViewById(R.id.tvLevelSkilled)).setTypeface(tf);
		((TextView)findViewById(R.id.tvLevelAdvanced)).setTypeface(tf);
		((TextView)findViewById(R.id.tvLevelLast)).setTypeface(tf);

		((TextView)findViewById(R.id.tvPointsToNextLevel)).setTypeface(tf1);
	}
	

	@Override
	public void onMessagesListRespone(JSONObject obj) {
		// TODO Auto-generated method stub
	}


	@Override
	public void onMesssageDeleteRespone(JSONObject obj) {
		// TODO Auto-generated method stub	
	}


	@Override
	public void OnMessageSentResponse(JSONObject jsonResponse) {
		List<String> jsonKeys = Arrays.asList("result");
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()) {
			MyToaster.showToast(this, "Message sent!", Toast.LENGTH_SHORT);
			if(needToFetchUpdatedUserMessages){
				ServerController.sendJSONMessage(ProfileConstants.ProfileDetailsRequest, null);
			} else {
				showFragment(this.profileMessagesFragment);
			}
		}
	}

	@Override
	public void OnFriendProfileViewRequestClick(User friend) {
		JSONObject jsonFriendDetails = new JSONObject();
		if(friend.isFacebookUser()){
			JSONUtils.setStringValue(jsonFriendDetails, "isFacebookUser", "true");
			JSONUtils.setStringValue(jsonFriendDetails, "friendprofileid", friend.getProfileID());
		} else{
			JSONUtils.setStringValue(jsonFriendDetails, "isFacebookUser", "false");
			JSONUtils.setStringValue(jsonFriendDetails, "friendusername", friend.getUserName());
		}
		ServerController.sendJSONMessage("friendUserDetailsRequest", jsonFriendDetails);
	}

	@Override
	public void OnFriendDetailsResponse(JSONObject jsonResponse) {
		List<String> jsonKeys = Arrays.asList("result", "friend");
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()) {
			JSONObject friendJson = JSONUtils.getJSONObject(jsonResponse, "friend");
			User friend = new User(friendJson);
			Bundle b = new Bundle();
			b.putSerializable("friend", friend);
			friendProfileViewFragment.setArguments(b);
			showFragment(friendProfileViewFragment);
		}
	}
	
	@Override
	public void onProfileDetailsRecieved(JSONObject jsonResponse) {
		Log.d(TAG, jsonResponse.toString());
		
		List<String> jsonKeys = Arrays.asList(ProfileConstants.UserMessages);
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()){
			JSONArray messages = JSONUtils.getJSONArray(
					jsonResponse, 
					ProfileConstants.UserMessages);
			Bundle b = new Bundle();        
			b.putString(ProfileConstants.UserMessages, messages.toString());
			profileMessagesFragment.setArguments(b);
			needToFetchUpdatedUserMessages = false;
		}
	}

	@Override
	public void OnRemovedMessage(Message message) {
		this.needToFetchUpdatedUserMessages = true;
	}

	@Override
	public void OnMessagesViewRequestClick() {
		showFragment(profileMessagesFragment);
	}

}
