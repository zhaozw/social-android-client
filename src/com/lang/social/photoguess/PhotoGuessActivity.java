package com.lang.social.photoguess;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lang.social.R;

public class PhotoGuessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_guess);
		
	    FrameLayout frame = (FrameLayout) findViewById(R.id.graphics_holder);
	    PlayAreaView image = new PlayAreaView(this, R.drawable.droid);
	    frame.addView(image);
	    
	    showToast();
	}
	

	private void showToast() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
			    Toast toast= Toast.makeText(getApplicationContext(), 
			    		"Car", Toast.LENGTH_SHORT);  
			    		toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
			    		toast.show();
				
			    Toast toast1= Toast.makeText(getApplicationContext(), 
			    		"Car", Toast.LENGTH_SHORT);  
			    		toast1.setGravity(Gravity.BOTTOM|Gravity.RIGHT, 0, 0);
			    		toast1.show();
			    		
			    Toast toast3= Toast.makeText(getApplicationContext(), 
			    		"Car", Toast.LENGTH_SHORT);  
			    		toast3.setGravity(Gravity.TOP|Gravity.RIGHT, 0, 0);
			    		toast3.show();
				
			    Toast toast4= Toast.makeText(getApplicationContext(), 
			    		"Car", Toast.LENGTH_SHORT);  
			    		toast4.setGravity(Gravity.BOTTOM|Gravity.LEFT, 0, 0);
			    		toast4.show();
					
			}
			
			
		}, 1000);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_guess, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
