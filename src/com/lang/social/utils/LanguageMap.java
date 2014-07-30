package com.lang.social.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.lang.social.R;

public class LanguageMap {
	
	private static final Map<String, String> LanguagesToLocalMap = createMap();
	private static final Map<String, Integer> LanguagesToImageResMap = createLanguageResMap();
	
    private static Map<String, String> createMap() {
        Map<String, String> languagesToLocalMap = new HashMap<String, String>();
        languagesToLocalMap.put("Spanish", "es");
        languagesToLocalMap.put("Hebrew", "he");
        languagesToLocalMap.put("French", "fr");
        languagesToLocalMap.put("German", "de");
        return Collections.unmodifiableMap(languagesToLocalMap);
    }
    
    private static Map<String, Integer> createLanguageResMap() {
        Map<String, Integer> LanguagesToImageResMap = new HashMap<String, Integer>();
        LanguagesToImageResMap.put("Spanish", R.drawable.spain1);
        LanguagesToImageResMap.put("Hebrew", R.drawable.israel1);
        LanguagesToImageResMap.put("French", R.drawable.france1);
        LanguagesToImageResMap.put("German", R.drawable.germany1);
        LanguagesToImageResMap.put("Dutch", R.drawable.netherlands1);
        LanguagesToImageResMap.put("Italian", R.drawable.italy1);
        return Collections.unmodifiableMap(LanguagesToImageResMap);
    }
    
    public static Map<String, String> getLangnuageLocaleHashMap(){
    	return LanguagesToLocalMap;
    }
    
    public static Map<String, Integer> getLanguageResHashMap(){
    	return LanguagesToImageResMap;
    }
}
