package com.lang.social.competition;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class EndGameDialogFragment extends DialogFragment {

	private String mTitle = "Game Over!";
	private String mMessage = "Default Message";
	private String mButton1Text = "OK";
	private String mButton2Text = "Quit";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mMessage = getArguments().getString("message");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mTitle);
        builder.setMessage(mMessage)
               .setPositiveButton(mButton1Text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   ////
                   }
               })
               .setNegativeButton(mButton2Text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   ///
                   }
               });
        return builder.create();
	}

}
