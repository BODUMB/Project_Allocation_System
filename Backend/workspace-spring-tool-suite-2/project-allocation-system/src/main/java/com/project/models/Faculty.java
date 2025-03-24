package com.project.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Faculty {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer facultyID;
	private String name;
	private String email;
	private String password;
	private List<String> skills;
	private String Experience;
	private Role role;
	
	public Faculty() {
		// TODO Auto-generated constructor stub
	}

	public Faculty(Integer facultyID, String name, String email, String password, List<String> skills,
			String experience, Role role) {
		super();
		this.facultyID = facultyID;
		this.name = name;
		this.email = email;
		this.password = password;
		this.skills = skills;
		Experience = experience;
		this.role = role;
	}

	public Integer getFacultyID() {
		return facultyID;
	}

	public void setFacultyID(Integer facultyID) {
		this.facultyID = facultyID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}

	public String getExperience() {
		return Experience;
	}

	public void setExperience(String experience) {
		Experience = experience;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}	
	

}
