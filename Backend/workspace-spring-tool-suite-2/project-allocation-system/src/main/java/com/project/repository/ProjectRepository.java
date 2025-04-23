package com.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.models.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer>{
	public Optional<Project> findByProjectTitle(String projectTitle);
	public Optional<Project> findByGroupName(String groupName);
	public Optional<Project> findByGroupNameAndProjectTitle(String groupName, String projectTitle);
}
