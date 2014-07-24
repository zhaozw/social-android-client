package com.lang.social.quizgame;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.hangman.HangmanGameActivity;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;

public class QuizActivity extends Activity implements QuizActivityListener {
	
	private static final int MAX_QUESTIONS = 10;
	private static final int USER_HEARTS = 3;
	private static final int MAX_USER_HEARTS = 5;
	private static final int CORRECT_ANSWER_POINTS = 20;
	private static final String TAG = "QUIZ";
	
	//REQUESTS
	//-----------------------------------------------------------
	
	//-----------------------------------------------------------
	
	private Question CurrQuestion;
	private String CurrUserAnswer;
	private int QuestionCounter = 0;
	private int UserPoints = 0;
	private int UserHeartsIndex = USER_HEARTS - 1;	//-1 For array indexing
	private User m_CurrentUser;
	
	
	private Button option1BT; 
	private Button option2BT;
	private Button option3BT;
	private Button option4BT;
	private TextView tvQuestionCounter;
	private TextView tvAddPointsLabel;	//make left animation
	private TextView tvPointsEarned;
	private TextView tvUserName;
	private TextView tvWordToTranslate;
	private ImageView ivSuccessIcon;
	private ImageView ivNextQuestionIcon;
	
	private ImageView[] HeartArrays = new ImageView[MAX_USER_HEARTS];
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_layout);
		
		//Action Bar Initialization
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle(" Quiz");
        getActionBar().setIcon(getResources().getDrawable(R.drawable.selflearnning));
        //--------------------------------------------------------------------------------------------------
        
        //Views Initialization
        setViews();
        setUserDetails();
		setRelevantViewsTextAndApeerances();
		//--------------------------------------------------------------------------------------------------
		
		setContinueButtonListener();
		
		//Server Listener Initalization
		setServerResponeListener();
 		//-------------------------------------------------------------------------------------------------
	}
	
	@Override
	protected void onStart() {
		super.onStart();
 		startNewQuizGameToServer();
	}
	
	private void startNewQuizGameToServer() {
		Log.d(TAG, "sending sendStartNewQuizGame");
		JSONObject jsonToSend = new JSONObject();
		ServerController.sendJSONMessage("startNewQuizGame", jsonToSend);
		
	}

	private void setRelevantViewsTextAndApeerances() {
		tvAddPointsLabel.setVisibility(View.GONE);
		option1BT.setVisibility(View.GONE);
		option2BT.setVisibility(View.GONE);
		option3BT.setVisibility(View.GONE);
		option4BT.setVisibility(View.GONE);
	}

	private void setViews() {
		tvUserName = (TextView) findViewById(R.id.tvQuizFullName);
		tvQuestionCounter = (TextView) findViewById(R.id.tvQuestionNumber);
		tvAddPointsLabel = (TextView) findViewById(R.id.tvCorrectAnswerAddPointsLabel);
		tvPointsEarned = (TextView) findViewById(R.id.tvPointsEarned);
		tvWordToTranslate = (TextView) findViewById(R.id.tvTheWordToTranslate);
		ivSuccessIcon = (ImageView) findViewById(R.id.ivSuccessStatusIcon);
		ivNextQuestionIcon = (ImageView) findViewById(R.id.ivNextQuestionIcon);
		
		HeartArrays[0] = (ImageView) findViewById(R.id.ivPlayer1Heart1);
		HeartArrays[1] = (ImageView) findViewById(R.id.ivPlayer1Heart2);
		HeartArrays[2] = (ImageView) findViewById(R.id.ivPlayer1Heart3);
		HeartArrays[3] = (ImageView) findViewById(R.id.ivPlayer1Heart4);
		HeartArrays[4] = (ImageView) findViewById(R.id.ivPlayer1Heart5);
		
		//Decided player will only have 3 hearts
		HeartArrays[3].setVisibility(View.GONE);
		HeartArrays[4].setVisibility(View.GONE);
		
		option1BT = (Button) findViewById(R.id.btnQuiz1);
		option2BT = (Button) findViewById(R.id.btnQuiz2);
		option3BT = (Button) findViewById(R.id.btnQuiz3);
		option4BT = (Button) findViewById(R.id.btnQuiz4);
		
	}

	private void setUserDetails() {
		m_CurrentUser = UserController.getUser();
		ProfilePictureView ppv = (ProfilePictureView)findViewById(R.id.ppvQuizPlayer);
		if(m_CurrentUser.getProfileID() != null){
		    ppv.setProfileId(m_CurrentUser.getProfileID());
		}
		
		TextView tvPlayerName = (TextView) findViewById(R.id.tvQuizPlayer);
		tvPlayerName.setText(m_CurrentUser.getFullName());
		tvUserName.setText(m_CurrentUser.getFullName() + ",");
		
		ImageView ivUserFlag = (ImageView) findViewById(R.id.ivFlagPlayer);
		ivUserFlag.setImageResource(m_CurrentUser.getFlagImageRes());
		
	}

	private void sendAnswerLastQuestionRequest(String answer) {
		Log.d(TAG, "sending sendAnswerLastQuestionRequest");
		JSONObject jsonToSend = JSONUtils.CreateJSON("answer", answer);
		ServerController.sendJSONMessage("answerLastQuestion", jsonToSend);
	}
	
	
	private void setContinueButtonListener()
	{
		ivNextQuestionIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(QuestionCounter == MAX_QUESTIONS)
				{
					ivNextQuestionIcon.setImageDrawable(getResources().getDrawable(R.drawable.finishquizicon));
					ivNextQuestionIcon.setOnClickListener(null);
					ivNextQuestionIcon.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ivNextQuestionIcon.setVisibility(View.GONE);
							endGame();
						}
					});
					
				}
				else
				{
					requestQuestionFromServer();
					ivNextQuestionIcon.setVisibility(View.GONE);
					ivSuccessIcon.setVisibility(View.GONE);
				}
			}
			
		});
	}
	
	private void endGame() {
		JSONObject jsonToSend = new JSONObject();
		try {
			jsonToSend.put("points", UserPoints);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServerController.sendJSONMessage("updatePointsRequest", jsonToSend);
		  // Display Alert Dialog
		  AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
		  winBuild.setTitle("Game Over");
		  winBuild.setMessage("You Have Earned " + UserPoints + " Points!");
		  winBuild.setPositiveButton("Play Again",
		    new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int id) {
		    	  finish();
		    	  startActivity(getIntent());
		    }});
		 
		  winBuild.setNegativeButton("Exit",
		    new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int id) {
		    	  finish();
		    }});
		 
		  winBuild.show();
	}
	
	private void updateQuestionCounter()
	{
		QuestionCounter++;
		tvQuestionCounter.setText(QuestionCounter + " / 10" );
	}
	
	private void enableAnswersClicking()
	{
		option1BT.setClickable(true);
		option2BT.setClickable(true);
		option3BT.setClickable(true);
		option4BT.setClickable(true);
	}
	
	private void setServerResponeListener() {
		Log.d(TAG, "setting setServerResponeListener");
		IOCallBackHandler.getInstance().setQuizActivityListener(this);
	}

	private void requestQuestionFromServer()
	{
		Log.d(TAG, "sending requestQuestionFromServer");
		ServerController.sendJSONMessage("getQuizQuestion", new JSONObject());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}

	private void showQuestionToUser() {
		Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
		animation.setDuration(600);
		tvWordToTranslate.setText(CurrQuestion.GetQuestion());
		tvWordToTranslate.startAnimation(animation);
		option1BT.setBackgroundResource(android.R.drawable.btn_default);
		option2BT.setBackgroundResource(android.R.drawable.btn_default);
		option3BT.setBackgroundResource(android.R.drawable.btn_default);
		option4BT.setBackgroundResource(android.R.drawable.btn_default);

		option1BT.setText(CurrQuestion.GetPossibleAnswerByIndex(0));
		option2BT.setText(CurrQuestion.GetPossibleAnswerByIndex(1));
		option3BT.setText(CurrQuestion.GetPossibleAnswerByIndex(2));
		option4BT.setText(CurrQuestion.GetPossibleAnswerByIndex(3));
		
		option1BT.setVisibility(View.VISIBLE);
		option2BT.setVisibility(View.VISIBLE);
		option3BT.setVisibility(View.VISIBLE);
		option4BT.setVisibility(View.VISIBLE);
		
		option1BT.startAnimation(animation);
		option2BT.startAnimation(animation);
		option3BT.startAnimation(animation);
		option4BT.startAnimation(animation);
		
		setOnUserSelectsAnswerClick();
		
		}

	private void setOnUserSelectsAnswerClick() {
		initListeners();
	}
	
	private void initListeners() {
		option1BT.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CurrUserAnswer = option1BT.getText().toString();
				sendAnswerLastQuestionRequest(CurrUserAnswer);
				runOnUiThread(new Runnable() {
				     @Override
				     public void run() {
				    	 disableClickingAnswers();
				    }
				});	
			}
		});
		
		option2BT.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CurrUserAnswer = option2BT.getText().toString();
				sendAnswerLastQuestionRequest(CurrUserAnswer);
				runOnUiThread(new Runnable() {
				     @Override
				     public void run() {
				    	 disableClickingAnswers();
				    }
				});
				
			}
		});
		
		option3BT.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CurrUserAnswer = option3BT.getText().toString();
				sendAnswerLastQuestionRequest(CurrUserAnswer);
				runOnUiThread(new Runnable() {
				     @Override
				     public void run() {
				    	 disableClickingAnswers();
				    }
				});
			}
		});
		
		option4BT.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CurrUserAnswer = option4BT.getText().toString();
				sendAnswerLastQuestionRequest(CurrUserAnswer);
				runOnUiThread(new Runnable() {
				     @Override
				     public void run() {
				    	 disableClickingAnswers();
				    }
				});
			}
		});		
	}
	
	private void disableClickingAnswers() {
		option1BT.setClickable(false);
		option2BT.setClickable(false);
		option3BT.setClickable(false);
		option4BT.setClickable(false);
		
	}

	private void showCorrectAnswer() {
		switch(CurrQuestion.GetCorrectAnswerIndex())
		{
		case 0:
			option1BT.setBackgroundResource(R.drawable.custom_green_btn_shape);
			option2BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option3BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option4BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option1BT.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			break;
		case 1:
			option2BT.setBackgroundResource(R.drawable.custom_green_btn_shape);
			option1BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option3BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option4BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option2BT.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			break;
		case 2:
			option3BT.setBackgroundResource(R.drawable.custom_green_btn_shape);
			option2BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option1BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option4BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option3BT.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			break;
		case 4:
			option4BT.setBackgroundResource(R.drawable.custom_green_btn_shape);
			option2BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option3BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option1BT.setBackgroundResource(R.drawable.custom_red_btn_shape);
			option4BT.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			break;
		}
		
	}

	@Override
	public void onQuestionRecived(JSONObject obj) {
		Log.d(TAG, "onQuestionRecived");
		String[] possibleAnswers = new String[4];
		try {
			//String result = obj.getString("result");
			String question = obj.getString("Q");
			String A1 = obj.getString("A1");
			String A2 = obj.getString("A2");
			String A3 = obj.getString("A3");
			String A4 = obj.getString("A4");
			possibleAnswers[0] = A1;
			possibleAnswers[1] = A2;
			possibleAnswers[2] = A3;
			possibleAnswers[3] = A4;
			CurrQuestion = new Question(question, possibleAnswers);
		} catch (JSONException e) {
			throw new RuntimeException();
		}
		
		runOnUiThread(new Runnable() {
		     @Override
		     public void run() {
		 		showQuestionToUser();
				enableAnswersClicking();
				updateQuestionCounter();
		    }
		});
	}

	@Override
	public void onAnswerRecived(JSONObject obj) {
		Log.d(TAG, "onAnswerRecived");
		try {
			boolean correct = obj.getBoolean("correct");
			String answer = obj.getString("correctAnswer");
			
			Log.d(TAG, "correct = " + correct);
			Log.d(TAG, "correctAnswer = " + answer);
			
			if(CurrQuestion != null) {
				updateCurrAnswerIndex(answer);
			}
			runOnUiThread(new Runnable() {
			     @Override
			     public void run() {
			    	 checkIfUserCorrected();
			    }
			});
		} catch (JSONException e) {
			throw new RuntimeException();
		}
	}
	
	private void updateCurrAnswerIndex(String answer)
	{
		int i;
		for (i = 0; i < 4; i++) {
			if (CurrQuestion.GetPossibleAnswers()[i].equals(answer) == true)
			{
				CurrQuestion.setCurrentAnswerIndex(i);
				break;
			}
		}
	}

	private void checkIfUserCorrected() {
		Log.d(TAG, CurrQuestion.GetPossibleAnswers()[CurrQuestion.GetCorrectAnswerIndex()] + " ? = " + CurrUserAnswer);
		Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
		animation.setDuration(600);
		if(CurrQuestion.GetPossibleAnswers()[CurrQuestion.GetCorrectAnswerIndex()].equals(CurrUserAnswer) == true) {
			UserPoints += CORRECT_ANSWER_POINTS;
			tvPointsEarned.setText("" + UserPoints);
			tvAddPointsLabel.setVisibility(View.VISIBLE);
			tvAddPointsLabel.startAnimation(animation);
			
			ivSuccessIcon.setImageDrawable(getResources().getDrawable(R.drawable.vicon));
			ivSuccessIcon.setVisibility(View.VISIBLE);
			ivSuccessIcon.startAnimation(animation);
			executeInDelay(4000, tvAddPointsLabel);
		}
		else {
			ivSuccessIcon.setImageDrawable(getResources().getDrawable(R.drawable.xicon));
			ivSuccessIcon.setVisibility(View.VISIBLE);
			if(UserHeartsIndex >= 0) {
				HeartArrays[UserHeartsIndex].setVisibility(View.GONE);
				UserHeartsIndex--;
				if(UserHeartsIndex < 0) {
					ivNextQuestionIcon.setVisibility(View.GONE);
					endGame();
					return;
				}
			}
		}
		ivNextQuestionIcon.setVisibility(View.VISIBLE);
		ivNextQuestionIcon.setAnimation(animation);
//		ivNextQuestionIcon.setOnClickListener(new View.OnClickListener() {	
//			@Override
//			public void onClick(View v) {
//				if(QuestionCounter != MAX_QUESTIONS) {
//					requestQuestionFromServer();
//					ivNextQuestionIcon.setVisibility(View.GONE);
//				}
//				else {
//					ivNextQuestionIcon.
//				}
//				
//				
//				ivSuccessIcon.setVisibility(View.GONE);
//			}
//		});
		showCorrectAnswer();	
	}
	
	 private void executeInDelay(int delayLength, final TextView PointsAdded) {
		  new Handler().postDelayed(new Runnable() {
		     @Override
		     public void run() {
		    	 PointsAdded.setVisibility(View.INVISIBLE);
		     }
		  }, delayLength);
	 }

	@Override
	public void onStartNewQuizGameResponse(JSONObject obj) {
		Log.d(TAG, "In onStartNewQuizGameResponse");
		String result = JSONUtils.getStringFromJSON(obj, "result", null);
		if(result.equals("OK")) {
			requestQuestionFromServer();
		}
		else {
			Log.d(TAG, "Failed To Start New Quiz Game");
		}
		
	}	
}
