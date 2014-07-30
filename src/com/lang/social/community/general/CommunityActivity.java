package com.lang.social.community.general;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lang.social.R;

public class CommunityActivity extends FragmentActivity implements
ActionBar.TabListener {

	private ActionBar m_ActionBar; 
	
	//----------------------------------------------------------------------------------------
	  // Tab titles
    private String[] tabs = { "Friends", "Messages", "Online Users", "Chat"};
	//----------------------------------------------------------------------------------------
	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
  //----------------------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_layout);
        setTitle(" Community");	//Activity Title
        
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        m_ActionBar = getActionBar();
        m_ActionBar.setDisplayHomeAsUpEnabled(true);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        m_ActionBar.setHomeButtonEnabled(true);
        m_ActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Adding Tabs
        for (String tab_name : tabs) {
        	if(tab_name.equals("Friends")) {
        		m_ActionBar.addTab(m_ActionBar.newTab().setIcon(R.drawable.firendstab)
                        .setTabListener(this));
        	}
        	else if(tab_name.equals("Messages")) {
        		m_ActionBar.addTab(m_ActionBar.newTab().setIcon(R.drawable.messagestab)
                        .setTabListener(this));
        	}
        	else if(tab_name.equals("Online Users")) {
        		m_ActionBar.addTab(m_ActionBar.newTab().setIcon(R.drawable.onlineusersicon)
                        .setTabListener(this));
        	}
        	else if(tab_name.equals("Chat")) {
        		m_ActionBar.addTab(m_ActionBar.newTab().setIcon(R.drawable.chattabiconpng)
                        .setTabListener(this));
        	}
        	
        	/*m_ActionBar.addTab(m_ActionBar.newTab().setText(tab_name)
                    .setTabListener(this));*/
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
        
		setActionBarItems();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.community_action_bar, menu);
	    
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; goto parent activity.
//	            this.finish();
//	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void setActionBarItems() {
		m_ActionBar.setIcon(getResources().getDrawable(R.drawable.friendsicon));
		
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

	

}
