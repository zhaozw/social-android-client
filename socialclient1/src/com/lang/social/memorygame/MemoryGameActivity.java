package com.lang.social.memorygame;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.competition.CompetitionPlayer;
import com.lang.social.competition.SocialGameConstants;
import com.lang.social.logic.User;

public class MemoryGameActivity extends Activity implements MemoryGameListener {

	Fragment[][] cardFrags = new Fragment[4][4];
	int[][] cardsFragsIds = new int[4][4];
	boolean[][] isCardFragmentTaken = new boolean[4][4];
	private String mUserState;
	private ProfilePictureView ppv;
	private CompetitionPlayer mPlayer1;
	private CompetitionPlayer mPlayer2;
	private TextView tvPlayerName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memory_game_activity);

        if (savedInstanceState == null) {
        	initAllCardContainersId();
        	initAllCardFragment();
        }
        
		getViews();
		
		mUserState = getIntent().getStringExtra(SocialGameConstants.IntentRoomStateKEY);
		
		User user1 = (User)getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer1Key);
		mPlayer1 = new CompetitionPlayer(user1);
		User user2 = (User)getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer2Key);
		mPlayer2 = new CompetitionPlayer(user2);
		
		if(mPlayer1.getUser().isFacebookUser()){
			ppv.setProfileId(mPlayer1.getUser().getProfileID());
		}
		
		tvPlayerName = (TextView) findViewById(R.id.tvPlayerName);
		tvPlayerName.setText(mPlayer1.getUser().getFullName());

		setFonts();
	}
	
	private void setFonts() {
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Roboto-LightItalic.ttf");
		Typeface tf1 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-ThinItalic.ttf");
		Typeface tf2 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
		Typeface tf3 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
		Typeface tf4 = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Thin.ttf");

		tvPlayerName.setTypeface(tf);

	}

	private void getViews() {
		ppv = (ProfilePictureView) findViewById(R.id.ppvProfilePic);
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
		cardsFragsIds[0][0] = R.id.card00;
		cardsFragsIds[0][1] = R.id.card01;
		cardsFragsIds[0][2] = R.id.card02;
		cardsFragsIds[0][3] = R.id.card03;
		cardsFragsIds[1][0] = R.id.card10;
		cardsFragsIds[1][1] = R.id.card11;
		cardsFragsIds[1][2] = R.id.card12;
		cardsFragsIds[1][3] = R.id.card13;
		cardsFragsIds[2][0] = R.id.card20;
		cardsFragsIds[2][1] = R.id.card21;
		cardsFragsIds[2][2] = R.id.card22;
		cardsFragsIds[2][3] = R.id.card23;
		cardsFragsIds[3][0] = R.id.card30;
		cardsFragsIds[3][1] = R.id.card31;
		cardsFragsIds[3][2] = R.id.card32;
		cardsFragsIds[3][3] = R.id.card33;

	}
	
	private void flipCard(int i, int j) {
	    cardFrags[i][j] = new CardBackFragment();
		((CardBackFragment)cardFrags[i][j]).SetRow(i);
		((CardBackFragment)cardFrags[i][j]).SetCol(j);
		
	    getFragmentManager()
	            .beginTransaction()
	            .setCustomAnimations(
	                    R.anim.card_flip_right_in, R.anim.card_flip_right_out,
	                    R.anim.card_flip_left_in, R.anim.card_flip_left_out)
	            .replace(cardsFragsIds[i][j], cardFrags[i][j])
	            .commit();
	}

	private void initAllCardFragment() {
		for (int i = 0; i < cardFrags.length; i++) {
			cardFrags[i] = new Fragment[4];
		}

		for (int i = 0; i < cardFrags.length; i++) {
			for (int j = 0; j < cardFrags[i].length; j++) {
				cardFrags[i][j] = new CardBackFragment();
				((CardBackFragment)cardFrags[i][j]).SetRow(i);
				((CardBackFragment)cardFrags[i][j]).SetCol(j);
			}
		}

		for (int i = 0; i < cardFrags.length; i++) {
			for (int j = 0; j < cardFrags[i].length; j++) {
		      getFragmentManager()
                .beginTransaction()
                .add(cardsFragsIds[i][j], cardFrags[i][j])
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
