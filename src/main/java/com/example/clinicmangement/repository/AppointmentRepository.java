package com.example.clinicmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.clinicmangement.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByOrderByDateAscTimeAsc();
}