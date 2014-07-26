package com.lang.social.memorygame;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.crypto.spec.IvParameterSpec;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.competition.CompetitionPlayer;
import com.lang.social.competition.SocialGameConstants;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.utils.ImageUtils;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;
import com.mikhaellopez.circularimageview.CircularImageView;

public class MemoryGameActivity extends Activity implements MemoryGameListener {


	private String mUserState;
	private CircularImageView playerPic;
	private User mPlayer1;
	private User mPlayer2;
	
	private TextView tvPlayerName;
	private ImageView ivLearningLnguageFlag;
	
	Fragment[][] cardBackFrags = new Fragment[4][4];
	Fragment[][] cardFrontFrags = new Fragment[4][4];
	int[][] cardsContainersFragsIds = new int[4][4];
	boolean[][] isCardFragmentTaken = new boolean[4][4];
	private ProgressDialog progressDialog;
	private TextView tvLearningLanguage;
	
	public static final String GameRoundRequestKey = "MemoryGameRoundRequest";
	public static final String GameRoundResponseKey = "MemoryGameRoundResponse";
	public static final String Round = "round";
	public static final String MemoryGameImageAndWordPairKey = "MemoryGameImageAndWordPairKey";
	public static final String MemoryGameImage = "image";
	public static final String MemoryGameWord = "word";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memory_game_activity);

        if (savedInstanceState == null) {
        	initAllCardContainersId();
        	initAllCardFragment();
        }
        
        createProgressDialog();
        
		getViews();
		
		mUserState = getIntent().getStringExtra(SocialGameConstants.IntentRoomStateKEY);
		
		mPlayer1 = (User)getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer1Key);
		mPlayer2 = (User)getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer2Key);
		
		if(mPlayer1.isFacebookUser()){
			setProfilePicture(playerPic, mPlayer1.getProfileID());
		}
		
		tvPlayerName = (TextView) findViewById(R.id.tvPlayerName);
		tvPlayerName.setText(mPlayer1.getFullName());

		tvLearningLanguage.setText(UserController.getUser().getLearningLanguageText());
		ivLearningLnguageFlag.setImageResource(UserController.getFlagImageRes());
		
		setFonts();
		
		getGameRound();
	}
	
	private void setProfilePicture(final CircularImageView iv, final String profileid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				 try {
					 URL MyProfilePicURL = new URL("https://graph.facebook.com/" +  profileid + "/picture?type=large&width=100&height=100");
					 Bitmap mIcon1 = BitmapFactory.decodeStream(MyProfilePicURL.openConnection().getInputStream());
					 onProfileImageRecieved(mIcon1, iv);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void onProfileImageRecieved(final Bitmap icon, final CircularImageView iv) {
		 runOnUiThread(new Runnable() {
			public void run() {
				iv.setImageBitmap(icon);
			}
		});
	}
	
	private void getGameRound() {
		IOCallBackHandler.getInstance().setMemoryGameListener(this);
		ServerController.sendJSONMessage(GameRoundRequestKey, null);
		progressDialog.show();
	}
	
	private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
	}
	
	@Override
	public void OnGameRoundRecieved(JSONObject gameRound) {
		//should recieve 8 json objects
		//each of picture and word fields
		List<String> jsonKeys = Arrays.asList("result", Round);
		ServerResponseParser srp = new ServerResponseParser(gameRound, jsonKeys);
		srp.checkLegalResponseJSON();
		if(srp.isOkResult()) {
			JSONArray imagesAndWordsArr = JSONUtils.getJSONArray(gameRound, Round);
			//need to create 16 fragments from an 8 length json array at randomized location and insert them to cardFrontFrags matrix
			for (int i = 0; i < imagesAndWordsArr.length(); i++) {
				String word = parseWord(JSONUtils.getJSONObject(imagesAndWordsArr, i));
				//Bitmap bitmap = parseImage(JSONUtils.getJSONObject(imagesAndWordsArr, i));
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.house);
				insertWordAndBitmapToFragments(word, bitmap);
			}
			progressDialog.dismiss();		
		} else {
			MyToaster.showToast(MemoryGameActivity.this, "Errror accured", Toast.LENGTH_SHORT);
		}
	}

	private void insertWordAndBitmapToFragments(String word, Bitmap bitmap) {
		insertWord(word);
		insertBitmap(bitmap);
	}

	private void insertBitmap(Bitmap bitmap) {
		boolean bitmapInserted = false;
		Random ran = new Random();
		do {
			int x = ran.nextInt(cardFrontFrags.length);
			int y = ran.nextInt(cardFrontFrags.length);
			if(!isCardFragmentTaken[x][y]) {
				cardFrontFrags[x][y] = new CardFrontImageFragment();
				((CardFrontImageFragment)cardFrontFrags[x][y]).SetRow(x);
				((CardFrontImageFragment)cardFrontFrags[x][y]).SetCol(y);
				Bundle bundle = new Bundle();
				bundle.putParcelable(MemoryGameImage, bitmap);
				((CardFrontImageFragment)cardFrontFrags[x][y]).setArguments(bundle);
				isCardFragmentTaken[x][y] = true;
				bitmapInserted = true;
			}
		} while(!bitmapInserted);
	}

	private void insertWord(String word) {
		boolean WordInserted = false;
		Random ran = new Random();
		do {
			int x = ran.nextInt(cardFrontFrags.length);
			int y = ran.nextInt(cardFrontFrags.length);
			if(!isCardFragmentTaken[x][y]) {
			    cardFrontFrags[x][y] = new CardFrontTextFragment();
				((CardFrontTextFragment)cardFrontFrags[x][y]).SetRow(x);
				((CardFrontTextFragment)cardFrontFrags[x][y]).SetCol(y);
				Bundle bundle = new Bundle();
				bundle.putString(MemoryGameWord, word);
				((CardFrontTextFragment)cardFrontFrags[x][y]).setArguments(bundle);
				isCardFragmentTaken[x][y] = true;
				WordInserted = true;
			}
		} while(!WordInserted);
	}

	private Bitmap parseImage(JSONObject json) {
		return ImageUtils.decodeToImage(
				JSONUtils.getStringFromJSON(json, MemoryGameImage, "error parsing memory game image"));
	}

	private String parseWord(JSONObject json) {
		return JSONUtils.getStringFromJSON(json, MemoryGameWord, "error parsing memory game word");
	}

	private void setFonts() {
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Roboto-LightItalic.ttf");
		Typeface tf1 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-ThinItalic.ttf");
		Typeface tf2 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
		Typeface tf3 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
		Typeface tf4 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");
		tvPlayerName.setTypeface(tf);
		tvLearningLanguage.setTypeface(tf);
	}

	private void getViews() {
		playerPic = (CircularImageView) findViewById(R.id.ivPlayerPicture);
		tvLearningLanguage = (TextView) findViewById(R.id.tvLearningLanguage);
		ivLearningLnguageFlag = (ImageView) findViewById(R.id.ivLearningLnguageFlag);
		//ppvPlayer2 = (ProfilePictureView) findViewById(R.id.ppvCompetitionGuest);
		//tvPlayer1 = (TextView) findViewById(R.id.tvCompetePlayer1);
		//tvPlayer2 = (TextView) findViewById(R.id.tvCompetePlayer2);
		//--------------------------------------------------------------------------------------
/*		mPlayer1HeartsIVs[0] = (ImageView) findViewById(R.id.ivPlayer1Heart1);
		mPlayer1HeartsIVs[1] = (ImageView) findViewById(R.id.ivPlayer1Heart2);
		mPlayer1HeartsIVs[2] = (ImageView) findViewById(R.id.ivPlayer1Heart3);
		mPlayer1HeartsIVs[3] = (ImageView) findViewById(R.id.ivPlayer1Heart4);
		mPlayer1HeartsIVs[4] = (ImageView) findViewById(R.id.ivPlayer1Heart5);
		//--------------------------------------------------------------------------------------
		mPlayer2HeartsIVs[0] = (ImageView) findViewById(R.id.ivPlayer2Heart1);
		mPlayer2HeartsIVs[1] = (ImageView) findViewById(R.id.ivPlayer2Heart2);
		mPlayer2HeartsIVs[2] = (ImageView) findViewById(R.id.ivPlayer2Heart3);
		mPlayer2HeartsIVs[3] = (ImageView) findViewById(R.id.ivPlayer2Heart4);
		mPlayer2HeartsIVs[4] = (ImageView) findViewById(R.id.ivPlayer2Heart5);*/
		//--------------------------------------------------------------------------------------
		//sendChatMsgButton = (Button)findViewById(R.id.ButtonSendChatMessage);
		//chatLine = (EditText)findViewById(R.id.EditTextChatLine);
		//listView = (ListView)findViewById(R.id.left_drawer_chat_listView);
		
	}


	private void initAllCardContainersId() {
		cardsContainersFragsIds[0][0] = R.id.card00;
		cardsContainersFragsIds[0][1] = R.id.card01;
		cardsContainersFragsIds[0][2] = R.id.card02;
		cardsContainersFragsIds[0][3] = R.id.card03;
		cardsContainersFragsIds[1][0] = R.id.card10;
		cardsContainersFragsIds[1][1] = R.id.card11;
		cardsContainersFragsIds[1][2] = R.id.card12;
		cardsContainersFragsIds[1][3] = R.id.card13;
		cardsContainersFragsIds[2][0] = R.id.card20;
		cardsContainersFragsIds[2][1] = R.id.card21;
		cardsContainersFragsIds[2][2] = R.id.card22;
		cardsContainersFragsIds[2][3] = R.id.card23;
		cardsContainersFragsIds[3][0] = R.id.card30;
		cardsContainersFragsIds[3][1] = R.id.card31;
		cardsContainersFragsIds[3][2] = R.id.card32;
		cardsContainersFragsIds[3][3] = R.id.card33;

	}
	
	private void flipCard(int i, int j) {
		if(!isCardFragmentTaken[i][j]) 
			return;
	    getFragmentManager()
	            .beginTransaction()
	            .setCustomAnimations(
	                    R.anim.card_flip_right_in, R.anim.card_flip_right_out,
	                    R.anim.card_flip_left_in, R.anim.card_flip_left_out)
	            .replace(cardsContainersFragsIds[i][j], cardFrontFrags[i][j])
	            .commit();
	}

	private void initAllCardFragment() {
		for (int i = 0; i < cardBackFrags.length; i++) {
			cardBackFrags[i] = new Fragment[4];
			cardFrontFrags[i] = new Fragment[4];
		}

		for (int i = 0; i < cardBackFrags.length; i++) {
			for (int j = 0; j < cardBackFrags[i].length; j++) {
				cardBackFrags[i][j] = new CardBackFragment();
				((CardBackFragment)cardBackFrags[i][j]).SetRow(i);
				((CardBackFragment)cardBackFrags[i][j]).SetCol(j);
			}
		}

		for (int i = 0; i < cardBackFrags.length; i++) {
			for (int j = 0; j < cardBackFrags[i].length; j++) {
		      getFragmentManager()
                .beginTransaction()
                .add(cardsContainersFragsIds[i][j], cardBackFrags[i][j])
                .commit();
			}
		}		
	}

	@Override
	public void OnCardClick(int row, int col) {
		Toast.makeText(this, row + "  " + col , Toast.LENGTH_SHORT).show();	
		flipCard(row, col);
	}


}
