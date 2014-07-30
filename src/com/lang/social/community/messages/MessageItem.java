package com.lang.social.community.messages;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.lang.social.logic.User;

public class MessageItem {
	private User sender;
	private String m_Subject;
	private Date m_Date;
	private String m_Content;
	private String msgID;
	private List<MessageItemChild> m_MessageItemChild;
	
	public MessageItem(JSONObject userJson, String i_Subject, Date i_Date, String i_Content, String i_MsgId) {
		sender = new User(userJson);
		m_Subject = i_Subject;
		m_Date = i_Date;
		m_Content = i_Content;
		msgID = i_MsgId;
		m_MessageItemChild = new ArrayList<MessageItemChild>();
	}

	public User getSender() {
		return sender;
	}
	
	public String getSubject() {
		return m_Subject;
	}
	
	public Date getDate() {
		return m_Date;
	}
	
	public String getContent() {
		return m_Content;
	}
	
	public List<MessageItemChild> getMessageItemChildList() {
		return m_MessageItemChild;
	}
	
	public String getMessageId() {
		return msgID;
	}
}
