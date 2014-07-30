package com.lang.social.community.messages;

import com.lang.social.logic.User;

public class MessageItemChild {
	private User m_Sender;
	private String m_Content;
	
	public MessageItemChild(User i_sender, String i_Content) {
		m_Content = i_Content;
		m_Sender = i_sender;
	}
	
	public String getContent() {
		return m_Content;
	}
	
	public String getSenderFullName() {
		return m_Sender.getFullName();
	}
}
