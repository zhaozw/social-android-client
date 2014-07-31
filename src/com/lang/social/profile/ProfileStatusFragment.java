package com.lang.social.profile;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lang.social.R;
import com.lang.social.utils.JSONUtils;

public class ProfileStatusFragment extends Fragment {
	
	private static final String TAG = "Profile";
	
	private ListView listViewStatuses;
	private StatusAdapter statusAdapter;
	private ArrayList<StatusItem> statusItems = new ArrayList<StatusItem>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.profile_fragment_layout, container, false);
       
		Bundle bundle = getArguments();
		String statsJsonArrayString = bundle.getString(ProfileConstants.UserStats);
		
		JSONArray statsJsonArray = null;
		try{
			statsJsonArray = new JSONArray(statsJsonArrayString);
		} catch(JSONException ex) {
			ex.printStackTrace();
		}
       
       //---------------------------------------------------------------------------------------------------
	   statusAdapter = new StatusAdapter(getActivity(), R.layout.profile_status_item, statusItems);
	   statusAdapter.clear();
	   listViewStatuses = (ListView) view.findViewById(R.id.profileFragmentList);
       populateMessages(statsJsonArray);
       listViewStatuses.setAdapter(statusAdapter);
       //---------------------------------------------------------------------------------------------------
     
       setFonts(view);
       
	   return view;
		
	}

	private void populateMessages(JSONArray statsJsonArray) {
		if(statsJsonArray != null) {
			Log.d(TAG, statsJsonArray.toString());
			for(int i=0; i < statsJsonArray.length(); i++){
				StatusItem status = new StatusItem(JSONUtils.getJSONObject(statsJsonArray, i));
				statusItems.add(status);
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
        textHeader.setText("Languages Levels And Points");
	}
}
