package com.lang.social.teachstudy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lang.social.teachstudy.students.StudentsTabFragment;
import com.lang.social.teachstudy.teachers.TeachersTabFragment;

public class StudentTeacherTabsPagerAdapter extends FragmentPagerAdapter {
	
    public StudentTeacherTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
        switch (index) {
        case 0:
            return new StudentsTabFragment();
        case 1:
        	return new TeachersTabFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
}
