package com.authenticate.Infosys_EDoctor.Repository;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctor_DoctorIdAndAppointmentDateTimeBetween(String doctorId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByPatient_PatientId(String patientId);
    List<Appointment> findByDoctor_DoctorId(String doctorId);
}
