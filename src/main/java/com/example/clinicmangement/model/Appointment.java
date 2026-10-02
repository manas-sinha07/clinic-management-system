package com.example.clinicmangement.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String patientName;
    private String email;
    private String phone;
    private String doctor;
    private String department;
    private LocalDate date;
    private LocalTime time;
    private String reason;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    protected Appointment() {
    }

    public Appointment(String patientName, String email, String phone, String doctor, String department,
                       LocalDate date, LocalTime time, String reason, AppointmentStatus status) {
        this.patientName = patientName;
        this.email = email;
        this.phone = phone;
        this.doctor = doctor;
        this.department = department;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getPatientName() { return patientName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDoctor() { return doctor; }
    public String getDepartment() { return department; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getReason() { return reason; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
}