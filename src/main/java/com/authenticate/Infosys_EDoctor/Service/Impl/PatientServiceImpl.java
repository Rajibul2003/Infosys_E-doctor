package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Entity.Patient;
import com.authenticate.Infosys_EDoctor.Repository.DoctorAvailabilityRepository;
import com.authenticate.Infosys_EDoctor.Repository.DoctorRepository;
import com.authenticate.Infosys_EDoctor.Repository.PatientRepository;
import com.authenticate.Infosys_EDoctor.Service.PatientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

//    @Autowired
//    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmailService emailService;

    // 1. Add Profile
    public Patient addPatient(Patient patient) {
//        Optional<Patient> exists = patientRepository.findByEmail(patient.getEmail());
//        if(exists.isPresent()) {
//            throw new RuntimeException("Patient with given email already exists");
//        }

        String id = "PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        patient.setPatientId(id);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        Patient savedPatient = patientRepository.save(patient);

        emailService.sendPatientIdEmail(savedPatient.getEmail(), savedPatient.getPatientId());

        return savedPatient;
    }

    // 2. Update Profile
    @Transactional
    public Patient updateProfile(String patientId, Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));

        existingPatient.setName(updatedPatient.getName());
        existingPatient.setMobileNo(updatedPatient.getMobileNo());
        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setPassword(passwordEncoder.encode(updatedPatient.getPassword()));
        existingPatient.setBloodGroup(updatedPatient.getBloodGroup());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setAddress(updatedPatient.getAddress());

        return patientRepository.save(existingPatient);
    }

    // 3. Find Doctors
    public List<Doctor> findDoctors() {

        return doctorRepository.findAll(); // Fetches all doctors
    }

    // 4. Make Appointment
    //public Appointment makeAppointment(Appointment appointment) {
//        return appointmentRepository.save(appointment);
//    }

    // 5. Update Appointment
//    @Transactional
//    public Appointment updateAppointment(Long appointmentId, Appointment updatedAppointment) {
//        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));
//
//        existingAppointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
//        existingAppointment.setAppointmentStatus(updatedAppointment.getAppointmentStatus());
//        existingAppointment.setRemark(updatedAppointment.getRemark());
//
//        return existingAppointment;
//    }

    // 6. Cancel Appointment
//    @Transactional
//    public void cancelAppointment(Long appointmentId) {
//        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));
//
//        existingAppointment.setAppointmentStatus(Appointment.AppointmentStatus.CANCELED);
//    }

    public List<Doctor> findDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    // Check Available Dates for a Particular Doctor
    public List<DoctorAvailability> getAvailableDates(String doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));

        return doctorAvailabilityRepository.findByDoctorId(doctorId); // Assuming Doctor entity has a list of available dates
    }
}

