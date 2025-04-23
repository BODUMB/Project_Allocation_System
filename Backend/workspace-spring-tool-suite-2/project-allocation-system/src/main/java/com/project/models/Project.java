package com.project.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer projectId;
	
	private String groupName;
	
	private String projectTitle;
	
	private String domain;
	
	private String projectDescription;
	
	private LocalDate taskSubmissionDate;
	
	private LocalDate presentationDate;
	
	private LocalDate reportSubmissionDate;
	
	private LocalDate ieeeReportSubmissionDate;
	
	public Project() {
		// TODO Auto-generated constructor stub
	}

	public Project(Integer projectId, String groupName, String projectTitle, String domain, String projectDescription,
			LocalDate taskSubmissionDate, LocalDate presentationDate, LocalDate reportSubmissionDate,
			LocalDate ieeeReportSubmissionDate) {
		super();
		this.projectId = projectId;
		this.groupName = groupName;
		this.projectTitle = projectTitle;
		this.domain = domain;
		this.projectDescription = projectDescription;
		this.taskSubmissionDate = taskSubmissionDate;
		this.presentationDate = presentationDate;
		this.reportSubmissionDate = reportSubmissionDate;
		this.ieeeReportSubmissionDate = ieeeReportSubmissionDate;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getProjectTitle() {
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public LocalDate getTaskSubmissionDate() {
		return taskSubmissionDate;
	}

	public void setTaskSubmissionDate(LocalDate taskSubmissionDate) {
		this.taskSubmissionDate = taskSubmissionDate;
	}

	public LocalDate getPresentationDate() {
		return presentationDate;
	}

	public void setPresentationDate(LocalDate presentationDate) {
		this.presentationDate = presentationDate;
	}

	public LocalDate getReportSubmissionDate() {
		return reportSubmissionDate;
	}

	public void setReportSubmissionDate(LocalDate reportSubmissionDate) {
		this.reportSubmissionDate = reportSubmissionDate;
	}

	public LocalDate getIeeeReportSubmissionDate() {
		return ieeeReportSubmissionDate;
	}

	public void setIeeeReportSubmissionDate(LocalDate ieeeReportSubmissionDate) {
		this.ieeeReportSubmissionDate = ieeeReportSubmissionDate;
	}
	
	
	
}
