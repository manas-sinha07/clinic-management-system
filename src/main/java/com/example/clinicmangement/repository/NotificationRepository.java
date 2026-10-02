package com.example.clinicmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinicmangement.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByEmailOrderByCreatedAtDesc(String email);

    List<Notification> findByEmailAndReadFalseOrderByCreatedAtDesc(String email);

    long countByEmailAndReadFalse(String email);
}