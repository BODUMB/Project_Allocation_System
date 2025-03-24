package com.project.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer notificationId;
	private Integer studentId;
	private Integer facultyId;
	private List<String> facultyMessages;
	private List<String> studentMessages;
	private RequestStatus status;
	
	
	public Notification() {
		// TODO Auto-generated constructor stub
	}


	public Notification(Integer notificationId, Integer studentId, Integer facultyId, List<String> facultyMessages,
			List<String> studentMessages, RequestStatus status) {
		super();
		this.notificationId = notificationId;
		this.studentId = studentId;
		this.facultyId = facultyId;
		this.facultyMessages = facultyMessages;
		this.studentMessages = studentMessages;
		this.status = status;
	}



	public Integer getNotificationId() {
		return notificationId;
	}


	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}


	public Integer getStudentId() {
		return studentId;
	}


	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}


	public Integer getFacultyId() {
		return facultyId;
	}


	public void setFacultyId(Integer facultyId) {
		this.facultyId = facultyId;
	}


	public List<String> getFacultyMessages() {
		return facultyMessages;
	}


	public void setFacultyMessages(List<String> facultyMessages) {
		this.facultyMessages = facultyMessages;
	}


	public List<String> getStudentMessages() {
		return studentMessages;
	}


	public void setStudentMessages(List<String> studentMessages) {
		this.studentMessages = studentMessages;
	}


	public RequestStatus getStatus() {
		return status;
	}


	public void setStatus(RequestStatus status) {
		this.status = status;
	}
	
	
	
	
	
}
