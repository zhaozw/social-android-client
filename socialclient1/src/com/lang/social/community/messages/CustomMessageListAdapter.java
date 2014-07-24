package com.lang.social.community.messages;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.lang.social.R;
import com.lang.social.community.general.UserProfileWindow;
import com.lang.social.controllers.ServerController;
import com.lang.social.logic.User;
import com.lang.social.utils.JSONUtils;

public class CustomMessageListAdapter extends BaseExpandableListAdapter  {
	private Context context;
	private LayoutInflater inflater;
	private int parentLayoutResourceId;
	private int childLayoutResourceId;
	private ArrayList<MessageItem> messagesList;
	
	private UserProfileWindow userProfileWindow;
	
	private int removedMessagePos = -1;
	
	public CustomMessageListAdapter(Context context, int layoutResourceId, int childLayoutResourceId, ArrayList<MessageItem> messagesList){
        this.parentLayoutResourceId = layoutResourceId;
        this.context = context;
        this.messagesList = messagesList;
        this.childLayoutResourceId = childLayoutResourceId;
        this.inflater = LayoutInflater.from(context);
	}
	
	@Override
    public MessageItemChild getChild(int groupPosition, int childPosition) {

            return messagesList.get(groupPosition).getMessageItemChildList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
    	return messagesList.get(groupPosition).getMessageItemChildList().size();
    }
    
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             final ViewGroup parent) {
            View resultView = convertView;
            MessageContentItemHolder holder;


            if (resultView == null) {
                    resultView = inflater.inflate(childLayoutResourceId, parent, false); 
                    
                    holder = new MessageContentItemHolder();
//                    holder.tvSender = (TextView) resultView.findViewById(R.id.tvSubListItemSenderName); 
                    holder.tvContent = (TextView) resultView.findViewById(R.id.tvMessageContent); 
                    resultView.setTag(holder);
            } else {
                    holder = (MessageContentItemHolder) resultView.getTag();
            }

            final MessageItemChild item = getChild(groupPosition, childPosition);

//            holder.tvSender.setText(item.getSenderFullName());
            holder.tvContent.setText(item.getContent());

            return resultView;
    }
    
    private static class MessageContentItemHolder {
		TextView tvSender;
		TextView tvContent;
     }

    @Override
    public MessageItem getGroup(int groupPosition) {
            return messagesList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
            return messagesList.size();
    }

    @Override
    public long getGroupId(final int groupPosition) {
            return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View theConvertView, ViewGroup parent) {
            View resultView = theConvertView;
            
            MessageItemHolder holder;

            final MessageItem messageItem = getGroup(groupPosition);
            final User sender = messageItem.getSender();
            
            final int pos = groupPosition;	//for final use only..
            
            if (resultView == null) {
                    resultView = inflater.inflate(parentLayoutResourceId, parent, false); 
                    holder = new MessageItemHolder();
                    holder.tvSenderName = (TextView) resultView.findViewById(R.id.tvListItemSenderName); 
                    holder.tvSubject = (TextView) resultView.findViewById(R.id.tvMessageSubject);
                    holder.tvDate = (TextView) resultView.findViewById(R.id.tvMessageDate);
                    holder.ivDeleteMessage = (ImageView) resultView.findViewById(R.id.ivDeleteMessage);
                    holder.ivViewProfileIcon = (ImageView) resultView.findViewById(R.id.ivViewProfileIcon);
                    holder.ivReplayIcon = (ImageView) resultView.findViewById(R.id.ivReplayMessage);
                    if(sender.getProfileID() != null){
                    	holder.ppv = (ProfilePictureView) resultView.findViewById(R.id.ivListItemSenderProfileImage);
                    	holder.ppv.setProfileId(sender.getProfileID());
                    }
                    
                    resultView.setTag(holder);
            }
            else {
                    holder = (MessageItemHolder) resultView.getTag();
            }
            
            final View view = resultView;	//For annoymous function..
            
            holder.tvSenderName.setText(sender.getFullName());
            holder.tvSubject.setText(messageItem.getSubject());
            
            //Delete Message OnClick Listener
            holder.ivDeleteMessage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showAlertPopUp(sender, messageItem, pos);
				}
			});
            
            //View Sender Profile OnClick Listener
            holder.ivViewProfileIcon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					userProfileWindow = new UserProfileWindow(context, view, sender);
		        	userProfileWindow.ShowProfileWindow();
					
				}
			});
            
            //Replay to Sender
            holder.ivReplayIcon.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showPopUpMessageDialog(view, sender);
					
				}
			});
            
            Date messageDate = messageItem.getDate();
            if(messageDate != null) {
            	holder.tvDate.setText(messageDate.toString());
            }
            
            
            return resultView;
    }
    
    private static class MessageItemHolder {
    	TextView tvSenderName;
    	TextView tvSubject;
    	TextView tvDate;
    	ImageView ivDeleteMessage;
    	ImageView ivViewProfileIcon;
    	ImageView ivReplayIcon;
    	ProfilePictureView ppv;
     }
    
    private void showPopUpMessageDialog(View anchorView, final User originalSender) {
		final View popupView = ((Activity) context).getLayoutInflater().inflate(R.layout.send_new_message_view, null);

	    final PopupWindow popupWindow = new PopupWindow(popupView, 
	                           LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	    // Example: If you have a TextView inside `popup_layout.xml`  
	    
	    if(originalSender.getProfileID() != null){
		    ProfilePictureView ppv = (ProfilePictureView) popupView.findViewById(R.id.ivReciverProfileImage);
		    ppv.setProfileId(originalSender.getProfileID());
		}
	    
	    Button btCancel = (Button) popupView.findViewById(R.id.btCancelMessage);
	    btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	    
	    Button btSendMessage = (Button) popupView.findViewById(R.id.btSendMessage);
	    btSendMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleMessageSent(popupView, originalSender);
				popupWindow.dismiss();
			}
		});
	    
	    TextView ttReciverName = (TextView) popupView.findViewById(R.id.tvReciverName);
	    ttReciverName.setText(originalSender.getFullName());

	    // If the PopupWindow should be focusable
	    popupWindow.setFocusable(true);

	    // If you need the PopupWindow to dismiss when when touched outside 
	    popupWindow.setBackgroundDrawable(new ColorDrawable());

	    int location[] = new int[2];

	    // Get the View's(the one that was clicked in the Fragment) location
	    anchorView.getLocationOnScreen(location);

	    // Using location, the PopupWindow will be displayed right under anchorView
	    popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
	}
    
    private void handleMessageSent(View view, User originalSender)
	{
		JSONObject msgDetails = new JSONObject();
		String isFacebookUser;
		String friendProfileID = null;
		String friendUserName = null;
		//Handling the receiver details
		if(originalSender.isFacebookUser() == true) {
			friendProfileID = originalSender.getProfileID();
			isFacebookUser = "true";
		}
		else {
			friendUserName = originalSender.getUserName();
			isFacebookUser = "false";
		}
		EditText subjectText = (EditText) view.findViewById(R.id.etSubjectText);
		String subject = subjectText.getText().toString();
		
		EditText contentText = (EditText) view.findViewById(R.id.etContentText);
		String content = contentText.getText().toString();
		
		JSONUtils.setStringValue(msgDetails, "facebookUser", isFacebookUser);
		JSONUtils.setStringValue(msgDetails, "profileid", friendProfileID);
		JSONUtils.setStringValue(msgDetails, "username", friendUserName);
		JSONUtils.setStringValue(msgDetails, "subject", subject);
		JSONUtils.setStringValue(msgDetails, "content", content);
		
		ServerController.sendJSONMessage("sendNewMessageRequest", msgDetails);
		
		
	}

    private void showAlertPopUp(final User sender, final MessageItem messageItem, final int pos) {
    	new AlertDialog.Builder(context)
	    .setTitle("Delete Message")
	    .setMessage("Delete Message From:" + " " + sender.getFullName() + "?")
	    .setPositiveButton("Delete!", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
				handleDeleteMessageEvent(sender, messageItem, pos);
	        }
	     })
	    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
    }
    
    public void handleDeleteMessageEvent(User sender, MessageItem messageItem, int pos) {
		JSONObject messageToDelete = new JSONObject();
		JSONUtils.setStringValue(messageToDelete, "msgid", messageItem.getMessageId());
		ServerController.sendJSONMessage("deleteMessageRequest", messageToDelete);
		removedMessagePos = pos;
	}

	public int getMessageToRemovePos() {
		if (removedMessagePos != -1) {
			int temp = removedMessagePos;
			removedMessagePos = -1;
			return temp;
		}
		else {	//returns -1
			return removedMessagePos;
		}
	}
    
    
    @Override
    public boolean hasStableIds() {
            return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
    }
    
    
    
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		View itemView = convertView;
//		
//		if(itemView == null){
//			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//			itemView = inflater.inflate(layoutResourceId, parent, false);
//		}
//		
//		MessageItem messageItem = messagesList.get(position);
//		User sender = messageItem.getSender();
//		
//		
//
//		if(sender.getProfileID() != null){
//		    ProfilePictureView ppv = (ProfilePictureView) itemView.findViewById(R.id.ivListItemSenderProfileImage);
//		    ppv.setProfileId(sender.getProfileID());
//		}
//
//		String fName = sender.getFirstName();
//		String lName = sender.getLastName();
//		TextView tvSenderName = (TextView) itemView.findViewById(R.id.tvListItemSenderName);
//		tvSenderName.setText(fName + " " + lName);
//		
//		String subject = messageItem.getSubject();
//		TextView tvSubject = (TextView) itemView.findViewById(R.id.tvMessageSubject);
//		tvSubject.setText(subject);
//		
//		Date messageDate = messageItem.getDate();
//		TextView tvDate = (TextView) itemView.findViewById(R.id.tvMessageDate);
//		tvDate.setText(messageDate.toString());
//		
//
//		return itemView;
//	}
}
