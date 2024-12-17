package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.*;
import com.authenticate.Infosys_EDoctor.Repository.UserRepository;
import com.authenticate.Infosys_EDoctor.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    // --- User Management ---
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String username, User updatedUser) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setEnabled(updatedUser.isEnabled());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // --- Doctor Management ---
    @Override
    public Doctor addDoctor(Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Doctor existingDoctor = doctorService.getDoctorById(String.valueOf(id));
        existingDoctor.setName(doctor.getName());  // setting the doctor's name
        existingDoctor.setSpecialization(doctor.getSpecialization());  // setting the doctor's specialization
        return doctorService.updateDoctor(String.valueOf(id), existingDoctor);  // updating doctor details
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorService.deleteDoctor(String.valueOf(id));  // deleting doctor by ID
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorService.findAllDoctors();  // fetching all doctors
    }

    // --- Appointment Management ---
    @Override
    public Appointment addAppointment(Appointment appointment) {
        return appointmentService.scheduleAppointment(appointment);  // scheduling a new appointment
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) {
        Appointment existingAppointment = appointmentService.getAppointmentById(id);
        existingAppointment.setPatient(appointment.getPatient());
        existingAppointment.setDoctor(appointment.getDoctor());
        existingAppointment.setAppointmentDateTime(appointment.getAppointmentDate());
        existingAppointment.setStatus(appointment.getStatus());
        return appointmentService.updateAppointment(id, existingAppointment);  // updating appointment details
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentService.cancelAppointment(id, "Deleted by Admin");  // canceling the appointment with a reason
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAppointmentsForDoctor(null);  // Fetching all appointments (no specific doctor)
    }

    // --- Patient Management ---
    @Override
    public Patient addPatient(Patient patient) {
        return patientService.addPatient(patient);  // adding new patient
    }

    @Override
    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient existingPatient = patientService.getPatientById(id.toString());
        existingPatient.setName(updatedPatient.getName());  // updating patient's name
        existingPatient.setEmail(updatedPatient.getEmail());  // updating patient's email
        return patientService.updateProfile(id.toString(), existingPatient);  // updating patient profile
    }

    @Override
    public void deletePatient(Long id) {
        Patient existingPatient = patientService.getPatientById(id.toString());
        if (existingPatient != null) {
            patientService.deletePatient(id.toString());  // deleting patient by ID
        } else {
            throw new RuntimeException("Patient not found");  // exception handling if patient not found
        }
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientService.findAllPatients();  // fetching all patients
    }
}
