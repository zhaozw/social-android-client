package com.lang.social.teachstudy;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lang.social.R;
import com.lang.social.competition.SocialGameConstants;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.GameType;
import com.lang.social.logic.UserController;
import com.lang.social.room.RoomActivity;
import com.lang.social.room.RoomConstants;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.JSONUtils;

public class StudentTeacherActivity extends FragmentActivity implements
ActionBar.TabListener, StudentTeacherServerListener {

	private ActionBar m_ActionBar; 
  //----------------------------------------------------------------------------------------
  // Tab titles
    private String[] tabs = { "Students", "Teachers"};
	//----------------------------------------------------------------------------------------
	private ViewPager viewPager;
    private StudentTeacherTabsPagerAdapter mAdapter;
    private ProgressDialog mProgressDialog;
  //----------------------------------------------------------------------------------------
    private GameType mGameType;
    
  //----------------------------------------------------------------------------------------
    private static final String TAG = "StudentTeacherActivity";
	private static final String roomIDKey = "GameRoomID";
  //----------------------------------------------------------------------------------------

	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_teacher);
        setTitle("Study / Teach");	//Activity Title
        
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.studentTeacherPager);
        m_ActionBar = getActionBar();
        m_ActionBar.setDisplayHomeAsUpEnabled(true);
        mAdapter = new StudentTeacherTabsPagerAdapter(getSupportFragmentManager());
        createProgressDialog();
        IOCallBackHandler.getInstance().setStudentTeacherListener(this);
        
        viewPager.setAdapter(mAdapter);
        m_ActionBar.setHomeButtonEnabled(true);
        m_ActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        m_ActionBar.setIcon(getResources().getDrawable(R.drawable.studentteacher));
        
        // Adding Tabs
        for (String tab_name : tabs) {
        	m_ActionBar.addTab(m_ActionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
  
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
       	 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
            	m_ActionBar.setSelectedNavigationItem(position);
            }
         
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
         
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
   
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.student_teacher, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case android.R.id.home:
	    		finish();
	    		return true;
	        case R.id.action_new_game:
	        	if(m_ActionBar.getSelectedTab().toString().equals(m_ActionBar.getTabAt(0).toString())) { //TAB[0] = Students Tab
	        		createNewStudentGameItem();
	        	}
	        	else {
	        		createNewTeacherGameItem();
	        	}
	            return true;
	        case R.id.action_logout:
	        	new UserSessionManager(this).logOutUser();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void createProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(true);
	}
	
	private void createNewTeacherGameItem() {
		requestOpenNewTeachingGameFromServer(GameType.TeacherGame);
		
	}
	
	private void createNewStudentGameItem() {
		requestOpenNewTeachingGameFromServer(GameType.StudentGame);
	}

	private void requestOpenNewTeachingGameFromServer(GameType i_type) {
		Log.d(TAG, "Sending startNewTeachingGame Request");
		mGameType = i_type;
		JSONObject json = new JSONObject();
		JSONUtils.setStringValue(json, "GameType", i_type.toString());
		ServerController.sendJSONMessage(RoomConstants.startGameRequest, json);
		mProgressDialog.show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
		
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	// A new room has been  Opened.
	@Override
	public void onNewGameOpenedRespone(JSONObject jsonRespone) {
		Log.d(TAG, "in OnStudentTeacherGameStartResponse");
		IOCallBackHandler.getInstance().setStudentsTabFragmentListener(null); //?
		IOCallBackHandler.getInstance().setTeachersTabFragmentListener(null); //?
		
		mProgressDialog.dismiss();
		String result = JSONUtils.getStringFromJSON(jsonRespone, "result", null);
		if(result.equals("OK"))
		{
	    	Intent intent = new Intent(this, RoomActivity.class);
	    	intent.putExtra(StudentTeacherConstants.IntentRoomStateKEY, "Created");	//Room Condition
	    	intent.putExtra(RoomConstants.GameTypeKEY, mGameType);
	    	if(mGameType == GameType.StudentGame) {
	    		intent.putExtra(StudentTeacherConstants.IsTeacherHost, StudentTeacherConstants.TeacherNotHost);
	    	}
	    	else {
	    		intent.putExtra(StudentTeacherConstants.IsTeacherHost, StudentTeacherConstants.TeacherHost);
	    	}
	    	intent.putExtra(SocialGameConstants.IntentPlayer1Key, UserController.getUser());
			startActivity(intent);
		}
		else 
		{
			Log.d("StudentTeacher", "Failed To Open Game!");
		}
		
	}

}
