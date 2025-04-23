package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.models.Task;

public interface TaskRepository extends JpaRepository<Task, Integer>{

}
