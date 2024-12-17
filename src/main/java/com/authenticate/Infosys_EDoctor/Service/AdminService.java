package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.Entity.*;

import java.util.List;

public interface AdminService {
    // User Management
    User addUser(User user);
    User updateUser(String username, User user);
    void deleteUser(String username);
    List<User> getAllUsers();

    // Doctor Management
    Doctor addDoctor(Doctor doctor);
    Doctor updateDoctor(Long id, Doctor doctor);
    void deleteDoctor(Long id);
    List<Doctor> getAllDoctors();

    // Appointment Management
    Appointment addAppointment(Appointment appointment);
    Appointment updateAppointment(Long id, Appointment appointment);
    void deleteAppointment(Long id);
    List<Appointment> getAllAppointments();

    // Patient Management
    Patient addPatient(Patient patient);
    Patient updatePatient(Long id, Patient patient);
    void deletePatient(Long id);
    List<Patient> getAllPatients();
}
