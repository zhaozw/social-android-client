package com.lang.social.iocallback;

import org.json.JSONObject;

import com.lang.social.room.RoomConstants;
import com.lang.social.teachstudy.StudentTeacherServerListener;
import com.lang.social.teachstudy.students.StudentsServerListener;
import com.lang.social.teachstudy.teachers.TeachersServerListener;

public class StudentTeacherIOCallBackHandler {
	private StudentTeacherServerListener m_StudentTeacherserverListener;
	private StudentsServerListener m_StudentsServerListener;
	private TeachersServerListener m_TeachersServerListener;
	
	public void setStudentTeacherServerListener(StudentTeacherServerListener studentTeacherServerListener){
		m_StudentTeacherserverListener = studentTeacherServerListener;
	}
	
	public void setStudentsServerListener(StudentsServerListener studentsListener){
		m_StudentsServerListener = studentsListener;
	}
	
	public void setTeachersServerListener(TeachersServerListener teachersListener){
		m_TeachersServerListener = teachersListener;
	}
	
	public void handleResponse(String eventName, JSONObject jsonResponse) {
		if (eventName.equals("startNewTeacherGameRespone") || eventName.equals("startNewStudentGameRespone")){
			if(m_StudentTeacherserverListener != null){
				m_StudentTeacherserverListener.onNewGameOpenedRespone(jsonResponse);
			}
		}
		
		else if(eventName.equals("studentsGameHostsResponse")) {
			if(m_StudentsServerListener != null){
				m_StudentsServerListener.onFetchStudentsGamesHosts(jsonResponse);
			}
		}
		
		else if(eventName.equals("teachersGameHostsResponse")) {
			if(m_TeachersServerListener != null){
				m_TeachersServerListener.onFetchTeachersGamesHosts(jsonResponse);
			}
		}
		
		else if(eventName.equals(RoomConstants.teacherJoinedStudentGameResponse)){
			if(m_StudentsServerListener != null){
				m_StudentsServerListener.OnTeacherJoinStudentGameResponse(jsonResponse);
			}
		}
		
		else if(eventName.equals(RoomConstants.studentJoinedTeacherGameResponse)){
			if(m_TeachersServerListener != null){
				m_TeachersServerListener.OnStudentJoinTeacherGameResponse(jsonResponse);
			}
		}
		
	}
}

