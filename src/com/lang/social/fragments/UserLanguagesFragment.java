//package com.lang.social.fragments;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.lang.social.R;
//
//public class UserLanguagesFragment extends Fragment {
//	 
//    ImageView ivIcon;
//    TextView tvItemName;
//
//    public static final String IMAGE_RESOURCE_ID = "iconResourceID";
//    public static final String ITEM_NAME = "itemName";
//
//    public UserLanguagesFragment() {}
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//
//          View view = inflater.inflate(R.layout.languages_layout_fragment, container, false);
//
//          ivIcon = (ImageView) view.findViewById(R.id.ivLanguageIcon);
//          tvItemName = (TextView) view.findViewById(R.id.tvLanguageTitle);
//
//          tvItemName.setText(getArguments().getString(ITEM_NAME));
//          ivIcon.setImageDrawable(view.getResources().getDrawable(getArguments().getInt(IMAGE_RESOURCE_ID)));
//          return view;
//    }
//}
