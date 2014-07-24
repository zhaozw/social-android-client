//package com.lang.social.activities;
//
//import java.io.File;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Typeface;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.lang.social.R;
//import com.lang.social.controllers.FormServerController;
//import com.lang.social.controllers.UserController;
//import com.lang.social.controllers.UserController.LoginMethod;
//import com.lang.social.interfaces.ProfileUpdateListener;
//import com.lang.social.iocallback.FormIOCallBackHandler;
//import com.lang.social.utils.EditTextSimpleTextWatcher;
//import com.lang.social.utils.MyToaster;
//
//
//public class ProfileActivity extends Activity implements ProfileUpdateListener {
//	
//	private File profileImageFile;
//	
//	private static final int BROWSE_GALLERY_REQUEST = 1;
//
//	private EditText etUsername;
//	private EditText etFName;
//	private EditText etLName;
//	private Button btnPEdit;
//	private ImageView ivProfileImage;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_profile3);
//
//		FormIOCallBackHandler.setProfileUpdateListener(this);
//		
//		etUsername = (EditText) findViewById(R.id.etPUsername);
//		etFName = (EditText) findViewById(R.id.etPFName);
//		etLName = (EditText) findViewById(R.id.etPLName);
//		
//		if(UserController.getUserLoginMethod() == LoginMethod.FACEBOOK){
//			//ivProfileImage = (ImageView) findViewById(R.id.ivProfilePic);
//		}
//		
//		
//		setOnClickListeners();
//		setCurrentUserText();
//		updateFonts();
//
//	}
//	
//	private void replaceProfilePicture() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		View v = getLayoutInflater().inflate(R.layout.replace_image, null);
//		builder.setTitle(R.string.replace_profile_pic);
//		builder.setView(v);
//	
//		final AlertDialog dlg = builder.create();
//		dlg.show();
//		
//		Button takePhoto = (Button) dlg.findViewById(R.id.take_photo);
//		takePhoto.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				startActivity(intent);
//			}
//		});
//		
//			
//		Button browseGallery = (Button) dlg.findViewById(R.id.browse_gallery);
//		browseGallery.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				browseGallery();
//			}
//		});
//		
//	}
//
//	private void browseGallery(){
//		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		startActivityForResult(intent, BROWSE_GALLERY_REQUEST);
//	}
//	
//	private void setOnClickListeners(){
//		
//		ImageView ivEditUsername = (ImageView) findViewById(R.id.ivEditUsername);
//		ivEditUsername.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				etUsername.setEnabled(true);
//				etUsername.selectAll();
//				etUsername.requestFocus();
//				btnPEdit.setEnabled(true);
//			}
//		});
//		
//		ImageView ivEditFirstName = (ImageView) findViewById(R.id.ivEditFirstName);
//		ivEditFirstName.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				etFName.setEnabled(true);
//				etFName.selectAll();
//				etFName.requestFocus();
//				btnPEdit.setEnabled(true);
//			}
//		});
//		
//		ImageView ivEditLastName = (ImageView) findViewById(R.id.ivEditLastName);
//		ivEditLastName.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				etLName.setEnabled(true);
//				etLName.selectAll();
//				etLName.requestFocus();
//				btnPEdit.setEnabled(true);
//			}
//		});
//		
//		
//		
////		ivProfileImage = (ImageView) findViewById(R.id.ivProfilePic);
////		ivProfileImage.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				replaceProfilePicture();
////			}
////		});
//		
//		btnPEdit = (Button) findViewById(R.id.btnPEdit);
//		btnPEdit.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				JSONObject jsonToSend = createJSONFromProfileForm();
//				if(jsonToSend != null){
//					FormServerController.sendJSONMessage("profileUpdate", jsonToSend);
//				}
//			}
//		});
//		
//		addTextChangeListeners();
//		
//	}
//
//
//	private void addTextChangeListeners() {
//		etUsername.addTextChangedListener(new EditTextSimpleTextWatcher(){
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if( etUsername.getText().toString().length() == 0 ){
//					etUsername.setError( "Username name is required!" );
//				}
//			}
//		});
//		
//		etFName.addTextChangedListener(new EditTextSimpleTextWatcher(){
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if( etFName.getText().toString().length() == 0 ){
//					etFName.setError( "First Name is required!" );
//				}
//			}
//		});
//		etLName.addTextChangedListener(new EditTextSimpleTextWatcher(){
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if( etLName.getText().toString().length() == 0 ){
//					etLName.setError( "Last Name is required!" );
//				}
//			}
//		});
//	}
//	
//	private JSONObject createJSONFromProfileForm() {
//		JSONObject json = new JSONObject();
//		try {
//			
//			json.put("firstName", etFName.getText().toString());
//			json.put("lastName", etLName.getText().toString());
//			json.put("email", etUsername.getText().toString());
//			return json;
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private void setCurrentUserText() {
//		String currentUserEmail = UserController.getCurrentUser().getEmail();
//		String firstName = UserController.getCurrentUser().getFirstName();
//		String lastName = UserController.getCurrentUser().getLastName();
//		etUsername.setText(currentUserEmail);
//		etFName.setText(firstName);
//		etLName.setText(lastName);
//	}
//
//
//	private void updateFonts() {
//		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Jura-DemiBold.ttf");
//		TextView tvPUsername = (TextView) findViewById(R.id.tvPUsername);
//		tvPUsername.setTypeface(tf);
//		TextView tvFirstName = (TextView) findViewById(R.id.tvPLFName);
//		tvFirstName.setTypeface(tf);
//		TextView tvLastName = (TextView) findViewById(R.id.tvPLName);
//		tvLastName.setTypeface(tf);
//		Button btnEditProfile = (Button) findViewById(R.id.btnPEdit);
//		btnEditProfile.setTypeface(tf);
//	}
//
//	private void afterUpdateSuccessActions(JSONObject jsonUser) throws JSONException {
//		btnPEdit.setText("Edit Profile");
//		etUsername.setEnabled(false);	
//		etFName.setEnabled(false);		
//		etLName.setEnabled(false);	
//		
//		//need to change currentUser in app details
//		UserController.getCurrentUser().setEmail(jsonUser.getString("email"));
//		UserController.getCurrentUser().setFirstName(jsonUser.getString("firstName"));
//		UserController.getCurrentUser().setLastName(jsonUser.getString("lastName"));
//	}
//	
//	@Override
//	public void onProfileUpdated(JSONObject jsonReply) {
//		try {
//			
//			boolean success = jsonReply.getBoolean("success");
//			String msg = jsonReply.getString("msg");
//			JSONObject jsonUser = jsonReply.getJSONObject("data");
//
//			if(success){
//				MyToaster.showToast(this, "Your profile was updated successfuly!", Toast.LENGTH_LONG);
//				afterUpdateSuccessActions(jsonUser);
//			}
//			else{
//				Log.d("profileEvent", msg);
//				MyToaster.showToast(this, "Error updating your profile!", Toast.LENGTH_SHORT);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		
//		if(requestCode == BROWSE_GALLERY_REQUEST){
//			//after user browsed gallery for profile image replace
//			Log.d("profileEvent", "Gallery Data : " + data.getData());
//			
//			//query the gallery
//			String [] columns = {MediaStore.Images.Media.DATA};
//			
//			Uri imageUri = data.getData();
//			Cursor cursor = getContentResolver().query(imageUri, columns, null, null, null);
//			
//			cursor.moveToFirst();
//			
//			int columnIndex = cursor.getColumnIndex(columns[0]);
//			String imagePath = cursor.getString(columnIndex);
//			
//			Uri profileImageUri = Uri.parse(imagePath);
//			
//			//do we need to save an image profile to mongodb for every user
//			//or can we store the image on their phone sd card
//			
//			ivProfileImage.setImageURI(profileImageUri);
//			
//			cursor.close();
//		}
//		
//	}
//	
//}
