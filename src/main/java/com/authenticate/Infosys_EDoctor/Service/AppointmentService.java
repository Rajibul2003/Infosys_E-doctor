package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment scheduleAppointment(Appointment appointment);
    List<Appointment> getAppointmentsForPatient(String patientId);
    List<Appointment> getAppointmentsForDoctor(String doctorId);
}
