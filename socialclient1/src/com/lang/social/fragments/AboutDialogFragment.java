package com.lang.social.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.lang.social.R;

public class AboutDialogFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		 builder.setMessage("SociaLang is a free social language learning community for everybody.")
		 	.setIcon(R.drawable.logo2)
		 	.setTitle("About SociaLang");

		 return builder.create();
	} 
}
