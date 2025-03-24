package com.project.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.models.StudentGroup;
import com.project.repository.GroupRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	GroupRepository groupRepository;
	
	@GetMapping("/getGroups/{facultyId1}")
	public ResponseEntity<StudentGroup> monitor(@PathVariable Integer facultyId1) {
	    return groupRepository.findByFacultyId(facultyId1)
	            .map(ResponseEntity::ok)                    // If found, return 200 OK
	            .orElse(ResponseEntity.notFound().build()); // If not found, return 404
	}
	
	@PostMapping("/reallocateGroup")
	public ResponseEntity<String> change(@RequestBody Map<String, Object> payload) {

	    try {
	        Integer newFacultyId = (Integer) payload.get("newFacultyId");
	        Integer groupId = (Integer) payload.get("GroupId");

	        if (newFacultyId == null || groupId == null) {
	            return ResponseEntity.badRequest().body("Both newFacultyId and GroupId are required.");
	        }

	        Optional<StudentGroup> optionalGroup = groupRepository.findById(groupId);

	        if (optionalGroup.isEmpty()) {
	            return ResponseEntity.status(404).body("Group with ID " + groupId + " not found.");
	        }

	        StudentGroup group = optionalGroup.get();

	        // Set the new faculty ID
	        group.setFacultyId(newFacultyId);

	        // Save the updated group
	        groupRepository.save(group);

	        return ResponseEntity.ok("Group reallocated Successfully !!!");

	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
	    }
	}


}
