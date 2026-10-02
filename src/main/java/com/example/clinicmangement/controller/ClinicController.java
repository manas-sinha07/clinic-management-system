package com.example.clinicmangement.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.clinicmangement.model.AppointmentStatus;
import com.example.clinicmangement.service.AppointmentService;
import com.example.clinicmangement.service.NotificationService;

@Controller
public class ClinicController {

    private final AppointmentService appointmentService;
    private final NotificationService notificationService;

    public ClinicController(
            AppointmentService appointmentService,
            NotificationService notificationService) {

        this.appointmentService = appointmentService;
        this.notificationService = notificationService;
    }

    @GetMapping("/")
    public String booking(Model model) {

        model.addAttribute("minimumDate", LocalDate.now());

        return "booking";
    }

    @PostMapping("/appointments")
    public String createAppointment(
            @RequestParam String patientName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String doctor,
            @RequestParam String department,
            @RequestParam LocalDate date,
            @RequestParam LocalTime time,
            @RequestParam String reason,
            RedirectAttributes redirectAttributes) {

        appointmentService.book(
                patientName,
                email,
                phone,
                doctor,
                department,
                date,
                time,
                reason
        );

        redirectAttributes.addFlashAttribute(
                "success",
                "Your appointment request has been sent for review."
        );

        return "redirect:/";
    }

    @GetMapping("/admin")
    public String admin(Model model) {

        var appointments = appointmentService.findAll();

        model.addAttribute("appointments", appointments);

        model.addAttribute(
                "pendingCount",
                appointments.stream()
                        .filter(a -> a.getStatus() == AppointmentStatus.PENDING)
                        .count()
        );

        model.addAttribute(
                "approvedCount",
                appointments.stream()
                        .filter(a -> a.getStatus() == AppointmentStatus.APPROVED)
                        .count()
        );

        model.addAttribute(
                "rejectedCount",
                appointments.stream()
                        .filter(a -> a.getStatus() == AppointmentStatus.REJECTED)
                        .count()
        );

        return "admin";
    }

    @PostMapping("/admin/appointments/status")
    public String updateStatus(
            @RequestParam long id,
            @RequestParam AppointmentStatus status,
            RedirectAttributes redirectAttributes) {

        var appointment = appointmentService.findById(id);

        if (appointment != null) {

            appointmentService.updateStatus(id, status);

            String message;

            if (status == AppointmentStatus.APPROVED) {

                message = "Your appointment with Dr. "
                        + appointment.getDoctor()
                        + " on "
                        + appointment.getDate()
                        + " at "
                        + appointment.getTime()
                        + " has been accepted.";

                notificationService.createNotification(
                        appointment.getEmail(),
                        message,
                        "APPOINTMENT_APPROVED"
                );

            } else if (status == AppointmentStatus.REJECTED) {

                message = "Your appointment request with Dr. "
                        + appointment.getDoctor()
                        + " has been rejected.";

                notificationService.createNotification(
                        appointment.getEmail(),
                        message,
                        "APPOINTMENT_REJECTED"
                );
            }

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Appointment status updated."
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "That appointment could not be found."
            );
        }

        return "redirect:/admin";
    }

    @GetMapping("/patient/notifications")
    public String patientNotifications(
            @RequestParam String email,
            Model model) {

        var notifications =
                notificationService.getNotifications(email);

        long unreadCount =
                notificationService.getUnreadCount(email);

        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        model.addAttribute("email", email);

        return "notifications";
    }

    @PostMapping("/patient/notifications/read")
    public String markNotificationRead(
            @RequestParam Long id,
            @RequestParam String email) {

        notificationService.markAsRead(id);

        return "redirect:/patient/notifications?email=" + email;
    }
}