package com.lang.social.iocallback;

import org.json.JSONObject;

import android.util.Log;

import com.lang.social.teachstudy.lesson.LessonFragmentListener;
import com.lang.social.teachstudy.lesson.LessonGameListener;
import com.lang.social.teachstudy.lesson.PhotosTwoWayFragmentListener;

public class LessonIOCallBackHandler {
	private LessonGameListener m_CloseGameListener;
	private PhotosTwoWayFragmentListener m_PhotosTwoWayListener;
	private LessonFragmentListener m_LessonFragmentListener;
	
	public void setCloseActiveGameListener(LessonGameListener i_CloseGameListener) {
		m_CloseGameListener = i_CloseGameListener;
	}
	
	public void setPhotosTwoWayFragmentListener(PhotosTwoWayFragmentListener i_PhotosTwoWayFragmentListener) {
		m_PhotosTwoWayListener = i_PhotosTwoWayFragmentListener;
	}
	
	public void setLessonFragmentListener(LessonFragmentListener i_LessonFragmentListener) {
		m_LessonFragmentListener = i_LessonFragmentListener;
	}
	
	public void handleResponse(String eventName, JSONObject jsonResponse) {
		if (eventName.equals("ActiveStudentTeacherGameClosed")){
			if(m_CloseGameListener != null){
				m_CloseGameListener.onActiveGameClosed(jsonResponse);
			}
		}
		else if(eventName.equals("audioFileResponse")) {
			if(m_CloseGameListener != null){
				m_CloseGameListener.onAudioMessageRecived(jsonResponse);
			}
		}
		else if(eventName.equals("fileRequest")) {
			if(m_PhotosTwoWayListener != null){
				m_PhotosTwoWayListener.onImageRecived(jsonResponse);
			}
		}
		else if(eventName.equals("showImageResponse")) {
			if(m_LessonFragmentListener != null){
				m_LessonFragmentListener.onImageResponse(jsonResponse);
			}
			if(m_CloseGameListener != null){
				m_CloseGameListener.onImageResponse(jsonResponse);
			}
		}
		
	}
}
