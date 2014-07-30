package com.lang.social.chat;

import android.graphics.Color;


public class ChatLineItem {

	int resIdcolor;
	String sender;
	String msg;
	
	public ChatLineItem(String sender, String msg, int color){
		this.sender = sender;
		this.msg = msg;
		this.resIdcolor = color;
	}

	public int getResColor() {
		return resIdcolor;
	}

	
	public String getSender() {
		return sender;
	}

	public String getMsg() {
		return msg;
	}
	
}
