package com.lang.social.competition;

import com.lang.social.competition.CompetitionGame.PlayerNumber;

public interface CompetitionActivityNotifier {
	public void OnPlayerAnsweredCorrect(PlayerNumber player);
	public void OnPlayerAnsweredWrong(PlayerNumber player);
	public void OnPlayersSwitchedTurns(PlayerNumber playerNum);
}
