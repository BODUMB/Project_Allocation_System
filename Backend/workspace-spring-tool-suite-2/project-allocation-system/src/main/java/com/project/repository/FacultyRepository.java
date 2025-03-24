package com.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.models.Faculty;
import com.project.models.Student;

public interface FacultyRepository extends JpaRepository<Faculty, Integer>{
	
	public Optional<Faculty> findByName(String name);
}
