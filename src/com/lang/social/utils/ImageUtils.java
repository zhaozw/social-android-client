package com.lang.social.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

@SuppressWarnings("restriction")
public class ImageUtils {

	/**
	 * Decode string to image
	 * @param imageString The string to decode
	 * @return decoded image
	 */
	public static Bitmap decodeToImage(String imageString) {

		  byte[] imageAsBytes = Base64.decode(imageString.getBytes(), 0);
		     
//		     image.setImageBitmap(
//		             BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
//		     );
//		  return image;
		  return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
		 }

	/**
	 * Encode image to string
	 * @param image The image to encode
	 * @param type jpeg, bmp, ...
	 * @return encoded string
	 */

}