package com.lang.social.community.general;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {
	private static ProgressDialog dialog;
	
	private LoadingDialog() {
		
	}
	
	public static ProgressDialog getProgressDialog(Context i_Context)
	{
		if(dialog == null)
		{
			dialog = new ProgressDialog(i_Context);
			dialog.setMessage("Loading...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(true);
		}
		return dialog;
	}
}
