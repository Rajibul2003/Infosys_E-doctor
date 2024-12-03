package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DoctorService {
    Doctor addDoctor(Doctor doctor);
    Doctor getDoctorById(String doctorId);
    Doctor updateDoctor(String doctorId, Doctor doctor);
    void deleteDoctor(String doctorId);
    List<DoctorAvailability> getAvailableSlots(String doctorId);
    List<Doctor> findDoctorsBySpecialization(String specialization);
}

