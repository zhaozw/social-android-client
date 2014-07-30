package com.lang.social.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.lang.social.R;
import com.lang.social.adapters.CustomDrawerAdapter;
import com.lang.social.adapters.CustomGridViewAdapter;
import com.lang.social.community.general.CommunityActivity;
import com.lang.social.controllers.ServerController;
import com.lang.social.fragments.AboutDialogFragment;
import com.lang.social.fragments.HelpDialogFragment;
import com.lang.social.fragments.LanguagesDialogFragment;
import com.lang.social.interfaces.ProfilePageListener;
import com.lang.social.interfaces.UserLanguageUpdateListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.items.DrawerItem;
import com.lang.social.items.GridItem;
import com.lang.social.items.LanguageItem;
import com.lang.social.items.SpinnerItem;
import com.lang.social.logic.UserController;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.profile.ProfileActivity;
import com.lang.social.profile.ProfileConstants;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;


public class HomeActivity extends Activity implements UserLanguageUpdateListener, ProfilePageListener {
	
	protected static final String TAG = "HomeActivity";
	//Grid
	//----------------------------------------------
	GridView gridView; 
	ArrayList<GridItem> gridArray = new ArrayList<GridItem>();
	CustomGridViewAdapter customGridAdapter;
	//----------------------------------------------
	
	//Custom Drawer
	//----------------------------------------------
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle = "SociaLang";
    private CustomDrawerAdapter mDrawerAdapter;
    List<DrawerItem> mDrawerItems;
    List<SpinnerItem> mSpinnerItems;
    //----------------------------------------------
    
    //Languages DialogFragment
    //----------------------------------------------
	private JSONArray mLanguagesArray;
	private LanguagesDialogFragment mLanguagesDialogFragment;

    //Current User Icon Bitmap
    //----------------------------------------------
	private Bitmap mDefaultUserIconBitmap;
	
	//About DialogFragment
	private AboutDialogFragment mAboutDialogFragment;
	
	//Help DialogFragment
	private HelpDialogFragment mHelpdDialogFragment;
	
	private static final String FACEBOOKTAG = "FACEBOOK";
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	private UiLifecycleHelper uiHelper;
	private Session mSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		mDefaultUserIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_whats_hot);
		
		initLanguagesJsonArray();
		
		Bitmap selflearnbluelight = BitmapFactory.decodeResource(this.getResources(), R.drawable.selflearnbluelight); 
		Bitmap socialbluelight = BitmapFactory.decodeResource(this.getResources(), R.drawable.socialbluelight); 
		Bitmap profile = BitmapFactory.decodeResource(this.getResources(), R.drawable.socialprofile);

		gridArray.add(new GridItem(selflearnbluelight,"Self Learn"));
		gridArray.add(new GridItem(socialbluelight,"Social Learn"));
		gridArray.add(new GridItem(profile ,"   Profile"));

		gridView = (GridView) findViewById(R.id.gridView1);
		customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
		gridView.setAdapter(customGridAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				switch(position){
				case 0 :
					startActivity(new Intent(HomeActivity.this, SelfLearnMenuActivity.class));
					break;
				case 1:
					startActivity(new Intent(HomeActivity.this, SocialLearnMenuActivity.class));
					break;
				case 2:
					IOCallBackHandler.getInstance().setProfileDetailsListener(HomeActivity.this);
					ServerController.sendJSONMessage(ProfileConstants.ProfileDetailsRequest, null);
					break;
				}
			}
		});
		
		
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
        getActionBar().setTitle(mTitle);
        
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
      
	    uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
	    
	    if (savedInstanceState != null) {
	        pendingPublishReauthorization = savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
	    }

   }
	
	///FACEBOOK IMPLEMENTANION START --//////////////////////////////////////////////////////////////////
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (pendingPublishReauthorization && state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
		    pendingPublishReauthorization = false;
		    publishStory();
		}
	}

    private void publishStory() {
        Session session = Session.getActiveSession();
        if (session != null){

            // Check for publish permissions    
            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }
            
            Bundle postParams = new Bundle();
            postParams.putString("name", "SociaLang");
            postParams.putString("caption", "Language Community For The World!");
            postParams.putString("description", "SociaLang promotes education world wide");
            postParams.putString("link", "https://www.facebook.com/socialang");
            postParams.putString("picture", "http://postimg.org/image/4xco6e67f/");

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {
                    JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                    } catch (JSONException e) {
                        Log.i(TAG, "JSON error "+ e.getMessage());
                    }
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Toast.makeText(HomeActivity.this, error.getErrorMessage(),Toast.LENGTH_SHORT).show();
                        } else {
                        Toast.makeText(HomeActivity.this, postId, Toast.LENGTH_LONG).show();
                    }
                }
            };

            Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);
            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }
    }
    
    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	    outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		IOCallBackHandler.getInstance().setLanguageUpdateListener(this);
		if(!UserController.getUser().HaveLanguage())
		{
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
			  @Override
			  public void run() {
				  boolean isCancelable = false;
				  showLanguagesFragment(isCancelable);
			  }
			}, 1000);
		}
		
	    Session session = Session.getActiveSession();
	    if (session != null && (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
		uiHelper.onResume();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	///FACEBOOK IMPLEMENTANION END --//////////////////////////////////////////////////////////////////

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
			MyToaster.showToast(HomeActivity.this, "Language Updated Successfuly!", Toast.LENGTH_SHORT);
		}
	}
	
	//-----------------------------------------------------

	
	private void repopulateDrawerDataList(){
		addItemsToDrawerDataList();
		mDrawerAdapter.clear();
		mDrawerAdapter.addAll(mDrawerItems);
		mDrawerAdapter.notifyDataSetChanged();
	}

	private void addItemsToDrawerDataList() {
		
		mDrawerItems = new ArrayList<DrawerItem>();

		if(UserController.getUser().isFacebookUser())
		{
			mDrawerItems.add(new DrawerItem(UserController.getUser().getProfileID()));
		}
		else
		{
			mDrawerItems.add(new DrawerItem(mDefaultUserIconBitmap));
		}
		
		if(UserController.getUser().HaveLanguage()) 
		{
			mDrawerItems.add(new DrawerItem(new LanguageItem(UserController.getFlagImageRes(), UserController.getLearningLanguageText())));
		}

		mDrawerItems.add(new DrawerItem("Community", R.drawable.ic_action_group));
		//mDrawerItems.add(new DrawerItem("Settings", R.drawable.ic_action_settings));
		mDrawerItems.add(new DrawerItem("Help", R.drawable.ic_action_help));
		mDrawerItems.add(new DrawerItem("About", R.drawable.ic_action_about));
		
		if(UserSessionManager.IsLoggedInByFacebook()){
			mDrawerItems.add(new DrawerItem("Share", R.drawable.facebookicon1));
		}

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

	@Override
    public void setTitle(CharSequence title) {
         mTitle = title;
         getActionBar().setTitle(mTitle);
    }
	
	private void selectItem(int position) {
        switch (position) {
        case 1:
			boolean isCancelable = true;
			showLanguagesFragment(isCancelable);
            break;
        case 2:
            startActivity(new Intent(HomeActivity.this, CommunityActivity.class));
            break;
        case 4:
        	showHelpFragment();
        	break;
        case 5:
        	showAboutFragment();
        	break;
        case 6:
        	publishStory();
        	break;
        default:
              break;
        }

        mDrawerList.setItemChecked(position, false);
        mDrawerLayout.closeDrawer(mDrawerList);
        
	}
	
	private void showHelpFragment() {
    	if(mHelpdDialogFragment == null){
    		mHelpdDialogFragment = new HelpDialogFragment();
	    }
    	mHelpdDialogFragment.show(getFragmentManager(), "HelpDialogFragment");
		
	}


	private void showAboutFragment() {
	    if(mAboutDialogFragment == null){
	    	mAboutDialogFragment = new AboutDialogFragment();
	    }
	    mAboutDialogFragment.show(getFragmentManager(), "AboutDialogFragment");
	}
    
    
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



	/* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
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

	////////////////////////////////////////////////////////////////////////////////////////////////
	// SideDrawer Implementation End
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onProfileDetailsRecieved(JSONObject jsonResponse) {
		Log.d(TAG, jsonResponse.toString());
		
		List<String> jsonKeys = Arrays.asList(
				ProfileConstants.PointsToNextLevel,
				ProfileConstants.CurrentUserPoints,
				ProfileConstants.UserLanguage,
				ProfileConstants.UserFriends,
				ProfileConstants.UserMessages,
				ProfileConstants.UserStats
			);
		
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		
		if(srp.isOkResult())
		{
			String languageLearnning = JSONUtils.getStringFromJSON(
					jsonResponse, 
					ProfileConstants.UserLanguage,
					"Could not get user language from json");
			
			String pointToNextLevel = JSONUtils.getStringFromJSON(
					jsonResponse, 
					ProfileConstants.PointsToNextLevel,
					"Could not get user language from json");
			
			String currentUserPoints = JSONUtils.getStringFromJSON(
					jsonResponse, 
					ProfileConstants.CurrentUserPoints,
					"Could not get user language from json");
			
			JSONArray friends = JSONUtils.getJSONArray(
					jsonResponse, 
					ProfileConstants.UserFriends);
			
			JSONArray messages = JSONUtils.getJSONArray(
					jsonResponse, 
					ProfileConstants.UserMessages);
			
			JSONArray stats = JSONUtils.getJSONArray(
					jsonResponse, 
					ProfileConstants.UserStats);
			
			Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
			
			Bundle b = new Bundle();        
			
			b.putString(ProfileConstants.UserFriends, friends.toString());
			b.putString(ProfileConstants.UserMessages, messages.toString());
			b.putString(ProfileConstants.UserLanguage, languageLearnning);
			b.putString(ProfileConstants.PointsToNextLevel, pointToNextLevel);
			b.putString(ProfileConstants.CurrentUserPoints, currentUserPoints);
			b.putString(ProfileConstants.UserStats, stats.toString());
			
			i.putExtras(b);
			
			startActivity(i);
		}
	}
}
