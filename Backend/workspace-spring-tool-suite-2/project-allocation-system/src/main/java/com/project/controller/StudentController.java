package com.project.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.project.models.Faculty;
import com.project.models.Notification;
import com.project.models.Student;
import com.project.models.StudentGroup;
import com.project.repository.FacultyRepository;
import com.project.repository.GroupRepository;
import com.project.repository.NotificationRepository;
import com.project.repository.StudentRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/student")
public class StudentController {
	
	@Autowired	
	private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    // ✅ Get all faculties
    @GetMapping("/faculties")
    public List<Faculty> getFaculties() {
        return facultyRepository.findAll();
    }

    // ✅ Get all students
    @GetMapping("/getStudents")
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    // ✅ Get all groups
    @GetMapping("/getGroups")
    public List<StudentGroup> getGroups() {
        return groupRepository.findAll();
    }

    // ✅ Delete a student by ID
    @DeleteMapping("/delete/{studentId}")
    public String deleteStudent(@PathVariable Integer studentId) {
        studentRepository.deleteById(studentId);
        return "Student deleted successfully";
    }

    // ✅ Register new student
    @PostMapping("/register")
    public Student createStudent(@RequestBody Student student) {
        Student newStudent = new Student();
        newStudent.setName(student.getName());
        newStudent.setEmail(student.getEmail());
        newStudent.setPassword(passwordEncoder.encode(student.getPassword()));
        newStudent.setRole(student.getRole());

        if (student.getGroupId() != null) {
            newStudent.setGroupId(student.getGroupId());
        }

        return studentRepository.save(newStudent);
    }
    
    @PutMapping("/update/{email}")
    public ResponseEntity<Student> updateStudent(@PathVariable String email, @RequestBody Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findByEmail(email);

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            student.setName(studentDetails.getName());
            student.setEmail(studentDetails.getEmail());
            String encodedPassword = passwordEncoder.encode(studentDetails.getPassword());
            student.setPassword(encodedPassword);
            student.setRole(studentDetails.getRole());
            student.setSkills(studentDetails.getSkills());
            student.setAchievements(studentDetails.getAchievements());

            if (studentDetails.getGroupId() != null) {
                student.setGroupId(studentDetails.getGroupId());
            }

            Student updatedStudent = studentRepository.save(student);
            return ResponseEntity.ok(updatedStudent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Student login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Student student) {
        Optional<Student> existingStudent = studentRepository.findByEmail(student.getEmail());

        if (existingStudent.isPresent()) {
            Student fetchedStudent = existingStudent.get();

            if (passwordEncoder.matches(student.getPassword(), fetchedStudent.getPassword())) {
                return ResponseEntity.ok("Login Successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials. Please try again");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
    }

    // ✅ Form a new group
    @Transactional
    @PostMapping("/form-group")
    public ResponseEntity<String> formGroup(@RequestBody StudentGroup group) {

        // Check if any student is already in a group
        List<Integer> existingGroupMembers = group.getMembers().stream()
                .filter(studentId -> {
                    Optional<Student> studentOpt = studentRepository.findById(studentId);
                    return studentOpt.isPresent() && studentOpt.get().getGroupId() != null;
                })
                .collect(Collectors.toList());

        if (!existingGroupMembers.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Students " + existingGroupMembers + " are already part of other groups.");
        }

        // Save new group
        StudentGroup newGroup = new StudentGroup();
        newGroup.setGroupName(group.getGroupName());
        newGroup.setMembers(group.getMembers());
        newGroup.setProjectId(group.getProjectId());

        StudentGroup savedGroup = groupRepository.save(newGroup);

        // Update groupId for each student
        for (Integer studentId : group.getMembers()) {
            studentRepository.findById(studentId).ifPresent(student -> {
                student.setGroupId(savedGroup.getGroupId());
                studentRepository.save(student);
            });
        }

        groupRepository.flush();

        return ResponseEntity.ok("Group formed successfully.");
    }

    // ✅ Request a mentor (notification to faculty)
    @PostMapping("/request/{facultyName}")
    public String requestMentor(@RequestBody Notification notif, @PathVariable String facultyName) {

        try {
            Integer studentId = notif.getStudentId();

            if (studentId == null) {
                return "Student ID must be provided in the request body.";
            }

            Optional<Student> optionalStudent = studentRepository.findById(studentId);

            if (optionalStudent.isEmpty()) {
                return "Student with ID " + studentId + " not found.";
            }

            Student student = optionalStudent.get();

            if (student.getGroupId() == null) {
                return "No Group formed. User with name " + student.getName() + " is not in any group.";
            }

            Optional<Faculty> optionalFaculty = facultyRepository.findByName(facultyName.trim());

            if (optionalFaculty.isEmpty()) {
                return "Faculty with name '" + facultyName + "' not found.";
            }

            Faculty faculty = optionalFaculty.get();

            // Prepare notification
            notif.setStudentId(student.getStudentId());
            notif.setFacultyId(faculty.getFacultyID());
            notif.setNotificationId(null); // Ensure it's treated as a new notification

            if (notif.getStudentMessages() == null || notif.getStudentMessages().isEmpty()) {
                notif.setStudentMessages(List.of("Request sent by student ID " + student.getStudentId()));
            }

            notificationRepository.save(notif);

            return "Mentorship request notification sent successfully.";
        } catch (Exception e) {
            return "Failed to process mentorship request: " + e.getMessage();
        }
    }

}
