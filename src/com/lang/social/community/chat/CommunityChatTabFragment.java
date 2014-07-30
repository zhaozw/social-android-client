package com.lang.social.community.chat;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lang.social.R;
import com.lang.social.chat.ChatLineItem;
import com.lang.social.chat.CustomChatAdapter;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.UserController;
import com.lang.social.utils.JSONUtils;



public class CommunityChatTabFragment extends Fragment implements CommunityChatServerListener {
	private CustomChatAdapter adapter;
	private ArrayList<ChatLineItem> listItems = new ArrayList<ChatLineItem>();
	private ListView listView;
	
	private EditText chatLine;
	private Button sendChatMsgButton;
	
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	     final View rootView = inflater.inflate(R.layout.fragment_community_chat_layout, container, false);
	     
	     listView = (ListView) rootView.findViewById(R.id.chat_listView);
	     adapter = new CustomChatAdapter(getActivity(), R.layout.custom_chat_item , listItems);
	     listView.setAdapter(adapter);
	     
	     chatLine = (EditText) rootView.findViewById(R.id.EditTextChatLine);
	     sendChatMsgButton = (Button) rootView.findViewById(R.id.ButtonSendChatMessage);
	     
	     IOCallBackHandler.getInstance().setCommunityChatServerListener(this);
	     addUserToChat();
	     setButtonChatListener();
	     
	     return rootView;
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
				ServerController.sendJSONMessage("communityChatMessage", json);
			}
		});
	}
	
	@Override
	public void onChatMsgRecived(JSONObject obj) {
		Log.d("Chat", "Recieved Message " + obj.toString());
		final String msg = JSONUtils.getStringFromJSON(obj, "message", null);
		final String sender = JSONUtils.getStringFromJSON(obj, "sender", null);
		String type = JSONUtils.getStringFromJSON(obj, "type", null);	
		final ChatLineItem line;
		Log.d("Chat", UserController.getUser().getFullName());
		Log.d("Chat", sender);
		
		if(UserController.getUser().getFullName().equals(sender)){
			Log.d("Chat", "inside if");
			line = new ChatLineItem(sender, msg, android.R.color.holo_green_light);
		}
		else{
			Log.d("Chat", "inside else");
			line = new ChatLineItem(sender, msg, android.R.color.holo_orange_light);
		}
		getActivity().runOnUiThread(new Runnable() {
		    public void run() {
		    	listItems.add(line);
				scrollMyListViewToBottom();
				adapter.notifyDataSetChanged();
		    }
		});
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
	
	public void addUserToChat() {
		JSONObject obj = new JSONObject();
		Log.d("Chat", "Notifying to server to start chat.");
		ServerController.sendJSONMessage("addUserToCommunityChat", obj);
	}
	
	public void onStop() {
		super.onStop();
		notifyServerToRemoveUserFromChat();
	}

	private void notifyServerToRemoveUserFromChat() {
		JSONObject obj = new JSONObject();
		Log.d("Chat", "Notifying to server to remove user from chat.");
		ServerController.sendJSONMessage("removeUserFromCommunityChat", obj);
		
	}
}
