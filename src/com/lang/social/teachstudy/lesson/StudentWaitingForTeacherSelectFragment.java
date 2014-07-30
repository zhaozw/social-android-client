package com.lang.social.teachstudy.lesson;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.lang.social.R;
import com.lang.social.logic.User;

public class StudentWaitingForTeacherSelectFragment extends Fragment {
	private User m_Teacher;
	private User m_Student;
	private TextView m_tvWaitingTeacherSelectionLabel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_student_waiting_teacher_select, container, false);
		
		m_tvWaitingTeacherSelectionLabel = (TextView) view.findViewById(R.id.tvWaitingTeacherSelectionLabel);
		
		Bundle args = getArguments();
		if(args != null) {
			m_Teacher = (User) args.getSerializable("Teacher");
			m_Student = (User) args.getSerializable("Student");
		}
		
		m_tvWaitingTeacherSelectionLabel.setText("Waiting For Teacher " + m_Teacher.getFirstName() + " To Select Photo...");
		m_tvWaitingTeacherSelectionLabel.setAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));

		return view;
	}
	
	public void changeText(String text) {
		if(m_tvWaitingTeacherSelectionLabel != null) {
			m_tvWaitingTeacherSelectionLabel.setText(text);
		}
		
	}
	
	
}
