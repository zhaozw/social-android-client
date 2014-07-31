package com.lang.social.community.general;
 
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lang.social.community.chat.CommunityChatTabFragment;
import com.lang.social.community.friends.FriendsTabFragment;
import com.lang.social.community.messages.MessagesTabFragment;
import com.lang.social.community.onlineusers.OnlineUsersTabFragment;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new FriendsTabFragment();
        case 1:
            // Games fragment activity
        	return new MessagesTabFragment();
        case 2:
            return new OnlineUsersTabFragment();
        case 3:
            return new CommunityChatTabFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
 
}