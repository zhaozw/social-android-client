package com.lang.social.competition;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lang.social.R;
import com.lang.social.competition.CompetitionGame.PlayerNumber;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.utils.JSONUtils;

public class CompetitionGameFragment extends Fragment implements HeadToHeadListener  {
	
	//constants
	//--------------------------------------------------------------------------------------
	private static final int vIconDrawableRes = R.drawable.vicon;
	private static final int xIconDrawableRes = R.drawable.xicon;
	private static final int kGreenBtnShapeRes = R.drawable.custom_green_btn_shape;
	private static final int kRedBtnShapeRes = R.drawable.custom_red_btn_shape;
	private static final String kCORRECT_MSG = "Correct!";
	private static final String kWRONG_MSG = "Wrong!";
	private static final String playerAnswerKEY = "playerAnswer";
	private static final String kRightResponseKEY = "Right";
	private static final String kWrongResponseKEY = "Wrong";
	private static final String kAnswerStatusResponseKEY = "answerStatus";
	
	private static final int kCorrectnessStatusShownTime = 2500;
	//--------------------------------------------------------------------------------------
	CompetitionTimerFragment mCompetitionTimerFragment = new CompetitionTimerFragment();
	FragmentManager mFragmentManager = getFragmentManager();

	//--------------------------------------------------------------------------------------
	private CompetitionActivityNotifier mCompetitionActivityNotifier;
	private Button[] mChoiceButtons = new Button[4];
	private TextView mCorrectStatusMessage;
	private ImageView mCorrectStatusIcon;
	private TextView mFullNameTV;
	private TextView mTranslateThisTV;
	private TextView mTheWordToTranslate;

	private Round mGameRound;
	
	private EndGameDialogFragment mEndGameDialogFragment;
	
	private int mCorrectButtonIndex;
	
	private CompetitionGame mCompetitionGame;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_competition_game_layout, container, false);
		initWidgets(view);
		setFonts();
		setButtonListeners(view);
		resetChoiceButtonBackground();
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IOCallBackHandler.getInstance().setHeadToHeadListener(this);
		askForNextRoundFromServer();
	}
	
	private void askForNextRoundFromServer() {
		ServerController.sendJSONMessage(SocialGameConstants.QuestionAndAnswersRequest, new JSONObject());
	}
	
	private void notifyServerAboutActionMade(int buttonIndexPressed){
		JSONObject json = new JSONObject();
		JSONUtils.setStringValue(json, "answer", mChoiceButtons[buttonIndexPressed].getText().toString());
		ServerController.sendJSONMessage(SocialGameConstants.playerActionNotify, json);
	}
	
	private void handleUserChoice(int buttonIndexPressed){
		notifyServerAboutActionMade(buttonIndexPressed);
		askForNextRoundFromServer();
		initSuccessStatusGraphicsBeforeShow();
		if(mChoiceButtons[buttonIndexPressed].getText().toString().equals(mGameRound.getmCorrectAnswer())){
			Log.d("Comp", "invoking onCorrectAnswer");
			onCorrectAnswer(buttonIndexPressed);
		} else {
			Log.d("Comp", "invoking onWrongAnswer");
			onWrongAnswer(buttonIndexPressed);
			if(mCompetitionGame.getCurrentPlayer().getNumOfHearts() == 0){
				showLostGameGraphicAfterDelay();
			}
		}
		
		lockChoiceButtonsLock();
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				initNewRound();
			}
		}, kCorrectnessStatusShownTime);
	}
	
	private void showLostGameGraphicAfterDelay() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				showLostGameGraphic();
			}
		}, kCorrectnessStatusShownTime);
	}
	
	private void showLostGameGraphic(){
		mEndGameDialogFragment = new EndGameDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("message", "You Lost!");
		mEndGameDialogFragment.setArguments(bundle);
		mEndGameDialogFragment.show(getFragmentManager(), "EndGameFragment");
	}
	
	private void initNewRound() {
		Log.d("Comp", "in initNewRound");
  		mCorrectStatusMessage.setVisibility(View.INVISIBLE);
		mCorrectStatusIcon.setVisibility(View.INVISIBLE);
		resetChoiceButtonBackground();
		setAnswersAndQuestionInView();
		switchPlayers();	
	}

	private void delayedRoundInitAfterResponseRecieved() {
		Log.d("Comp", "in delayedRoundInitAfterResposeRecieved");
		Thread thread = new Thread() {
		    @Override
		    public void run() {
		        try {
		            Thread.sleep(kCorrectnessStatusShownTime);
		            getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							releaseChoiceButtonsLock();
							initNewRound();
						}
					});
		        } catch (InterruptedException e) {}
		    }
		};
		thread.start();
	}
	
	@Override
	public void onQuestionAndAnswersRecieved(JSONObject json){
		Log.d("Comp", json.toString());
		List<String> jsonKeys = Arrays.asList(SocialGameConstants.RoundKey);
		ServerResponseParser srp = new ServerResponseParser(json, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()) {
			JSONObject roundJson = JSONUtils.getJSONObject(json, SocialGameConstants.RoundKey);
			if(mGameRound == null) {
				mGameRound = new Round(roundJson);
				setAnswersAndQuestionInView();
			} else {
				mGameRound = new Round(roundJson);
			}
		}
	}
	
	@Override
	public void onAnswerResponse(final JSONObject json){
		Log.d("Comp", json.toString());
		List<String> jsonKeys = Arrays.asList(kAnswerStatusResponseKEY);
		ServerResponseParser srp = new ServerResponseParser(json, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()) {
			final String answerStatus = JSONUtils.getStringFromJSON(json, kAnswerStatusResponseKEY, "error getting answerStatus");
			final String playerAnswer = JSONUtils.getStringFromJSON(json, playerAnswerKEY, "error getting playerAnswer");
			final int buttonIndexPressed = findButtonIndex(playerAnswer);
			Log.d("Comp", "buttonIndexPressed was : " + buttonIndexPressed);
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
						initSuccessStatusGraphicsBeforeShow();
						if(answerStatus.equals(kRightResponseKEY)){
							Log.d("Comp", "invoking onCorrectAnswer in onAnswerResponse");
							onCorrectAnswer(buttonIndexPressed);
						} else {
							Log.d("Comp", "invoking onWrongAnswer in onAnswerResponse");
							onWrongAnswer(buttonIndexPressed);
						}
					}
			});
			
			delayedRoundInitAfterResponseRecieved();
		}
	}
	
	private void switchPlayers() {
		Log.d("Comp", "in switchPlayers");
		mCompetitionGame.switchPlayersTurns();
		setCurrentFullNameDisplay();
		((CompetitionActivity)getActivity()).
			OnPlayersSwitchedTurns(mCompetitionGame.getCurrentPlayerNumber());
	}
	
	private void initSuccessStatusGraphicsBeforeShow() {
		Log.d("Comp", "in initSuccessStatusGraphicsBeforeShow");
		mCorrectStatusMessage.setVisibility(View.VISIBLE);
		mCorrectStatusIcon.setVisibility(View.VISIBLE);
	}

	private void onWrongAnswer(final int buttonIndexPressed) {
		mCompetitionActivityNotifier.OnPlayerAnsweredWrong(mCompetitionGame.getCurrentPlayerNumber());
		mCompetitionGame.onWrongAnswer();
		showFailedGraphics(buttonIndexPressed);
	}
	
	private void onCorrectAnswer(int buttonIndexPressed) {
		mCompetitionActivityNotifier.OnPlayerAnsweredCorrect(mCompetitionGame.getCurrentPlayerNumber());
		mCompetitionGame.onRightAnswer();
		showSuccessGraphics(buttonIndexPressed);
	}
	
	private void showSuccessGraphics(int buttonIndexPressed) {
		Log.d("Comp", "in showSuccessGraphics");
		mChoiceButtons[buttonIndexPressed].setBackgroundResource(kGreenBtnShapeRes);
		mCorrectStatusIcon.setImageDrawable(getResources().getDrawable(vIconDrawableRes));
		mCorrectStatusMessage.setText(kCORRECT_MSG);
		
	}
	
	private void showFailedGraphics(int buttonIndexPressed){
		Log.d("Comp", "in showFailedGraphics");
		mChoiceButtons[buttonIndexPressed].setBackgroundResource(kRedBtnShapeRes);
		mCorrectStatusIcon.setImageDrawable(getResources().getDrawable(xIconDrawableRes));
		mCorrectStatusMessage.setText(kWRONG_MSG);
	}

	public void releaseChoiceButtonsLock() {
		Log.d("Comp", "in releaseChoiceButtonsLock");
		for(Button button : mChoiceButtons){
			button.setEnabled(true);
		}
	}
	
	private int findButtonIndex(String playerAnswer) {
		for (int i = 0; i < mChoiceButtons.length; i++) {
			if(mChoiceButtons[i].getText().toString().equals(playerAnswer)){
				return i;
			}
		}
		
		Log.d("Comp", "Returnning 0 as default button index!!!");
		return 0;
	}



	private void resetChoiceButtonBackground() {
		Log.d("Comp", "in resetChoiceButtonBackground");
		for(Button button : mChoiceButtons){
			button.setBackgroundResource(android.R.drawable.btn_default);
		}
	}


	private void setAnswersAndQuestionInView(){
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String[] answers = mGameRound.getmAnswers();
				for (int i = 0; i < mGameRound.getmAnswers().length; i++) {
					if(answers.equals(mGameRound.getmCorrectAnswer())){
						mCorrectButtonIndex = i;
						Log.d("Comp", "correct mCorrectButtonIndex : " + mCorrectButtonIndex);
					}
					mChoiceButtons[i].setText(answers[i]);
				}
				mFullNameTV.setText(mCompetitionGame.getCurrentUser().getFullName());
				mTheWordToTranslate.setText(mGameRound.getmQuestion());
			}
		});
	}

	public void lockChoiceButtonsLock() {
		for(Button button : mChoiceButtons){
			button.setEnabled(false);
		}
	}
	
	public void SetCompetitionGame(CompetitionGame competitionGame) {
		mCompetitionGame = competitionGame;
	}
	

	private void setFonts() {
	    Typeface tf = Typeface.createFromAsset(
	    		getActivity().getAssets(),"fonts/Jura-DemiBold.ttf");
	    mFullNameTV.setTypeface(tf);
	    mTranslateThisTV.setTypeface(tf);
	}

	private void initWidgets(View view) {
		mCorrectStatusMessage = (TextView) view.findViewById(R.id.tvAnswerCorrectMsg);
		mCorrectStatusIcon = (ImageView) view.findViewById(R.id.ivSuccessStatusIcon);
		mFullNameTV = (TextView) view.findViewById(R.id.tvCompFullName);
		mTheWordToTranslate = (TextView) view.findViewById(R.id.tvTheWordToTranslate);
		mTranslateThisTV = (TextView) view.findViewById(R.id.tvTranslateTheWord);
		mChoiceButtons[0] = (Button) view.findViewById(R.id.btnCompetition0);
		mChoiceButtons[1] = (Button) view.findViewById(R.id.btnCompetition1);
		mChoiceButtons[2] = (Button) view.findViewById(R.id.btnCompetition2);
		mChoiceButtons[3] = (Button) view.findViewById(R.id.btnCompetition3);
		
		if((((CompetitionActivity) getActivity()).getUserState())
				.equals(SocialGameConstants.IntentRoomStateVALUEJoined)){
			mChoiceButtons[0].setEnabled(false);
			mChoiceButtons[1].setEnabled(false);
			mChoiceButtons[2].setEnabled(false);
			mChoiceButtons[3].setEnabled(false);
		}
	}

	private void setButtonListeners(View view) {
		mChoiceButtons[0].setOnClickListener(new BtnChoiceClickListener(0));
		mChoiceButtons[1].setOnClickListener(new BtnChoiceClickListener(1));
		mChoiceButtons[2].setOnClickListener(new BtnChoiceClickListener(2));
		mChoiceButtons[3].setOnClickListener(new BtnChoiceClickListener(3));

	}
	

	class BtnChoiceClickListener implements OnClickListener{
		private int buttonIndex;
		public BtnChoiceClickListener(int btnIndex){
			buttonIndex = btnIndex;
		}
		@Override
		public void onClick(View v) {
			handleUserChoice(buttonIndex);
		}
	}
	
	public void setCurrentFullNameDisplay() {
		if(mCompetitionGame.getCurrentPlayerNumber() == PlayerNumber.Player1){
			mFullNameTV.setText(mCompetitionGame.getPlayer1().getUser().getFullName());
		} else {
			mFullNameTV.setText(mCompetitionGame.getPlayer2().getUser().getFullName());
		}	
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCompetitionActivityNotifier = (CompetitionActivity) activity;
	}

	public void InitCompetitionGame(CompetitionGame competitionGame) {
		this.mCompetitionGame = competitionGame;
	}

}
