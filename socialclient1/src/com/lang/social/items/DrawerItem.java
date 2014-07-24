package com.lang.social.items;

import android.graphics.Bitmap;


public class DrawerItem {
	String ItemName = null;
	int imgResID = 0;
	String title = null;
	String profileID = null;
	boolean isLanguageItem = false;
	boolean isProfileImage = false;
	LanguageItem languageItem = null;
	private Bitmap userImageBitmap = null;
	private boolean isFacebookImage = false;
	
	public DrawerItem(String itemName, int imgResID) {
		ItemName = itemName;
		this.imgResID = imgResID;
	}

	public DrawerItem(Bitmap userImageBitmap) {
		isProfileImage = true;
		this.userImageBitmap = userImageBitmap;
	}
	
	public DrawerItem(String profileID) {
		isProfileImage = true;
		isFacebookImage = true;
		this.profileID = profileID;
	}
	
	public DrawerItem(LanguageItem languageItem) {
		isLanguageItem = true;
		this.languageItem = languageItem;
	}

	public boolean isFacebookImage() {
		return isFacebookImage;
	}

	public void setFacebookImage(boolean isFacebookImage) {
		this.isFacebookImage = isFacebookImage;
	}
	
	public Bitmap getUserImageBitmap() {
		return userImageBitmap;
	}

	public void setUserImageBitmap(Bitmap userImageBitmap) {
		this.userImageBitmap = userImageBitmap;
	}

	public String getProfileID() {
		return profileID;
	}

	public void setProfileID(String profileID) {
		this.profileID = profileID;
	}

	public LanguageItem getLanguageItem() {
		return languageItem;
	}

	public boolean isLanguageItem() {
		return isLanguageItem;
	}
	

	public boolean isProfileImage() {
		return isProfileImage;
	}

	public void setProfileImage(boolean isProfileImage) {
		this.isProfileImage = isProfileImage;
	}

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public int getImgResID() {
		return imgResID;
	}
	
	public void setImgResID(int imgResID) {
		this.imgResID = imgResID;
	}

}