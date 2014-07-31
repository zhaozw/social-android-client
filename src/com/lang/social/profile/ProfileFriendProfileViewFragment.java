package com.lang.social.profile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.lang.social.R;
import com.lang.social.controllers.ServerController;
import com.lang.social.logic.User;
import com.lang.social.logic.UserController;
import com.lang.social.utils.JSONUtils;
import com.lang.social.utils.LanguageMap;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileFriendProfileViewFragment extends Fragment  {

	private static final String TAG = "Profile";
	private TextView tvFriendName;
	private TextView tvFriendPoints;
	private TextView tvFriendLevel;
	private ImageView ivFriendLanguage;
	private TextView learningLanguageText;
	private CircularImageView profileCircularImageView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	     // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment_friend_details_layout, container, false);
        
		Bundle bundle = getArguments();
		User friend = (User)bundle.getSerializable("friend");
		
		tvFriendName = (TextView) view.findViewById(R.id.tvFriendName);
		tvFriendName.setText(friend.getFullName());
		
		ivFriendLanguage = (ImageView) view.findViewById(R.id.ivFriendLearningLanguageFlag);
		ivFriendLanguage.setImageDrawable(getResources().getDrawable(LanguageMap.getLanguageResHashMap().get(friend.getLearningLanguage())));
		
		learningLanguageText = (TextView) view.findViewById(R.id.friendLearningLanguage);
		learningLanguageText.setText(friend.getLearningLanguageText());
		
		tvFriendPoints = (TextView) view.findViewById(R.id.tvFriendNumOfPoints);
		tvFriendPoints.setText("points : " + friend.getPointsOfCurrentLearningLanguage());
		
		tvFriendLevel = (TextView) view.findViewById(R.id.tvFriendLevel);
		tvFriendLevel.setText("level : " + friend.getCurrentLanguageLevel());
		
		profileCircularImageView = (CircularImageView) view.findViewById(R.id.profilePictureCircular);
		if(friend.isFacebookUser()){
			setProfilePicture(friend.getProfileID());
		}
		
		setFonts(view);
		
		return view;
	}
	

	private void sendListeners(final Message message) {
		
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
        tvFriendName.setTypeface(tf);
        tvFriendPoints.setTypeface(tf2);
        tvFriendLevel.setTypeface(tf2);
        learningLanguageText.setTypeface(tf2);
	}	
}
