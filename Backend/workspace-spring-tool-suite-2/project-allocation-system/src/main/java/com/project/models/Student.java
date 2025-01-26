package com.project.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Student {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer StudentId;
	private String Name;
	private String email;
	private String password;
	private List<String> Skills;
	private List<String> Achievements;
	private Integer GroupId;
	private Role role;
	
	public Student() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Student(Integer studentId, String name, String email, String password, List<String> skills,
			List<String> achievements, Integer groupId, Role role) {
		super();
		StudentId = studentId;
		Name = name;
		this.email = email;
		this.password = password;
		Skills = skills;
		Achievements = achievements;
		GroupId = groupId;
		this.role = role;
	}


	public Integer getStudentId() {
		return StudentId;
	}


	public void setStudentId(Integer studentId) {
		StudentId = studentId;
	}


	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public List<String> getSkills() {
		return Skills;
	}


	public void setSkills(List<String> skills) {
		Skills = skills;
	}


	public List<String> getAchievements() {
		return Achievements;
	}


	public void setAchievements(List<String> achievements) {
		Achievements = achievements;
	}


	public Integer getGroupId() {
		return GroupId;
	}


	public void setGroupId(Integer groupId) {
		GroupId = groupId;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}
	
}
