package com.project.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.project.models.Project;
import com.project.models.RequestStatus;
import com.project.models.Student;
import com.project.models.StudentGroup;
import com.project.models.Task;
import com.project.repository.FacultyRepository;
import com.project.repository.GroupRepository;
import com.project.repository.NotificationRepository;
import com.project.repository.ProjectRepository;
import com.project.repository.StudentRepository;
import com.project.repository.TaskRepository;
import com.project.services.EmailService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/faculty")
@CrossOrigin(origins = "http://127.0.0.1:5501", allowCredentials = "true")
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
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	TaskRepository taskRepository;
	
	@GetMapping("getMessages")
	public List<Notification> getNotification(){
		return notificationRepository.findAll();
	}
	
	@GetMapping("/getFaculties")
	public List<Faculty> getFaculty(){
		return facultyRepository.findAll();
	}
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile(HttpSession session) {
	    System.out.println("Session ID: " + session.getId());
	    Object faculty = session.getAttribute("faculty");
	    if (faculty == null) {
	        System.out.println("No faculty found in session");
	        return ResponseEntity.status(401).body("Not logged in");
	    }
	    return ResponseEntity.ok(faculty);
	}	
	
	@GetMapping("/getProjects")
	public List<Project> getProjects(){
		return projectRepository.findAll();
	}
	
	@GetMapping("/getProjectsByGroup")
	public Optional<Project> getProjects(@RequestParam String groupName){
		return projectRepository.findByGroupName(groupName);
	}
	
	@DeleteMapping("/deleteProject")
	public ResponseEntity<String> deleteProject(@RequestBody Map<String, Object> payload) {
	    String groupName = (String) payload.get("groupName");
	    String projectTitle = (String) payload.get("projectTitle");

	    // Check if the project exists with the given group name and project name
	    Optional<Project> existingProject = projectRepository.findByGroupNameAndProjectTitle(groupName, projectTitle);

	    if (existingProject.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found for the given group and project name.");
	    }

	    // Delete the project
	    projectRepository.delete(existingProject.get());

	    return ResponseEntity.ok("Project deleted successfully.");
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
		newFaculty.setRole(faculty.getRole());
		
		Faculty savedFaculty = facultyRepository.save(newFaculty);
		return savedFaculty;
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
	    String email = credentials.get("email");
	    String password = credentials.get("password");

	    Optional<Faculty> optionalFaculty = facultyRepository.findByEmail(email);

	    if (optionalFaculty.isEmpty()) {
	        return ResponseEntity.status(404).body("Faculty not found");
	    }

	    Faculty faculty = optionalFaculty.get();

	    // Plain password comparison (since you’re not hashing)
	    if (!faculty.getPassword().equals(password)) {
	        return ResponseEntity.status(401).body("Invalid credentials");
	    }

	    // Store faculty in session
	    session.setAttribute("faculty", faculty);

	    // Remove password before sending response
	    faculty.setPassword(null);

	    // Return profile directly
	    return ResponseEntity.ok(faculty);
	}


	
	@PutMapping("/updateByEmail/{email}")
	public ResponseEntity<Faculty> updateFacultyByEmail(@PathVariable String email, @RequestBody Faculty facultyDetails) {
	    Optional<Faculty> optionalFaculty = facultyRepository.findByEmail(email);

	    if (optionalFaculty.isPresent()) {
	        Faculty faculty = optionalFaculty.get();

	        faculty.setName(facultyDetails.getName());
	        faculty.setEmail(facultyDetails.getEmail());
	        faculty.setPassword(facultyDetails.getPassword());
	        faculty.setSkills(facultyDetails.getSkills());
	        faculty.setExperience(facultyDetails.getExperience());
	        faculty.setRole(facultyDetails.getRole());

	        Faculty updatedFaculty = facultyRepository.save(faculty);
	        return ResponseEntity.ok(updatedFaculty);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}


	
	@PostMapping("/assignProject")
	public ResponseEntity<String> assignProject(@RequestParam String groupName,@RequestBody Project project) {
		
		Optional<StudentGroup> existingGroup= groupRepository.findByGroupName(groupName);
		
		if (existingGroup.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group name not found in StudentGroup.");
	    }
		
		Project newProject = new Project();
		
		newProject.setGroupName(existingGroup.get().getGroupName());
	    newProject.setProjectTitle(project.getProjectTitle());	
	    newProject.setDomain(project.getDomain());
	    newProject.setProjectDescription(project.getProjectDescription());
	    newProject.setTaskSubmissionDate(project.getTaskSubmissionDate());
	    newProject.setPresentationDate(project.getPresentationDate());
	    newProject.setReportSubmissionDate(project.getReportSubmissionDate());
	    newProject.setIeeeReportSubmissionDate(project.getIeeeReportSubmissionDate());
	    
	    Project savedProject = projectRepository.save(newProject);
	    return ResponseEntity.ok("Project assigned successfully with ID: " + savedProject.getProjectId());
	}
	
	@PostMapping("/scheduleMeeting")
	public ResponseEntity<String> scheduleMeeting(@RequestBody Map<String, Object> payload) {
	    try {
	        // ✅ Extract Data from Payload
	        String groupName = (String) payload.get("groupName");
	        String meetingDateStr = (String) payload.get("meetingDate");
	        String meetingTimeStr = (String) payload.get("meetingTime");
	        String description = (String) payload.get("description");

	        // ✅ Validate Input
	        if (groupName == null || meetingDateStr == null || meetingTimeStr == null || description == null) {
	            return ResponseEntity.badRequest().body("All fields are required.");
	        }

	        // ✅ Parse Date and Time
	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
	        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

	        LocalDate meetingDate;
	        LocalTime meetingTime;

	        try {
	            meetingDate = LocalDate.parse(meetingDateStr, dateFormatter);
	        } catch (DateTimeParseException e) {
	            return ResponseEntity.badRequest().body("Invalid date format. Expected format: MM/dd/yy. Received: " + meetingDateStr);
	        }

	        try {
	            meetingTime = LocalTime.parse(meetingTimeStr, timeFormatter);
	        } catch (DateTimeParseException e) {
	            return ResponseEntity.badRequest().body("Invalid time format. Expected format: hh:mm AM/PM. Received: " + meetingTimeStr);
	        }

	        // ✅ Fetch Group by Name
	        Optional<StudentGroup> optionalGroup = groupRepository.findByGroupName(groupName);
	        if (optionalGroup.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found.");
	        }

	        StudentGroup group = optionalGroup.get();
	        List<Integer> memberIds = group.getMembers();

	        // ✅ Collect Member Emails
	        List<String> memberEmails = new ArrayList<>();
	        for (Integer memberId : memberIds) {
	            Optional<Student> optionalStudent = studentRepository.findById(memberId);
	            optionalStudent.ifPresent(student -> memberEmails.add(student.getEmail()));
	        }

	        if (memberEmails.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No group members found.");
	        }

	        // ✅ Prepare Email Content
	        String subject = "Meeting Scheduled: " + groupName;
	        StringBuilder emailBody = new StringBuilder();
	        emailBody.append("Dear Group Members,\n\n");
	        emailBody.append("A meeting has been scheduled with the following details:\n");
	        emailBody.append("Date: ").append(meetingDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))).append("\n");
	        emailBody.append("Time: ").append(meetingTime.format(DateTimeFormatter.ofPattern("hh:mm a"))).append("\n");
	        emailBody.append("Description: ").append(description).append("\n\n");
	        emailBody.append("Please be on time.\n\n");
	        emailBody.append("Best Regards,\n");
	        emailBody.append("Admin Team");

	        // ✅ Send Emails
	        for (String email : memberEmails) {
	            emailService.sendEmail(email, subject, emailBody.toString());
	        }

	        // ✅ Return Success Response
	        return ResponseEntity.ok("Meeting scheduled successfully and emails sent to all group members!");

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Schedule a Meeting: " + e.getMessage());
	    }
	}

	
	@Transactional  // Ensures atomicity
	@PostMapping("/sendResponse")
	public ResponseEntity<String> approveRequest(@RequestBody Map<String, Object> payload) {
	    try {
	        Integer notificationId = (Integer) payload.get("notificationId");
	        String statusStr = (String) payload.get("status");
	        String message = (String) payload.get("message");

	        if (notificationId == null || statusStr == null) {
	            return ResponseEntity.badRequest().body("Notification ID and Status are required.");
	        }

	        RequestStatus status;
	        try {
	            status = RequestStatus.valueOf(statusStr.toUpperCase());
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body("Invalid status. Use PENDING, APPROVED, or REJECTED.");
	        }

	        Optional<Notification> optionalNotif = notificationRepository.findById(notificationId);
	        if (optionalNotif.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found.");
	        }

	        Notification notification = optionalNotif.get();
	        Integer studentId = notification.getStudentId();

	        Optional<Student> optionalStudent = studentRepository.findById(studentId);
	        if (optionalStudent.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found with ID: " + studentId);
	        }

	        Student student = optionalStudent.get();
	        Integer groupId = student.getGroupId();

	        Optional<StudentGroup> optionalGroup = groupRepository.findById(groupId);
	        if (optionalGroup.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found with ID: " + groupId);
	        }

	        StudentGroup group = optionalGroup.get();
	        Integer facultyId = notification.getFacultyId();

	        if (status == RequestStatus.APPROVED) {
	            group.setFacultyId(facultyId);
	            groupRepository.save(group);
	        }

	        notification.setStatus(status);
	        if (message != null && !message.isEmpty()) {
	            List<String> facultyMessages = notification.getFacultyMessages();
	            if (facultyMessages == null) {
	                facultyMessages = new ArrayList<>();
	            }
	            facultyMessages.add(message);
	            notification.setFacultyMessages(facultyMessages);
	        }
	        notificationRepository.save(notification);

	        Optional<Faculty> optionalFaculty = facultyRepository.findById(facultyId);
	        if (optionalFaculty.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty not found with ID: " + facultyId);
	        }

	        Faculty faculty = optionalFaculty.get();

	        // ✅ Send email to all group members
	        String subject = "Mentorship Request " + statusStr;
	        StringBuilder bodyBuilder = new StringBuilder();
	        
	        if (status == RequestStatus.APPROVED) {
	            bodyBuilder.append("Hello Team,\n\n")
	                .append("Your mentorship request has been APPROVED by ").append(faculty.getName()).append(".\n\n");
	        } else if (status == RequestStatus.REJECTED) {
	            bodyBuilder.append("Hello Team,\n\n")
	                .append("Your mentorship request has been REJECTED by ").append(faculty.getName()).append(".\n\n");
	        }

	        if (message != null && !message.isEmpty()) {
	            bodyBuilder.append("Message from faculty: ").append(message).append("\n\n");
	        }

	        bodyBuilder.append("Thank you!");

	        // ✅ Fetch all group members
	        List<Integer> memberIds = group.getMembers();
	        List<String> recipientEmails = new ArrayList<>();

	        for (Integer memberId : memberIds) {
	            Optional<Student> groupMemberOpt = studentRepository.findById(memberId);
	            if (groupMemberOpt.isPresent()) {
	                recipientEmails.add(groupMemberOpt.get().getEmail());
	            }
	        }

	        // ✅ Send emails to all members
	        for (String email : recipientEmails) {
	            emailService.sendEmail(email, subject, bodyBuilder.toString());
	        }

	        return ResponseEntity.ok("Mentorship request updated and notifications sent to group members.");

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to update mentorship request: " + e.getMessage());
	    }
	}

	
	@PostMapping("/assignTask")
	public ResponseEntity<String> createTask(
	        @RequestParam String groupName,
	        @RequestParam String projectTitle,
	        @RequestBody Task task) {

	    // Check if the group exists
	    Optional<StudentGroup> optionalGroup = groupRepository.findByGroupName(groupName);
	    if (!optionalGroup.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
	    }

	    // Check if the project exists
	    Optional<Project> optionalProject = projectRepository.findByProjectTitle(projectTitle);
	    if (!optionalProject.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
	    }

	    // Set the group and project references in the task
	    task.setGroupName(optionalGroup.get().getGroupName());
	    task.setProjectName(optionalProject.get().getProjectTitle());

	    // Save the task
	    taskRepository.save(task);

	    return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully");
	}


	
	@PostMapping("/uploadMarks")
	public ResponseEntity<String> uploadAllMarks(
	        @RequestParam Integer groupId,
	        @RequestBody Map<String, List<Integer>> marks) {

	    Optional<StudentGroup> optionalGroup = groupRepository.findById(groupId);

	    if (!optionalGroup.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
	    }

	    StudentGroup group = optionalGroup.get();

	    // Validate size for all marks if provided
	    if (marks.containsKey("taskMarks") && group.getMembers().size() != marks.get("taskMarks").size()) {
	        return ResponseEntity.badRequest().body("Task marks size mismatch with members");
	    }
	    if (marks.containsKey("presentationMarks") && group.getMembers().size() != marks.get("presentationMarks").size()) {
	        return ResponseEntity.badRequest().body("Presentation marks size mismatch with members");
	    }
	    if (marks.containsKey("reportMarks") && group.getMembers().size() != marks.get("reportMarks").size()) {
	        return ResponseEntity.badRequest().body("Report marks size mismatch with members");
	    }

	    // Set marks if provided
	    if (marks.containsKey("taskMarks")) {
	        group.setTaskMarks(marks.get("taskMarks"));
	    }
	    if (marks.containsKey("presentationMarks")) {
	        group.setPresentationMarks(marks.get("presentationMarks"));
	    }
	    if (marks.containsKey("reportMarks")) {
	        group.setReportMarks(marks.get("reportMarks"));
	    }

	    groupRepository.save(group);

	    return ResponseEntity.ok("All marks uploaded successfully");
	}

	
	
 
}
