package com.project.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Entity
public class StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groupId;  

    private String groupName; 
    private List<Integer> members;

    // Separate marks for different components
    private List<Integer> taskMarks;
    private List<Integer> presentationMarks;
    private List<Integer> reportMarks; 

    private Integer facultyId;  
    private Integer projectId;

    @Version
    private Integer version;

    // ✅ Getters and Setters
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Integer> getMembers() {
        return members;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }

    public List<Integer> getTaskMarks() {
        return taskMarks;
    }

    public void setTaskMarks(List<Integer> taskMarks) {
        this.taskMarks = taskMarks;
    }

    public List<Integer> getPresentationMarks() {
        return presentationMarks;
    }

    public void setPresentationMarks(List<Integer> presentationMarks) {
        this.presentationMarks = presentationMarks;
    }

    public List<Integer> getReportMarks() {
        return reportMarks;
    }

    public void setReportMarks(List<Integer> reportMarks) {
        this.reportMarks = reportMarks;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}