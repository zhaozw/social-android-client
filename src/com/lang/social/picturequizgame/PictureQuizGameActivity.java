package com.lang.social.picturequizgame;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.file.IOFileCallback;
import com.lang.social.file.IOServerFileFetcher;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.JSONUtils;


public class PictureQuizGameActivity extends Activity implements PictureQuizGameListener {

	private ImageView [] images = new ImageView[4];
	private TextView tvQuestion;
	private TextView tvPlayerName;
	private ProgressDialog progressDialog;
	private ArrayList<String> imagesNames = new ArrayList<String>();
	private String answerFileName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_quiz_game);
		
		findAllViews();

		setImagesTouchListeners();
		
		setTypeFace();
		
		IOCallBackHandler.getInstance().SetPictureQuizGameListener(this);
	
		startNewRound();

	}
	
	private void startNewRound() {
		showProgessDialog();
		ServerController.sendJSONMessage(PictureQuizGameConstants.RoundRequest, null);
	}

	private void findAllViews() {
		images[0] = (ImageView) findViewById(R.id.quizGameImage1);
		images[1] = (ImageView) findViewById(R.id.quizGameImage2);
		images[2] = (ImageView) findViewById(R.id.quizGameImage3);
		images[3] = (ImageView) findViewById(R.id.quizGameImage4);
		tvQuestion = (TextView) findViewById(R.id.tvPicQuizGameQuestion);
		tvPlayerName = (TextView) findViewById(R.id.quizGamePlayerName);
	}

	private class IndexedIOFileCallBack implements IOFileCallback {
		private int imageIndex;
		public IndexedIOFileCallBack(int imageIndex) {
			this.imageIndex = imageIndex;
		}
		@Override
		public void onFileInputStreamRecieved(BufferedInputStream inputStream) {
			Bitmap bp = BitmapFactory.decodeStream(inputStream);
			Log.d(PictureQuizGameConstants.TAG, "decoded image stream from inputstream and index is : " + imageIndex);
			updateImageInView(imageIndex, bp);
		}
	}
	

	@Override
	public void OnRoundRecieved(JSONObject jsonResponse) {
		Log.d(PictureQuizGameConstants.TAG, jsonResponse.toString());
		List<String> jsonKeys = Arrays.asList("result", "question" , "filenames");
		ServerResponseParser srp = new ServerResponseParser(jsonResponse, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()) 
		{
			String question = JSONUtils.getStringFromJSON(jsonResponse, "question", "No question recieved");
			Log.d(PictureQuizGameConstants.TAG, question);
			setQuestionInView(question);
			parseFileNames(jsonResponse);

			for (int i = 0; i < images.length; i++) 
			{
				IOServerFileFetcher fileFetcher = new IOServerFileFetcher(new IndexedIOFileCallBack(i));
				JSONObject json1 = new JSONObject();
				JSONUtils.setStringValue(json1, "routeKey", PictureQuizGameConstants.ImageRequest);
				if(i == 0){
					JSONUtils.setStringValue(json1, "filename", answerFileName);
				} else {
					JSONUtils.setStringValue(json1, "filename", imagesNames.get(i));
				}
				
				Log.d(PictureQuizGameConstants.TAG, "sending to server :" + json1.toString());
				fileFetcher.Get(json1.toString());
			}
		}
	}
		
	protected void parseFileNames(JSONObject jsonResponse) {
		JSONArray filenames = JSONUtils.getJSONArray(jsonResponse, "filenames");
		answerFileName = JSONUtils.getStringFromJSON(JSONUtils.getJSONObject(filenames, 0), "answer", "error parsing answer filename");
		imagesNames.add(answerFileName);
		Log.d(PictureQuizGameConstants.TAG, answerFileName);
		for (int i = 1; i < filenames.length(); i++) {
			String randfilename = JSONUtils.getStringFromJSON(JSONUtils.getJSONObject(filenames, i), "random", "error parsing a random filename");
			imagesNames.add(randfilename);
			Log.d(PictureQuizGameConstants.TAG, randfilename);
		}
	}

	private void setImagesTouchListeners() {
		images[0].setOnTouchListener(new CustomTouchListener(0));
		images[1].setOnTouchListener(new CustomTouchListener(1));
		images[2].setOnTouchListener(new CustomTouchListener(2));
		images[3].setOnTouchListener(new CustomTouchListener(3));
	}

	private void showProgessDialog() {
		progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
		progressDialog.show();
	}

	private void setTypeFace() {
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Roboto-LightItalic.ttf");
		Typeface tf1 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-ThinItalic.ttf");
		Typeface tf2 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
		Typeface tf3 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
		Typeface tf4 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");
		Typeface tf5 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
		Typeface tf6 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-BoldItalic.ttf");
		Typeface tf7 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-BlackItalic.ttf");
		tvQuestion.setTypeface(tf7);
		tvPlayerName.setTypeface(tf);
	}

	private void setQuestionInView(final String question) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvQuestion.setText("Select The : " + " \" " + question + " \" ");
			}
		});	
	}

	private void updateImageInView(final int imageIndex, final Bitmap bitmap) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(imageIndex == images.length - 1) {
					progressDialog.dismiss();
				}
				
				images[imageIndex].setImageBitmap(bitmap);
				Log.d(PictureQuizGameConstants.TAG, "finished setting image bitmap on image index" + imageIndex);
				Log.d(PictureQuizGameConstants.TAG, "the image name at this position is : " + imagesNames.get(imageIndex));
			}
		});
	}
	
	private class CustomTouchListener implements View.OnTouchListener {
		
			private int imageIndex;
			public CustomTouchListener(int imageIndex) {
				this.imageIndex = imageIndex;
			}
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				  switch (event.getAction()) {
	              case MotionEvent.ACTION_DOWN: {
	            	  handlePictureGuessAction(imageIndex);
	                  ImageView view = (ImageView) v;
	                  //overlay is black with transparency of 0x77 (119)
	                  view.getDrawable().setColorFilter(0x77000000,PorterDuff.Mode.SRC_ATOP);
	                  view.invalidate();
	                  break;
	              }
	              case MotionEvent.ACTION_UP:
	              case MotionEvent.ACTION_CANCEL: {
	                  ImageView view = (ImageView) v;
	                  //clear the overlay
	                  view.getDrawable().clearColorFilter();
	                  view.invalidate();
	                  break;
	              }
	          }
	
	          return true;
			}
		}
	

	public void handlePictureGuessAction(int imageIndexPressed) {
		Log.d(PictureQuizGameConstants.TAG, "User guessed an image at index : " + imageIndexPressed);
		Log.d(PictureQuizGameConstants.TAG, "User guessed the image with file name : " + imagesNames.get(imageIndexPressed));

		if(imagesNames.get(imageIndexPressed).equals(answerFileName)){
			Log.d(PictureQuizGameConstants.TAG, "Correct Answer!");
			showWinningGraphics(imageIndexPressed);
		} else {
			Log.d(PictureQuizGameConstants.TAG, "Wrong Answer!");
			showLosingGraphics(imageIndexPressed);
		}
	}

	
	private void showLosingGraphics(int imageIndexPressed) {
		showImageInImageCenter(R.drawable.xicon, imageIndexPressed);
		playSoundEffect(R.raw.wrong1);
		//startNewRoundAfterDelay();
	}

	private void showWinningGraphics(int imageIndexPressed) {
		showImageInImageCenter(R.drawable.vicon, imageIndexPressed);
		playSoundEffect(R.raw.success_low);
		startNewRoundAfterDelay();
	}
	
	private void playSoundEffect(int musicRes) {
		 MediaPlayer mp = MediaPlayer.create(PictureQuizGameActivity.this, musicRes);
         mp.setOnCompletionListener(new OnCompletionListener() {
             @Override
             public void onCompletion(MediaPlayer mp) {
                 mp.release();
             }
         });   
         mp.start();
	}

	private void startNewRoundAfterDelay() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startNewRound();
			}
		}, 3000);
	}

	
	
	public void showImageInImageCenter(int iconInCenterRes ,int imageIndexPressed){
		
		Bitmap backgroundBitmap = ((BitmapDrawable)images[imageIndexPressed].getDrawable()).getBitmap();
	    Bitmap bitmapToDrawInTheCenter = BitmapFactory.decodeResource(getResources(), iconInCenterRes);

	    Bitmap resultBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),backgroundBitmap.getHeight(), backgroundBitmap.getConfig());
	    Canvas canvas = new Canvas(resultBitmap);
	    canvas.drawBitmap(backgroundBitmap, new Matrix(), null);
	    canvas.drawBitmap(bitmapToDrawInTheCenter,
	    		(backgroundBitmap.getWidth() - bitmapToDrawInTheCenter.getWidth()) / 2,
	    		(backgroundBitmap.getHeight() - bitmapToDrawInTheCenter.getHeight()) / 2,
	    		new Paint());

	    images[imageIndexPressed].setImageBitmap(resultBitmap);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}
	


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			return true;
		} else if (itemId == R.id.action_logout) {
			new UserSessionManager(this).logOutUser();
			return true;
		} else if (itemId == R.id.action_settings) {
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}


}
