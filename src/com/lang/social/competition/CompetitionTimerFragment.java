package com.lang.social.competition;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lang.social.R;

public class CompetitionTimerFragment extends Fragment {
	
	private static final int kCountInterval = 1000;
	private static final int kCountMax = 6000;
	
	private TextView mTVCountTimer;

	private CompetitionActivityNotifier mCompetitionStateNotifier;
	
	private CountDownTimer mCountDownTimer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_competition_timer_layout, container, false);
		
		mTVCountTimer = (TextView) view.findViewById(R.id.tvTimerCount);
		
		setTextFonts(view);
		
		initCountDownTimer();
		
		return view;
	}
	

	private void initCountDownTimer() {
		mCountDownTimer =
			new CountDownTimer(kCountMax, kCountInterval) {
			     public void onTick(long millisUntilFinished) {
		            	mTVCountTimer.setText(Long.toString(millisUntilFinished / 1000));
			     }
			     
			     public void onFinish() {
			        //do when finished -> act like player made wrong choice
			     }
			  }; 
	}
	
	@Override
	public void onStart() {
		super.onStart();
		this.StartCountDown();
	}
	
	public void StartCountDown() {
		mCountDownTimer.start();
	}
	
	public void RestartCountDownTimer() {
		mCountDownTimer.cancel();
		this.StartCountDown();
	}


	private void setTextFonts(View view) {
	    Typeface tf = Typeface.createFromAsset(
	    		getActivity().getAssets(),"fonts/Jura-DemiBold.ttf");
	    mTVCountTimer.setTypeface(tf);
		((TextView) view.findViewById(R.id.tvYouHave)).setTypeface(tf);
		((TextView) view.findViewById(R.id.tvSecondsLeft)).setTypeface(tf);
	}
			
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		mCompetitionStateNotifier = (CompetitionActivity) activity;
//	}
//	
}
