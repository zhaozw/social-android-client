package com.lang.social.memorygame;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lang.social.R;
import com.lang.social.chat.ChatLineItem;
import com.lang.social.chat.ChatListener;
import com.lang.social.chat.CustomChatAdapter;
import com.lang.social.competition.SocialGameConstants;
import com.lang.social.controllers.ServerController;
import com.lang.social.interfaces.GameCloseListener;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.GameType;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.room.RoomActivity;
import com.lang.social.room.RoomConstants;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.ImageUtils;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;
import com.mikhaellopez.circularimageview.CircularImageView;

public class MemoryGameActivity extends Activity 
	implements MemoryGameListener, ChatListener, GameCloseListener {


	private String mUserState;
	private CircularImageView playerPic;
	private User mPlayer1;
	private User mPlayer2;
	
	private TextView tvPlayerName;
	private ImageView ivLearningLnguageFlag;
	
	Fragment[][] cardBackFrags = new Fragment[4][4];
	CardFrontFragment[][] cardFrontFrags = new CardFrontFragment[4][4];
	int[][] cardsContainersFragsIds = new int[4][4];
	boolean[][] isCardFragmentTaken = new boolean[4][4];
	private ProgressDialog progressDialog;
	private TextView tvLearningLanguage;
	
	
	private EditText chatLine;
	private Button sendChatMsgButton;
	//private ArrayList<String> listItems = new ArrayList<String>();
	private ArrayList<ChatLineItem> listItems = new ArrayList<ChatLineItem>();
	//private ArrayAdapter<String> adapter;
	private CustomChatAdapter adapter;
	private ListView listView;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	public static final String GameRoundRequestKey = "MemoryGameRoundRequest";
	public static final String GameRoundResponseKey = "MemoryGameRoundResponse";
	public static final String Round = "round";
	public static final String MemoryGameImageAndWordPairKey = "MemoryGameImageAndWordPairKey";
	public static final String MemoryGameImage = "image";
	public static final String MemoryGameWord = "word";
	
	
	private enum NumOfCardsShown {
		Zero,
		One,
		Two
	}
	
	private Point pointPressedFirst;
	private Point pointPressedSecond;
	private NumOfCardsShown currentNumOfCardsShown;
	
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
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(getResources().getDrawable(R.drawable.chaticon));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("Chat");
		
		mUserState = getIntent().getStringExtra(SocialGameConstants.IntentRoomStateKEY);
		
		mPlayer1 = (User)getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer1Key);
		mPlayer2 = (User)getIntent().getSerializableExtra(SocialGameConstants.IntentPlayer2Key);

		if(mUserState.equals(SocialGameConstants.IntentRoomStateVALUECreated)){
			if(mPlayer1.isFacebookUser()) {
				setProfilePicture(playerPic, mPlayer1.getProfileID());
			}			
		} else {
			if(mPlayer2.isFacebookUser()) {
				setProfilePicture(playerPic, mPlayer2.getProfileID());
			}
		}

		tvPlayerName = (TextView) findViewById(R.id.tvPlayerName);
		tvPlayerName.setText(mPlayer1.getFullName());

		tvLearningLanguage.setText(UserController.getUser().getLearningLanguageText());
		ivLearningLnguageFlag.setImageResource(UserController.getFlagImageRes());
		
		setFonts();
		
		getGameRound();
		
		initializeNavigationDrawer();
		
		setButtonChatListener();
	}
	
	private void initializeNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_chat);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_chat_listView);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
		adapter = new CustomChatAdapter(this, R.layout.custom_chat_item , listItems);
    	mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            	InputMethodManager imm = (InputMethodManager)getSystemService(
            	      Context.INPUT_METHOD_SERVICE);
            	imm.hideSoftInputFromWindow(sendChatMsgButton.getWindowToken(), 0);
            	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()     
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
            	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
		
	}


	private void setButtonChatListener() {
		sendChatMsgButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String msgToSend = chatLine.getText().toString();
				JSONObject json = new JSONObject();
				chatLine.setText(" ");
				JSONUtils.setStringValue(json, "message", msgToSend);
				JSONUtils.setStringValue(json, "sender", UserController.getUser().getFullName());
				JSONUtils.setStringValue(json, "type",  "userMessage");
				JSONUtils.setStringValue(json, "GameType",  GameType.MemoryGame.toString());
				ServerController.sendJSONMessage("chatMessage", json);
			}
		});
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
				//Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.house);
				
				//change! just for testing
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.house);
				insertWordAndBitmapToFragments(word, bitmap, i); 
			}
		} else {
			MyToaster.showToast(MemoryGameActivity.this, "Errror accured", Toast.LENGTH_SHORT);
		}
	}

	private void insertWordAndBitmapToFragments(final String word, final Bitmap bitmap, final int cardPairId) {
		Thread insertWordThread = new Thread(new Runnable() {
			@Override
			public void run() {
				insertWord(word, cardPairId);	
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progressDialog.dismiss();
					}
				});
			}
		});
		
		insertWordThread.start();
		
		
		Thread insertBitmapThread = new Thread(new Runnable() {
			@Override
			public void run() {
				insertBitmap(bitmap, cardPairId);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progressDialog.dismiss();
					}
				});
			}
		});
		
		insertWordThread.start();
		insertBitmapThread.start();
	}

	private void insertBitmap(final Bitmap bitmap, final int cardPairId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean bitmapInserted = false;
				Random ran = new Random();
				do {
					int x = ran.nextInt(cardFrontFrags.length);
					int y = ran.nextInt(cardFrontFrags.length);
					if(!isCardFragmentTaken[x][y]) {
						cardFrontFrags[x][y] = new CardFrontImageFragment();
						cardFrontFrags[x][y].SetRow(x);
						cardFrontFrags[x][y].SetCol(y);
						Bundle bundle = new Bundle();
						bundle.putParcelable(MemoryGameImage, bitmap);
						cardFrontFrags[x][y].setArguments(bundle);
						
						cardFrontFrags[x][y].SetCardPairId(cardPairId);
						
						isCardFragmentTaken[x][y] = true;
						bitmapInserted = true;
					}
				} while(!bitmapInserted);
			}
		}).start();
	}

	private void insertWord(final String word, final int cardPairId) {
		boolean WordInserted = false;
		Random ran = new Random();
		do {
			int x = ran.nextInt(cardFrontFrags.length);
			int y = ran.nextInt(cardFrontFrags.length);
			if(!isCardFragmentTaken[x][y]) {
			    cardFrontFrags[x][y] = new CardFrontTextFragment();
				cardFrontFrags[x][y].SetRow(x);
				cardFrontFrags[x][y].SetCol(y);
				Bundle bundle = new Bundle();
				bundle.putString(MemoryGameWord, word);
				cardFrontFrags[x][y].setArguments(bundle);
				
				cardFrontFrags[x][y].SetCardPairId(cardPairId);
				
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
		
		
		sendChatMsgButton = (Button)findViewById(R.id.ButtonSendChatMessage);
		chatLine = (EditText)findViewById(R.id.EditTextChatLine);
		listView = (ListView)findViewById(R.id.left_drawer_chat_listView);
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
	
	private void flipCardToFront(int i, int j) {
		if(!isCardFragmentTaken[i][j]) {
			return;
		}
	    getFragmentManager()
	            .beginTransaction()
	            .setCustomAnimations(
	                    R.anim.card_flip_right_in, R.anim.card_flip_right_out,
	                    R.anim.card_flip_left_in, R.anim.card_flip_left_out)
	            .replace(cardsContainersFragsIds[i][j], cardFrontFrags[i][j])
	            .commit();
	}
	
	private void flipCardToBack(int i, int j) {
		if(!isCardFragmentTaken[i][j]) {
			return;
		}
	    getFragmentManager()
	            .beginTransaction()
	            .setCustomAnimations(
	                    R.anim.card_flip_right_in, R.anim.card_flip_right_out,
	                    R.anim.card_flip_left_in, R.anim.card_flip_left_out)
	            .replace(cardsContainersFragsIds[i][j], cardBackFrags[i][j])
	            .commit();
	}

	private void initAllCardFragment() {
		for (int i = 0; i < cardBackFrags.length; i++) {
			cardBackFrags[i] = new Fragment[4];
			cardFrontFrags[i] = new CardFrontFragment[4];
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
	public void OnCardClick(final int row, final int col) {
		Log.d("MemoryGame", "row: " + row + " col: " + col);
	    if(currentNumOfCardsShown == NumOfCardsShown.Zero){
	    	pointPressedFirst = new Point(row, col);
	    	currentNumOfCardsShown = NumOfCardsShown.One;
	    	flipCardToFront(row, col);
	    } else if(currentNumOfCardsShown == NumOfCardsShown.One){
	    	pointPressedSecond = new Point(row, col);
	    	currentNumOfCardsShown = NumOfCardsShown.Two;
	    	flipCardToFront(row, col);
	    	
	    	if(CardsMatch()){
	    		MyToaster.showToast(MemoryGameActivity.this, "Match!", Toast.LENGTH_SHORT);
	    	} else{
	    		MyToaster.showToast(MemoryGameActivity.this, "Dont Match!", Toast.LENGTH_SHORT);
	    		new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						flipCardToBack(row, col);
					}
				}, Toast.LENGTH_SHORT);
	    	}
	    	
	    } else {
	    	///
	    }
	}

	private boolean CardsMatch() {
		//if user pressed the same point twice!
/*		if(pointPressedFirst.getX() == pointPressedSecond.getX() 
				&& pointPressedFirst.getY() == pointPressedSecond.getY()){
			return false;
		}*/
		
		//if the pair id of both fragments match
		if(cardFrontFrags[pointPressedFirst.getX()][pointPressedFirst.getY()].GetCardPairId() ==
				cardFrontFrags[pointPressedSecond.getX()][pointPressedSecond.getY()].GetCardPairId()) {
			return true;
		}
		
		return false;
	}

	private void notifyHostAboutGuestLeave() {
		JSONObject jsonToSend = new JSONObject();
		JSONUtils.setStringValue(jsonToSend, RoomConstants.GameTypeKEY, GameType.MemoryGame.toString());
		ServerController.sendJSONMessage(RoomConstants.GuestQuitGameNotification, jsonToSend);
	}

	private void closeGameNotification() {
		JSONObject jsonToSend = new JSONObject();
		JSONUtils.setStringValue(jsonToSend, RoomConstants.GameTypeKEY, GameType.MemoryGame.toString());
		ServerController.sendJSONMessage(RoomConstants.HostQuitGameNotification, jsonToSend);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		IOCallBackHandler.getInstance().setMemoryGameListener(this);
		IOCallBackHandler.getInstance().setChatListener(this);
		IOCallBackHandler.getInstance().setGameCloseListener(this);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // The action bar home/up action should open or close the drawer.
	    // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
		//Handle presses on the action bar items
		switch (item.getItemId()) {
        case R.id.action_logout:
        	handleExitRoomUserRequest();
            return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		handleExitRoomUserRequest();
	}
	
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	private void handleExitRoomUserRequest() {
		new AlertDialog.Builder(this)
		    .setTitle("Exit Game")
		    .setMessage("Are you sure you want to exit game?")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	if(mUserState.equals(SocialGameConstants.IntentRoomStateVALUECreated)){
		        		closeGameNotification();
		        		new UserSessionManager(MemoryGameActivity.this).logOutUser();
		        	} else {
		        		notifyHostAboutGuestLeave();
		        		MemoryGameActivity.this.finish();
		        	}
		        }
		     })
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            //do nothing
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .show();	
	}
	


	private void moveUserHostBackToWaitingRoom() {
    	Intent intent = new Intent(this, RoomActivity.class);
    	intent.putExtra(SocialGameConstants.IntentRoomStateKEY, "Created");
    	intent.putExtra(RoomConstants.GameTypeKEY, GameType.MemoryGame);
    	User currUser = UserController.getUser();
    	intent.putExtra(SocialGameConstants.IntentPlayer1Key, currUser);
    	startActivity(intent);
	}
	
	@Override
	public void onPlayerLeftGameEvent(JSONObject jsonResponse) {
		if(mUserState.equals(SocialGameConstants.IntentRoomStateVALUEJoined)) {
			MyToaster.showToast(MemoryGameActivity.this,
					"The game was closed by the host!", Toast.LENGTH_SHORT);
		} else {
			MyToaster.showToast(MemoryGameActivity.this,
					mPlayer2.getFullName() + " has left the game!", Toast.LENGTH_SHORT);
			moveUserHostBackToWaitingRoom();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	private boolean checkIfNeedToVibrate() {
		if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			  Log.d("Chat", "drawer open");
			  return false;
		}
		Log.d("Chat", "drawer closed");
		return true;
	}
	
	private void scrollMyListViewToBottom() {
		listView.post(new Runnable() {
	        @Override
	        public void run() {
	            // Select the last row so it will scroll into view...
	        	listView.setSelection(adapter.getCount() - 1);
	        }
	    });
	}
	
	private void vibrate(int miliSec){
		 Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		 v.vibrate(miliSec);
	}
	
	

	@Override
	public void onMsgRecived(JSONObject obj) {
		Log.d("Chat", "Recieved Message " + obj.toString());
		final String msg = JSONUtils.getStringFromJSON(obj, "message", null);
		final String sender = JSONUtils.getStringFromJSON(obj, "sender", null);
		String type = JSONUtils.getStringFromJSON(obj, "type", null);	
		ChatLineItem line = null;
		
		Log.d("Chat", UserController.getUser().getFullName());
		Log.d("Chat", sender);
		
		if(checkIfNeedToVibrate()){
			Log.d("Chat", "vibrating");
			vibrate(500);
		}
		
		if(UserController.getUser().getFullName().equals(sender)){
			Log.d("Chat", "inside if");
			line = new ChatLineItem(sender, msg, android.R.color.holo_green_light);
		}
		else{
			Log.d("Chat", "inside else");
			line = new ChatLineItem(sender, msg, android.R.color.holo_orange_light);
		}
		
    	listItems.add(line);
		runOnUiThread(new Runnable() {
		    public void run() {
				scrollMyListViewToBottom();
				adapter.notifyDataSetChanged();
		    }
		});
	}
}
