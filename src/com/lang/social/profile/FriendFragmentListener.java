package com.lang.social.profile;

import com.lang.social.logic.User;


public interface FriendFragmentListener {
	void OnFriendProfileViewRequestClick(User friend);
	void OnMessagesViewRequestClick();
}
