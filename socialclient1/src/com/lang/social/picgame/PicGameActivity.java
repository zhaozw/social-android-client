package com.lang.social.picgame;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lang.social.R;


public class PicGameActivity extends Activity implements OnClickListener{

	private List<JSONObject> jsonList;
	private List<ImageQuestion> questionsList;
	private ImageQuestion currentQuestionAsked;
	
	
	private Button buttonOptionBack;
	private Button buttonOption1;
	private Button buttonOption2;
	private Button buttonOption3;
	private Button buttonOption4;
	private Button buttonOptionPass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_game);
		
		buttonOptionBack = (Button)findViewById(R.id.btnBack);
		buttonOption1 = (Button)findViewById(R.id.btnOption1);
		buttonOption2 = (Button)findViewById(R.id.btnOption2);
		buttonOption3 = (Button)findViewById(R.id.btnOption3);
		buttonOption4 = (Button)findViewById(R.id.btnOption4);
		buttonOptionPass = (Button)findViewById(R.id.btnOptionPass);
		
		setListeners();
		
	}

	private void setListeners() {
		/*
		buttonOption1.setOnClickListener(getButtonOptionListener());
		buttonOption2.setOnClickListener(getButtonOptionListener());
		buttonOption3.setOnClickListener(getButtonOptionListener());
		buttonOption4.setOnClickListener(getButtonOptionListener());
		buttonOptionPass.setOnClickListener(getButtonOptionListener());
		*/
		buttonOptionBack.setOnClickListener(this);
		buttonOption1.setOnClickListener(this);
		buttonOption2.setOnClickListener(this);
		buttonOption3.setOnClickListener(this);
		buttonOption4.setOnClickListener(this);
		buttonOptionPass.setOnClickListener(this);
	}

	/*
	private OnClickListener getButtonOptionListener() {
		View.OnClickListener ret = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
						
			}
		};
		return ret;
	
	}*/

	
	private ImageView ImageFromJson()
	{
		return null;
	}
	
	protected class ImageQuestion
	{
		private String string1;
		private String string2;
		private String string3;
		private String string4;
		private String answer;
		
		private ImageView questionImage;
		
		public ImageQuestion(JSONObject init)
		{
			//setting the question from the json
		}
		
		public String getAnswer()
		{
			return answer;
		}
				
	}

	@Override
	public void onClick(View v) {
		int targetID = v.getId();
		Log.i("app", "answer button was clicked " + targetID);
		switch(targetID)
		{
			case(R.id.btnOptionPass):
			{
				Toast.makeText(getApplicationContext(), "option1", Toast.LENGTH_SHORT).show();
				passToNextQuestion();
			}	
			case(R.id.btnBack):
			{
				Toast.makeText(getApplicationContext(), "option2", Toast.LENGTH_SHORT).show();
				this.onBackPressed();
			}
			default:
			{
				Toast.makeText(getApplicationContext(), "check", Toast.LENGTH_SHORT).show();
				checkAnswer(((Button)v).getText().toString());
			}
				
		}
		
		
	}

	private void checkAnswer(String init_answer) { 
		if(true /*init_answer.equals(currentQuestionAsked.getAnswer())*/)
		{
			//update points
			passToNextQuestion();
		}
		else
		{
			//show the current
			//passToNextQuestion();
		}
		
	}

	private void passToNextQuestion() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "next", Toast.LENGTH_SHORT).show();
	}


	
	
}
