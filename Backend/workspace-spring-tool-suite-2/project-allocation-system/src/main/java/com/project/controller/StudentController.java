package com.project.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.models.Student;
import com.project.repository.StudentRepository;

@RestController 
public class StudentController {
	
	@Autowired
	 StudentRepository studentRepository;
	
	@PostMapping("/student/register")
	public Student createStudent(@RequestBody Student student) {
		Student newStudent = new Student();
		newStudent.setName(student.getName());
		newStudent.setEmail(student.getEmail());
		newStudent.setPassword(student.getPassword());
		newStudent.setGroupId(student.getGroupId());
		
		Student savedStudent = studentRepository.save(newStudent);
		
		return savedStudent;
		
	}
	
	@PostMapping("/student/login")
	public String login(@RequestBody Student student) {
		Optional<Student> existingStudent = studentRepository.findByEmail(student.getEmail());
		
		if(existingStudent.isPresent()) {
			Student fetchedStudent = existingStudent.get();
			if(fetchedStudent.getPassword().equals(student.getPassword())){
				return ("Login Succesfull");
			}
			else return ("Invalid Credentials. Please try again");
		}
		else {
			return ("Student not found");
		}
	}
	
}
