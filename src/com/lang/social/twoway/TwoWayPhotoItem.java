package com.lang.social.twoway;

import android.graphics.Bitmap;

public class TwoWayPhotoItem {
	
    private String title;
    private Bitmap imageRes;
    
	public TwoWayPhotoItem(String title, Bitmap imageRes) {
		this.imageRes = imageRes;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public Bitmap getImageRes() {
		return imageRes;
	}
}
