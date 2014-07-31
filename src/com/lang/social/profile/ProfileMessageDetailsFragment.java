package com.lang.social.profile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.community.messages.MessagesServerListener;
import com.lang.social.controllers.ServerController;
import com.lang.social.iocallback.IOCallBackHandler;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.parsers.ServerResponseParser;
import com.lang.social.usermanager.UserSessionManager;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.MyToaster;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileMessageDetailsFragment extends Fragment  {

	private static final String TAG = "Profile";
	private EditText etSendMessage;
	private TextView tvMessageContent;
	private TextView tvMessageDate;
	private TextView tvMessageSender;
	private CircularImageView profileCircularImageView;
	private Button btnSubmitSendMessage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	     // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment_message_details_layout, container, false);
        
		Bundle bundle = getArguments();
		Message message = (Message)bundle.getSerializable("Message");
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		etSendMessage = (EditText) view.findViewById(R.id.etSendBackAMessage);
		etSendMessage.requestFocus();
		
		tvMessageContent = (TextView) view.findViewById(R.id.tvMessageBody);
		tvMessageContent.setText(message.getContent());
		
		tvMessageDate = (TextView) view.findViewById(R.id.tvMessageSentDate);
		tvMessageDate.setText(message.getDate());
		
		tvMessageSender = (TextView) view.findViewById(R.id.tvFriendName);
		tvMessageSender.setText(message.getSender().getFullName());
		
		profileCircularImageView = (CircularImageView) view.findViewById(R.id.profilePictureCircular);
		setProfilePicture(message.getProfileId());
		
		btnSubmitSendMessage = (Button) view.findViewById(R.id.btnSubmitSendMessage);
		btnSubmitSendMessage.setEnabled(false);
		
		setFonts(view);
		
		sendListeners(message);
		
		return view;
	}
	

	private void sendListeners(final Message message) {
		
		etSendMessage.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 0){
					btnSubmitSendMessage.setEnabled(true);
				} else {
					btnSubmitSendMessage.setEnabled(false);
				}
			}
		});
		
		
		btnSubmitSendMessage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMessagToServer(message);
				etSendMessage.setText("");
				btnSubmitSendMessage.setEnabled(false);
			}
		});
		
	}
	
	private void sendMessagToServer(Message message) {
		JSONObject msgJson = new JSONObject();
		if(UserController.getUser().isFacebookUser()){
			JSONUtils.setStringValue(msgJson, "facebookUser", "true");
			JSONUtils.setStringValue(msgJson, "profileid", message.getSender().getProfileID());
			JSONUtils.setStringValue(msgJson, "username", null);
		} else{
			JSONUtils.setStringValue(msgJson, "facebookUser", "false");
			JSONUtils.setStringValue(msgJson, "username", message.getSender().getUserName());
			JSONUtils.setStringValue(msgJson, "profileid", null);
		}

		JSONUtils.setStringValue(msgJson, "subject", "quick reply");
		JSONUtils.setStringValue(msgJson, "content", etSendMessage.getText().toString());
		ServerController.sendJSONMessage("sendNewMessageRequest", msgJson);
	}

	private void setProfilePicture(final String profileid) {
		Log.d(TAG, profileid);
		new Thread(new Runnable() {
			@Override
			public void run() {
				 try {
					 URL MyProfilePicURL = new URL("https://graph.facebook.com/" + profileid + "/picture?type=large&width=100&height=100");
					 Bitmap mIcon1 = BitmapFactory.decodeStream(MyProfilePicURL.openConnection().getInputStream());
					 onProfileImageRecieved(mIcon1);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	private void onProfileImageRecieved(final Bitmap icon) {
		 getActivity().runOnUiThread(new Runnable() {
			public void run() {
				profileCircularImageView.setImageBitmap(icon);
			}
		});
	}
	
	private void setFonts(View view) {
        TextView textHeader = (TextView) view.findViewById(R.id.messageHeader);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-LightItalic.ttf");
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-ThinItalic.ttf");
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Light.ttf");
		Typeface tf3 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Regular.ttf");
		Typeface tf4 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Thin.ttf");
		((TextView) view.findViewById(R.id.messageHeader)).setTypeface(tf4);
        textHeader.setTypeface(tf);
        tvMessageContent.setTypeface(tf);
        tvMessageDate.setTypeface(tf);
        tvMessageSender.setTypeface(tf);
        btnSubmitSendMessage.setTypeface(tf);
        btnSubmitSendMessage.setTextSize(14);
        etSendMessage.setTypeface(tf);
	}


	
}
