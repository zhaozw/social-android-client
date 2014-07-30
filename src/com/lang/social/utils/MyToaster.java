package com.lang.social.utils;

import android.app.Activity;
import android.widget.Toast;

public class MyToaster {
	 
	 public static void showToast(Activity activityContext, final String msg, final int length){
	  final Activity thisActivity = activityContext;
	  activityContext.runOnUiThread(new Runnable() {
	      public void run() {
	       Toast.makeText(thisActivity, msg , length).show();
	      }
	  }); 
	 }
	}
