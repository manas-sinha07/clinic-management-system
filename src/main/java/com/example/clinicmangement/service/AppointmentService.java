package com.example.clinicmangement.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.clinicmangement.model.Appointment;
import com.example.clinicmangement.model.AppointmentStatus;
import com.example.clinicmangement.repository.AppointmentRepository;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Appointment> findAll() {
        return appointmentRepository.findAllByOrderByDateAscTimeAsc();
    }

    public Appointment book(String patientName, String email, String phone, String doctor, String department,
                            LocalDate date, LocalTime time, String reason) {
        Appointment appointment = new Appointment(patientName.trim(), email.trim(), phone.trim(), doctor,
                department, date, time, reason.trim(), AppointmentStatus.PENDING);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public boolean updateStatus(long id, AppointmentStatus status) {
        return appointmentRepository.findById(id).map(appointment -> {
            appointment.setStatus(status);
            return true;
        }).orElse(false);
    }
    public Appointment findById(Long id) {
    return appointmentRepository.findById(id).orElse(null);
}
}