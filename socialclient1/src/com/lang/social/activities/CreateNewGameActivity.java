//package com.lang.social.activities;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.lang.social.R;
//import com.lang.social.adapters.LanguageItemAdapter;
//import com.lang.social.interfaces.CreateGameListener;
//import com.lang.social.iocallback.IOCallBackHandler;
//import com.lang.social.items.LanguageItem;
//import com.lang.social.utils.MyToaster;
//
//public class CreateNewGameActivity extends Activity implements CreateGameListener {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_create_new_game);
//
//		createListItems();
//    	setTextFonts();
//    	//setListItemsListeners();
//    	//setBtnCreateListener();
//    	setCreateGameListener();
// 
//	}
//
//	private void setCreateGameListener() {
//		IOCallBackHandler.getInstance().setCreateGameListener(this);
//		
//	}
//
////	private void setBtnCreateListener() {
////		Button btnCreateGame = (Button) findViewById(R.id.btnCreateGame);
////		btnCreateGame.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				//send to server new game
////				if(learnLangSelected && teachLangSelected){
//////					User currUser = UserController.getCurrentUser();
//////					Player player = new Player(currUser.getFirstName(), 
//////							currUser.getLastName(), teachLang, learnLang, currUser.getUserID());
//////					int flagIconId = 0; //dont need i think
//////					Game newGame = new Game(GameState.Lobby, player, flagIconId);
//////					JSONObject gameJSONToSend = convertGameToJSON(newGame);
//////					ServerController.sendJSONMessage("gameCreated", gameJSONToSend);
////					
////				}
////				else{
////					MyToaster.showToast(CreateNewGameActivity.this,
////							"Choose languages for both categories", Toast.LENGTH_SHORT);
////				}
////			}
////		});
//////	}
////	
////	private JSONObject convertGameToJSON(Game game){
////		JSONObject jsonGame = new JSONObject();
////		JSONObject jsonPlayer = new JSONObject();
////		try {
////			
////			jsonGame.put("gameState", game.getGameState().toString());
////			jsonPlayer.put("firstName", game.getPlayer1().getFirstName());
////			jsonPlayer.put("lastName", game.getPlayer1().getLastName());
////			jsonPlayer.put("teaching", game.getPlayer1().getTeaching().toString());
////			jsonPlayer.put("studying", game.getPlayer1().getStudying().toString());
////			//jsonPlayer.put("profileID", UserController.getCurrentUser().getUserID());
////			jsonGame.put("player1", jsonPlayer);
////			return jsonGame;
////			
////		} catch (JSONException e) {
////			e.printStackTrace();
////		}
////	
////		return null;
////	}
////
////	private void setListItemsListeners(){
////		ListView lvWantToLearn = (ListView) findViewById(R.id.ListViewWantToLearn);
////		lvWantToLearn.setOnItemClickListener(new OnItemClickListener() {
////			@Override
////			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
////					long arg3) {
////				view.setSelected(true);
////				learnLangSelected = true;
////				TextView tv = (TextView) view.findViewById(R.id.txtTitle);
////				learnLang = Language.valueOf(tv.getText().toString());
////			}
////		});
////	
////
////		ListView lv2 = (ListView) findViewById(R.id.ListViewWantToTeach);
////		lv2.setOnItemClickListener(new OnItemClickListener() {
////			@Override
////			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
////					long arg3) {
////				view.setSelected(true);
////				teachLangSelected = true;
////				TextView tv = (TextView) view.findViewById(R.id.txtTitle);
////				teachLang = Language.valueOf(tv.getText().toString());
////			}
////		});
//		
//		
////		TextView tv = (TextView) view.findViewById(R.id.txtTitle);
////		String langToLearnStr = tv.getText().toString();
////		Intent intent = new Intent(CreateNewGameActivity.this, GameLobbyActivity.class);
////		intent.putExtra("langToLearn", langToLearnStr);
////		startActivity(intent);
//		
//
//	//}
//	
//	private void createListItems() {
//		 List<LanguageItem> languageItems = new ArrayList<LanguageItem>();
//		 languageItems.add(new LanguageItem(R.drawable.israel1, "Hebrew"));
//        
//        LanguageItemAdapter wantToLearnAdapter = new LanguageItemAdapter(this, 
//                R.layout.listview_language_item_row, languageItems);
//      
//        ListView ListViewWantToLearn = (ListView)findViewById(R.id.ListViewWantToLearn);
//         
//        View headerLearn = (View)getLayoutInflater().inflate(R.layout.listview_learn_header, null);
//        ListViewWantToLearn.addHeaderView(headerLearn, null, false);
//
//        ListViewWantToLearn.setAdapter(wantToLearnAdapter);
//        
//
//        //--------------------------------------------------------------
//        
//        
//        LanguageItemAdapter wantToTeachAdapter = new LanguageItemAdapter(this, 
//                R.layout.listview_language_item_row , languageItems);
//        
//        ListView ListViewWantToTeach = (ListView)findViewById(R.id.ListViewWantToTeach);
//        
//        View headerTeach = (View)getLayoutInflater().inflate(R.layout.listview_teach_header, null);
//        ListViewWantToTeach.addHeaderView(headerTeach, null, false);
//
//        ListViewWantToTeach.setAdapter(wantToTeachAdapter);
//	}
//
//	private void setTextFonts() {
//        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Jura-DemiBold.ttf");
//        ((TextView)findViewById(R.id.whatToLearnHeader)).setTypeface(tf);
//        ((TextView)findViewById(R.id.whatToTeachHeader)).setTypeface(tf);
//        ((TextView)findViewById(R.id.btnCreateGame)).setTypeface(tf);
//	}
//	
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.create_new_game, menu);
//		return true;
//	}
//
//	@Override
//	public void onGameCreationRecieved(JSONObject jsonObject) {
//		try {
//			boolean Success = jsonObject.getBoolean("success");
//			String Msg = jsonObject.getString("msg");
//			if(Success == false)
//			{
//				MyToaster.showToast(CreateNewGameActivity.this,
//						"Error During Room Creation", Toast.LENGTH_SHORT);
//				Log.d("CreateNewGame","Error CreatingRoom");
//			}
//			else
//			{
//				startActivity(new Intent(this, RoomActivity.class));
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//}
