package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.models.StudentGroup;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<StudentGroup, Integer> {

//    @Query(value = "SELECT * FROM student_group g WHERE :studentId = ANY (g.members)", nativeQuery = true)
//    Optional<StudentGroup> findByStudentId(@Param("studentId") Integer studentId);
	  public Optional<StudentGroup> findByFacultyId(Integer facultyId);
	  public Optional<StudentGroup> findByGroupName(String groupName);
}
