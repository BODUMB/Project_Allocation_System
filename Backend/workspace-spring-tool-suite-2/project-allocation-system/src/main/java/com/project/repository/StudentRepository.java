package com.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.models.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
	
	public Optional<Student> findByEmail(String email);
}
