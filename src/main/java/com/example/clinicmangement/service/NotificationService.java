package com.example.clinicmangement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.clinicmangement.model.Notification;
import com.example.clinicmangement.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(String email, String message, String type) {

        Notification notification =
                new Notification(email, message, type);

        notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(String email) {

        return notificationRepository
                .findByEmailOrderByCreatedAtDesc(email);
    }

    public List<Notification> getUnreadNotifications(String email) {

        return notificationRepository
                .findByEmailAndReadFalseOrderByCreatedAtDesc(email);
    }

    public long getUnreadCount(String email) {

        return notificationRepository
                .countByEmailAndReadFalse(email);
    }

    public void markAsRead(Long id) {

        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}