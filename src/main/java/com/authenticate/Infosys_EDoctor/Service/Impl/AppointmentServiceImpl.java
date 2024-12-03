package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Repository.AppointmentRepository;
import com.authenticate.Infosys_EDoctor.Repository.DoctorAvailabilityRepository;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Override
    public Appointment scheduleAppointment(Appointment appointment) {
        // Check doctor availability
        List<DoctorAvailability> availabilities = availabilityRepository.findByDoctorId(
                appointment.getDoctor().getDoctorId()
        );

        boolean isSlotAvailable = availabilities.stream().anyMatch(availability ->
                appointment.getAppointmentDateTime().toLocalDate().isAfter(availability.getFromDate().minusDays(1)) &&
                        appointment.getAppointmentDateTime().toLocalDate().isBefore(availability.getEndDate().plusDays(1))
        );

        if (!isSlotAvailable) {
            throw new RuntimeException("Doctor is not available for the selected time slot");
        }

        // Save appointment
        appointment.setStatus(Appointment.Status.Pending);
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsForPatient(String patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    @Override
    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        return appointmentRepository.findByDoctor_DoctorId(doctorId);
    }
}
