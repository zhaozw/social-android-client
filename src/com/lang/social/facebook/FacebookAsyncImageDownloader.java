package com.lang.social.facebook;

import java.io.IOException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class FacebookAsyncImageDownloader extends AsyncTask<ImageView, Void, Bitmap> {
	
	 	ImageView ivToSet;
	 	URL imageURL = null;
	 	Bitmap bitmap = null;

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //show a progress bar
	    }
	    
        @Override
        public Bitmap doInBackground(ImageView... imageViews) {
        	this.ivToSet = imageViews[0];
			return download_Image((String)ivToSet.getTag());	
        }
	
	    @Override
	    protected void onPostExecute(Bitmap result) {
	        ivToSet.setImageBitmap(result);
	        ivToSet.postInvalidate();
	    }
	    
	    private Bitmap download_Image(String url) {
        	try {
        		
        		imageURL = new URL(url);
        		bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        		
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        	return bitmap;
	    }
  
}

