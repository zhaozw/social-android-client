package com.lang.social.iocallback;

import org.json.JSONObject;

import com.lang.social.community.chat.CommunityChatServerListener;
import com.lang.social.community.friends.FriendsServerListener;
import com.lang.social.community.general.GeneralCommunityServerListener;
import com.lang.social.community.messages.MessagesServerListener;
import com.lang.social.community.onlineusers.OnlineUsersServerListener;

public class CommunityIOCallBackHandler {
	private FriendsServerListener mFriendsListener;
	private MessagesServerListener mMessagesListener;
	private GeneralCommunityServerListener mGeneralCommunityListener;
	private OnlineUsersServerListener mOnlineUsersListener;
	private CommunityChatServerListener mChatServerListener;
	
	public void setFriendsListener(FriendsServerListener friendsListener){
		mFriendsListener = friendsListener;
	}
	
	public void setMessagesListener(MessagesServerListener messagesListener){
		mMessagesListener = messagesListener;
	}
	
	public void setGeneralCommunityListener(GeneralCommunityServerListener generalCommunityListener){
		mGeneralCommunityListener = generalCommunityListener;
	}
	
	
	public void setOnlineUsersServerListener(OnlineUsersServerListener onlineUsersListener){
		mOnlineUsersListener = onlineUsersListener;
	}
	
	public void setCommunityChatServerListener(CommunityChatServerListener ChatServerListener){
		  mChatServerListener = ChatServerListener;
	}
	
	public void handleResponse(String eventName, JSONObject jsonResponse) {
		if (eventName.equals("friendsListResponse")){
			if(mFriendsListener != null){
				mFriendsListener.onFriendsListRespone(jsonResponse);
			}
		}
		
		else if (eventName.equals("messageListResponse")){
			if(mMessagesListener != null){
				mMessagesListener.onMessagesListRespone(jsonResponse);
			}
		}
		
		else if (eventName.equals("unfriendResponse")){
			if(mFriendsListener != null){
				mFriendsListener.onUnfriedRespone(jsonResponse);
			}
		}
		
		else if (eventName.equals("sendNewMessageResponse")){
			if(mFriendsListener != null){
				mFriendsListener.onMessageSentRespone(jsonResponse);
			}
		}
		
		else if (eventName.equals("friendsRequestsListRespone")){
			if(mFriendsListener != null){
				mFriendsListener.onFriendsRequestListRespone(jsonResponse);
			}
		}
		
		else if (eventName.equals("ignoreFriendRespone")){
			if(mFriendsListener != null){
				mFriendsListener.onIgnoreFriendRequest(jsonResponse);
			}
		}
		
		else if (eventName.equals("acceptFriendRequestRespone")){
			if(mFriendsListener != null){
				mFriendsListener.onAcceptFriendRequestRespone(jsonResponse);
			}
		}
		
		else if (eventName.equals("isFriendsRespone")){
			if(mGeneralCommunityListener != null){
				mGeneralCommunityListener.onIsFriendsRespone(jsonResponse);
			}
		}
		
		else if (eventName.equals("friendRequestRespone")){
			if(mGeneralCommunityListener != null){
				mGeneralCommunityListener.onFriendRequestRespone(jsonResponse);
			}
		}
		
		else if (eventName.equals("deleteMessageResponse")){
			if(mMessagesListener != null){
				mMessagesListener.onMesssageDeleteRespone(jsonResponse);
			}
		}
		
		else if (eventName.equals("sendMessageConfirmationResponse")){
			if(mMessagesListener != null){
				mMessagesListener.OnMessageSentResponse(jsonResponse);
			}
		}
		
		else if (eventName.equals("onlineUsersResponse")){
			if(mOnlineUsersListener != null){
				mOnlineUsersListener.onOnlineUserListRespone(jsonResponse);
			}
		} 
		
		else if (eventName.equals("newCommunityChatMessage")){
			   if(mChatServerListener != null){
				    mChatServerListener.onChatMsgRecived(jsonResponse);
			}
		}
	}
}
