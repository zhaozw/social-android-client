package com.lang.social.facebook;

import com.facebook.Session;

public class FacebookController {

	public static boolean isFacebookSessionOpen() {
		Session session = Session.getActiveSession();
		 if (session != null && session.isOpened()) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	
//	public void GetUserProfileImage(final String profileID, final ImageView ivToSet) {
//	    AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
//	        @Override
//	        public Bitmap doInBackground(Void... params) {
//	        	
//	            URL imageURL = null;
//	            Bitmap imageBitMap = null;
//	            
//	            try {
//	            	
//	            	imageURL = new URL("http://graph.facebook.com/"+ profileID +"/picture");
//	            	imageBitMap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
//	                
//	            } catch (MalformedURLException e) {
//	                e.printStackTrace();
//	            } catch (IOException e) {
//	                e.printStackTrace();
//	            }
//	            
//	            return imageBitMap;
//	        }
//
//	        @Override
//	        protected void onPostExecute(Bitmap result) {
//	        	ivToSet.setImageBitmap(result);
//	        }
//	    };
//	    task.execute();
//	}
	

}
