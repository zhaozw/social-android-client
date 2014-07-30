package com.lang.social.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.lang.social.R;

public class HelpDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		 builder.setMessage("sociaLang is an android application, designed to connect between people\n" + 
"who want to learn an extra language. They can do it personally or socially.\n"  +
"sociaLang meant to teach, but not in a classic way.\n" + 
"It offers games that make the process of learning way more interesting and fun.\n" + 
"The social side as well, can make new connections between people.\n" + 
"Every user can practice with other user, as they're exchanging languages.")
		 	.setIcon(R.drawable.logo2)
		 	.setTitle("Help");

		 return builder.create();
	}
}
