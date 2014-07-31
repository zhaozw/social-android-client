package com.lang.social.memorygame;

import org.json.JSONObject;

public interface MemoryGameListener {
	void OnCardClick(int row, int col);
	void OnGameRoundRecieved(JSONObject gameRound);
}
