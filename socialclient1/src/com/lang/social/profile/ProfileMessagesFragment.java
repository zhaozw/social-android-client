package com.lang.social.profile;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.enchancedlistview.EnhancedListView;
import com.lang.social.enchancedlistview.EnhancedListView.OnDismissCallback;
import com.lang.social.utils.JSONUtils;

public class ProfileMessagesFragment extends Fragment  {

	private static final String TAG = "Profile";
	
	private EnhancedListView listViewMessages;
	private MessagesAdapter messagesAdapter;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private TextView tvNoMessagesLabel;
	 
	private MessageFragmentListener messageFragmentListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	     // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment_messages_layout, container, false);
        
        tvNoMessagesLabel = (TextView) view.findViewById(R.id.tvNoMessagesLabel);
        
        setFonts(view);

		Bundle bundle = getArguments();
		String messagesString = bundle.getString(ProfileConstants.UserMessages);
		JSONArray messagesJsonArray = null;
		try{
			messagesJsonArray = new JSONArray(messagesString);
		} catch(JSONException ex) {
			ex.printStackTrace();
		}
        
        //---------------------------------------------------------------------------------------------------
        messagesAdapter = new MessagesAdapter(getActivity(), R.layout.profile_message_item, messages);
        messagesAdapter.clear();
        listViewMessages = (EnhancedListView) view.findViewById(R.id.profileFragmentEnhancedListView);
        
        if(messagesJsonArray != null && messagesJsonArray.length() > 0){
        	populateMessages(messagesJsonArray);
            tvNoMessagesLabel.setVisibility(View.GONE);
            listViewMessages.setVisibility(View.VISIBLE);
        } else {
        	showNoMessagesAlert();
        	listViewMessages.setVisibility(View.GONE);
            tvNoMessagesLabel.setVisibility(View.VISIBLE);
            tvNoMessagesLabel.setPaddingRelative(0, 50, 0, 0);
        }
        
        listViewMessages.setAdapter(messagesAdapter);
        //---------------------------------------------------------------------------------------------------
  
        initEnhancedListView();

		return view;
	}

	private void showNoMessagesAlert() {

	}

	private void initEnhancedListView() {
		// mAdapter if your adapter, that has already been initialized and set to the listview.
		listViewMessages.setDismissCallback(new OnDismissCallback() {

		  @Override public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {

		    // Store the item for later undo
		    final Message item = (Message) messagesAdapter.getItem(position);
		    // Remove the item from the adapter
		    messagesAdapter.remove(position);
		    // return an Undoable
		    return new EnhancedListView.Undoable() {
		      // Reinsert the item to the adapter
		      @Override public void undo() {
		    	 messagesAdapter.insert(position, item);
		      }

		      // Return a string for your item
		      @Override public String getTitle() {
		    	  return "message deleted!";
		      }

		      // Delete item completely from your persistent storage
		      @Override public void discard() {
		    	 //need to tell profileActivity(parent) to update the passed bundle
		    	  if(messageFragmentListener != null){
		    		  messageFragmentListener.OnRemovedMessage(item);
		    	  }
		    	  //need to send to server message id for him to delete from mongodb.
		    	  JSONObject msgToDeleteDetails = new JSONObject();
		    	  JSONUtils.setStringValue(msgToDeleteDetails, "msgid", item.getMessageId());
		    	  ServerController.sendJSONMessage("deleteMessageRequest", msgToDeleteDetails);
		      }
		    };
		  }
		});
		
		
        // Show toast message on click and long click on list items.
		listViewMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	if(messageFragmentListener != null) {
            		messageFragmentListener.OnShowMessageDetailsClick(messagesAdapter.getItem(position));
            	}
            }
        });
		
		listViewMessages.enableSwipeToDismiss();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		messageFragmentListener = (ProfileActivity)activity;
	}

	@Override
	public void onStop() {
        if(listViewMessages != null) {
        	listViewMessages.discardUndo();
        }
        super.onStop();
	}

	private void populateMessages(JSONArray messagesJsonArray) {
		if(messagesJsonArray != null) {
			Log.d(TAG, messagesJsonArray.toString());
			for(int i=0; i < messagesJsonArray.length(); i++){
				Message message = new Message(JSONUtils.getJSONObject(messagesJsonArray, i));
				messages.add(message);
			}
		}
	}

	private void setFonts(View view) {
        TextView textHeader = (TextView) view.findViewById(R.id.messageHeader);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-LightItalic.ttf");
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-ThinItalic.ttf");
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf");
		Typeface tf3 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Regular.ttf");
		Typeface tf4 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Thin.ttf");
        textHeader.setTypeface(tf);
        textHeader.setText("Your Messages");
        tvNoMessagesLabel.setTypeface(tf1);
	}

	
	
}
