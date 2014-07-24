
package com.lang.social.facebook;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.lang.social.R;
import com.lang.social.activities.LoginActivity;
import com.lang.social.controllers.ServerController;
import com.lang.social.logic.User;
import com.lang.social.usermanager.UserSessionManager;




public class FacebookFragment extends Fragment {

	private static final String FACEBOOKTAG = "FACEBOOK";
	private static final String FacebookLoginRequest = "FacebookLoginRequest";
	private UiLifecycleHelper uiHelper;
	private Session mSession;
	
	//public static Button shareButton;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	public static final String TAG = "FACEBOOK_FRAGMENT";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.facebook_fragment, container, false);
		LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
		authButton.setFragment(this);
		
		//shareButton = (Button) view.findViewById(R.id.shareButton);
		//shareButton.setOnClickListener(new View.OnClickListener() {
//		    @Override
//		    public void onClick(View v) {
//		        publishStory();        
//		    }
//		});
//		if (savedInstanceState != null) {
//		    pendingPublishReauthorization = 
//		        savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
//		}
		return view;
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		Log.d(FACEBOOKTAG, session.toString());
		Log.d(FACEBOOKTAG, state.toString());
		
	    if (state.isOpened()) {
	    	if (mSession == null || isSessionChanged(session)) {
	    		
	    		mSession = session;
	    		
	    		Log.d(FACEBOOKTAG, "User Logged in using facebook");
	    		Log.d(FACEBOOKTAG, "Access Token: " + session.getAccessToken());
	    		UserSessionManager.SetAccessToken(session.getAccessToken());
		        Request.newMeRequest(session, new Request.GraphUserCallback() {
		        	  @Override
		        	  public void onCompleted(GraphUser user, Response response) {
		        	    if (user != null) {
		        	    	Log.d("SocketIO", "sending" + FacebookLoginRequest);
		        	    	((LoginActivity)getActivity()).progressDialog.show();
		        	    	ServerController.sendJSONMessage(FacebookLoginRequest, createJsonUser(user));
		        	    }
		        	  }
		        }).executeAsync();
	    	}
	    }
//	    	shareButton.setVisibility(View.VISIBLE);
//	    	if (pendingPublishReauthorization && 
//	    	        state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
//	    	    pendingPublishReauthorization = false;
//	    	    publishStory();
//	    	}
//	    }
//	    else {
//	    	shareButton.setVisibility(View.INVISIBLE);
//	    }
	}
	
	private  void publishStory() {
	    Session session = Session.getActiveSession();

	    if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }

	        Bundle postParams = new Bundle();
	        postParams.putString("name", "SociaLang For Android");
	        postParams.putString("caption", "Learn New Languages with Others Around The World");
	        postParams.putString("description", "In SocialLang, You can learn new languages in fun-games while meeting new Friends");
	        postParams.putString("link", "https://developers.facebook.com/android");
	        postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	        
	        Request.Callback callback= new Request.Callback() {
	            public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    Log.i(TAG,
	                        "JSON error "+ e.getMessage());
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    Toast.makeText(getActivity()
	                         .getApplicationContext(),
	                         error.getErrorMessage(),
	                         Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(getActivity()
	                             .getApplicationContext(), 
	                             postId,
	                             Toast.LENGTH_LONG).show();
	                }
	            }
	        };

	        Request request = new Request(session, "me/feed", postParams, 
	                              HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }

	}
	
	private  boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}

	private JSONObject createJsonUser(GraphUser user) {
		JSONObject jsonUser = null;
		try {
			jsonUser = new JSONObject();
			jsonUser.put(User.firstNameKEY, user.getFirstName());
			jsonUser.put(User.lastNameKEY, user.getLastName());
			jsonUser.put(User.profileidKEY, user.getId());
			jsonUser.put(User.isFacebookUserKEY, true);
		} catch (JSONException e) {
			throw new RuntimeException();
		}
		return jsonUser;
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	    Session session = Session.getActiveSession();
	    if (session != null && (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
		uiHelper.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
		uiHelper.onSaveInstanceState(outState);
	}
	
	private boolean isSessionChanged(Session session) {
		
	    // Check if session state changed
	    if (mSession.getState() != session.getState())
	        return true;
	    
	    // Check if accessToken changed
	    if (mSession.getAccessToken() != null) {
	        if (!mSession.getAccessToken().equals(session.getAccessToken()))
	            return true;
	    }
	    else if (session.getAccessToken() != null) {
	        return true;
	    }
	    // Nothing changed
	    return false;
	    
	}
	
	
}

