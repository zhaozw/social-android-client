package com.lang.social.profile;

import java.io.Serializable;

import org.json.JSONObject;

import com.lang.social.logic.User;
import com.lang.social.utils.JSONUtils;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1987110894733571085L;
	public static final String subjectKEY = "subject";
	public static final String contentKEY = "content";
	public static final String dateKEY = "date";
	public static final String msgIdKEY = "messageId";
	public static final String senderKEY = "sender";
	
	private String senderFullName;
	private String senderProfileId;
	private String subject;
	private String content;
	private String date;
	private String messageId;
	private User sender;
	

	public Message(String senderFullName, String senderProfileId,
			String subject, String content, String date, String messageId,
			User sender) {
		this.senderFullName = senderFullName;
		this.senderProfileId = senderProfileId;
		this.subject = subject;
		this.content = content;
		this.date = date;
		this.messageId = messageId;
		this.sender = sender;
	}

	
	
	public Message(JSONObject json) {
		this.subject = JSONUtils.getStringFromJSON(json, subjectKEY, "error parsing subject");
		this.content = JSONUtils.getStringFromJSON(json, contentKEY, "error parsing content");
		this.date = JSONUtils.getStringFromJSON(json, dateKEY, "error parsing date");
		date = date.substring(0, 10);
		this.messageId = JSONUtils.getStringFromJSON(json, msgIdKEY, "error parsing message id");
		this.sender = new User(JSONUtils.getJSONObject(json, senderKEY));
		this.senderFullName = sender.getFullName();
	}



	public String getSenderProfileId() {
		return senderProfileId;
	}



	public void setSenderProfileId(String senderProfileId) {
		this.senderProfileId = senderProfileId;
	}



	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getMessageId() {
		return messageId;
	}



	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}



	public User getSender() {
		return sender;
	}



	public void setSender(User sender) {
		this.sender = sender;
	}



	public void setSenderFullName(String senderFullName) {
		this.senderFullName = senderFullName;
	}



	public String getSenderFullName() {
		return sender.getFullName();
	}
	
	public String getProfileId() {
		return sender.getProfileID();
	}
	
}
