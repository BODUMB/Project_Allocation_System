package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.models.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}
