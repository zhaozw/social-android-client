package com.lang.social.community.messages;
 
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;
 
public class MessagesTabFragment extends Fragment implements MessagesServerListener {
	
	private ExpandableListView messagesListView;
	private CustomMessageListAdapter adapter;
	private ArrayList<MessageItem> listItems = new ArrayList<MessageItem>();
	
	public ProgressDialog progressDialog;
	
	DisplayMetrics metrics;
	int width;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_messages_tab_layout, container, false);
        
        messagesListView = (ExpandableListView) rootView.findViewById(R.id.messages_list_listView);
        
        adapter = new CustomMessageListAdapter(getActivity(), R.layout.message_list_row, R.layout.message_item_child_row, listItems);
    
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        messagesListView.setIndicatorBounds(width - GetDipsFromPixel(30), width - GetDipsFromPixel(0));
        messagesListView.setAdapter(adapter);
        
//        progressDialog = LoadingDialog.getProgressDialog(getActivity());
//        if(progressDialog.isShowing() == false) {
//        	progressDialog.show();
//        }
        
        setMessagesServerListener();
        ServerController.sendJSONMessage("messagesRequest", new JSONObject());
        
        Date date = new Date(0);
        JSONObject obj = new JSONObject();
        try {
			obj.put("firstName", "Daniel");
			obj.put("lastName", "Rahamim");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        MessageItem item = new MessageItem(obj, "Hey Ossi!", date, "Hey, Whats up?");
//        item.getMessageItemChildList().add(new MessageItemChild(item.getSender(), item.getContent()));
//        date = new Date(100000000);
//        MessageItem item2 = new MessageItem(obj, "Yalla Solid!", date, "di ya tyish!");
//        item2.getMessageItemChildList().add(new MessageItemChild(item.getSender(), item.getContent()));
//        listItems.add(item);
//        listItems.add(item2);
//        adapter.notifyDataSetChanged();
        return rootView;
    }

	private void setMessagesServerListener() {
		IOCallBackHandler.getInstance().setMessagesListener(this);
	}

	 @Override
	 public void onMessagesListRespone(JSONObject responseJson) {
		 listItems.clear();
		 Log.d("Message", responseJson.toString());
		 Date messageSentDate = null;
		 JSONArray messagesArray = JSONUtils.getJSONArray(responseJson, "messages");
		 for (int i = 0; i < messagesArray.length(); i++) {
			 try {
				 JSONObject messageJson = messagesArray.getJSONObject(i);
				 String subject = messageJson.getString("subject");
				 
				 String content = messageJson.getString("content");
				 
				 String msgID = messageJson.getString("messageId");
				 
				 String date = messageJson.getString("date");
				 ISO8601DateFormat df = new ISO8601DateFormat();
				 messageSentDate = df.parse(date);
				
				 Log.d("DATE", messageSentDate.toString());
				 
				 JSONObject sender = messageJson.getJSONObject("sender");
				 
				 MessageItem messageItem = new MessageItem(sender, subject, messageSentDate, content, msgID);
				 messageItem.getMessageItemChildList().add(new MessageItemChild(messageItem.getSender(), messageItem.getContent()));
				
				 listItems.add(messageItem);
			 } catch (JSONException e) {
				 Log.d("Message", "Error parsing friend");
				 throw new RuntimeException();
			 } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 updateProgressDialogAndAdapter();
	 }
	 
	 private void updateProgressDialogAndAdapter() {
		 getActivity().runOnUiThread(new Runnable() {
			 @Override
			 public void run() {
//				 if(progressDialog.isShowing() == true) {
//			        	progressDialog.dismiss();
//				 }
				 adapter.notifyDataSetChanged();
			 }
		 }); 
	 }
	 
	 private int GetDipsFromPixel(float pixels)
	 {
		 // Get the screen's density scale
		 final float scale = getResources().getDisplayMetrics().density;
		 // Convert the dps to pixels, based on density scale
		 return (int) (pixels * scale + 0.5f);
	 }

	@Override
	public void onMesssageDeleteRespone(JSONObject obj) {
		String result = JSONUtils.getStringFromJSON(obj, "result", null);
		if(result.equals("OK")) {
			listItems.remove(adapter.getMessageToRemovePos());
			updateProgressDialogAndAdapter();
			MyToaster.showToast(getActivity(), "Message Was Successfully Deleted!", Toast.LENGTH_LONG);
		}
		else {
			MyToaster.showToast(getActivity(), "Error At Deleting Message!", Toast.LENGTH_LONG);
		}
		
	}

	@Override
	public void OnMessageSentResponse(JSONObject obj) {
		// TODO Auto-generated method stub
		
	}
	 
}