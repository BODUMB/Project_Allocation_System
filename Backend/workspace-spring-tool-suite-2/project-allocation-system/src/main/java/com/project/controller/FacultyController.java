package com.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.models.Faculty;
import com.project.models.Notification;
import com.project.models.RequestStatus;
import com.project.models.Student;
import com.project.models.StudentGroup;
import com.project.repository.FacultyRepository;
import com.project.repository.GroupRepository;
import com.project.repository.NotificationRepository;
import com.project.repository.StudentRepository;
import com.project.services.EmailService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	FacultyRepository facultyRepository;
	
	@Autowired 
	GroupRepository groupRepository;
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@GetMapping("getMessages")
	public List<Notification> getNotification(){
		return notificationRepository.findAll();
	}
	
	@GetMapping("/getFaculties")
	public List<Faculty> getFaculty(){
		return facultyRepository.findAll();
	}
	
	@DeleteMapping("/delete/{facultyID}")
	public String deleteFaculty(@PathVariable Integer facultyID) {
		facultyRepository.deleteById(facultyID);
		return "Faculty Deleted Succesfully";
	}
	
	@PostMapping("/register")
	public Faculty createFaculty(@RequestBody Faculty faculty) {
		
		Faculty newFaculty = new Faculty();
		
		newFaculty.setName(faculty.getName());
		newFaculty.setEmail(faculty.getEmail());
		newFaculty.setPassword(faculty.getPassword());
		newFaculty.setSkills(faculty.getSkills());
		newFaculty.setExperience(faculty.getExperience());
		
		Faculty savedFaculty = facultyRepository.save(newFaculty);
		return savedFaculty;
		
	}
	
	@Transactional  // ✅ Makes sure all operations below succeed or fail together
	@PostMapping("/sendResponse")
	public ResponseEntity<String> approveRequest(@RequestBody Map<String, Object> payload) {
	    try {
	        // ✅ Extract data from request payload
	        Integer notificationId = (Integer) payload.get("notificationId");
	        String statusStr = (String) payload.get("status");
	        String message = (String) payload.get("message");

	        // ✅ Validate required parameters
	        if (notificationId == null || statusStr == null) {
	            return ResponseEntity.badRequest().body("Notification ID and Status are required.");
	        }

	        // ✅ Convert status from String to Enum
	        RequestStatus status;
	        try {
	            status = RequestStatus.valueOf(statusStr.toUpperCase());
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body("Invalid status. Use PENDING, APPROVED, or REJECTED.");
	        }

	        // ✅ Fetch the notification from DB
	        Optional<Notification> optionalNotif = notificationRepository.findById(notificationId);
	        if (optionalNotif.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found.");
	        }

	        Notification notification = optionalNotif.get();
	        Integer studentId = notification.getStudentId();

	        // ✅ Fetch the student from DB
	        Optional<Student> optionalStudent = studentRepository.findById(studentId);
	        if (optionalStudent.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with ID: " + studentId);
	        }

	        Student student = optionalStudent.get();
	        Integer groupId = student.getGroupId();

	        // ✅ Fetch the student group from DB
	        Optional<StudentGroup> optionalGroup = groupRepository.findById(groupId);
	        if (optionalGroup.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found with ID: " + groupId);
	        }

	        StudentGroup group = optionalGroup.get();
	        Integer facultyId = notification.getFacultyId();

	        // ✅ If APPROVED, assign faculty as the group's guide
	        if (status == RequestStatus.APPROVED) {
	            group.setFacultyId(facultyId);
	            groupRepository.save(group);  // ✅ Save updated group
	        }

	        // ✅ Update notification status
	        notification.setStatus(status);

	        // ✅ Append faculty message if provided
	        if (message != null && !message.isEmpty()) {
	            List<String> facultyMessages = notification.getFacultyMessages();
	            if (facultyMessages == null) {
	                facultyMessages = new ArrayList<>();
	            }
	            facultyMessages.add(message);
	            notification.setFacultyMessages(facultyMessages);
	        }

	        // ✅ Save updated notification
	        notificationRepository.save(notification);

	        // ✅ Fetch faculty details (optional, for email)
	        Optional<Faculty> optionalFaculty = facultyRepository.findById(facultyId);
	        if (optionalFaculty.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty not found with ID: " + facultyId);
	        }

	        Faculty faculty = optionalFaculty.get();

	        // ✅ Prepare email content based on status
	        String toEmail = student.getEmail();
	        String subject = "Mentorship Request " + statusStr;
	        StringBuilder bodyBuilder = new StringBuilder();

	        if (status == RequestStatus.APPROVED) {
	            bodyBuilder.append("Hello ").append(student.getName()).append(",\n\n")
	                .append("Your mentorship request has been APPROVED by ").append(faculty.getName()).append(".\n\n");
	        } else if (status == RequestStatus.REJECTED) {
	            bodyBuilder.append("Hello ").append(student.getName()).append(",\n\n")
	                .append("Your mentorship request has been REJECTED by ").append(faculty.getName()).append(".\n\n");
	        }

	        if (message != null && !message.isEmpty()) {
	            bodyBuilder.append("Message from faculty: ").append(message).append("\n\n");
	        }

	        bodyBuilder.append("Thank you!");

	        // ✅ Send email notification to student
	        emailService.sendEmail(toEmail, subject, bodyBuilder.toString());

	        // ✅ Return success message
	        return ResponseEntity.ok("Mentorship request updated to " + status);

	    } catch (Exception e) {
	        // ✅ Return error message on failure
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to update mentorship request: " + e.getMessage());
	    }
	}


	
	@PostMapping("/uploadMarks")
	public ResponseEntity<String> uploadMarks(
	        @RequestParam Integer groupId,
	        @RequestBody List<Integer> marks) {

	    Optional<StudentGroup> optionalGroup = groupRepository.findById(groupId);

	    if (!optionalGroup.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
	    }

	    StudentGroup group = optionalGroup.get();

	    if (group.getMembers().size() != marks.size()) {
	        return ResponseEntity.badRequest().body("Marks list size mismatch with members");
	    }

	    group.setMarks(marks);

	    groupRepository.save(group);

	    return ResponseEntity.ok("Marks uploaded successfully");
	}
 
}
