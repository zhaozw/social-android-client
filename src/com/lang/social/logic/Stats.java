package com.lang.social.logic;

import java.io.Serializable;

public class Stats implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4328637399477885788L;
	private String m_Language;
	private String m_Level;
	private int m_Points;
	
	public Stats(String i_Language, String i_Level, int i_Points) {
		m_Language = i_Language;
		m_Level = i_Level;
		m_Points = i_Points;
	}
	
	public String getLanguage() {
		return m_Language;
	}
	
	public String getLevel() {
		return m_Level;
	}
	
	
	public int getPoints() {
		return m_Points;
	}
}
