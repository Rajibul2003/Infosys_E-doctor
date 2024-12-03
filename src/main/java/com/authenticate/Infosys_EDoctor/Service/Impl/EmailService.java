package com.authenticate.Infosys_EDoctor.Service.Impl;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendDoctorIdEmail(String email, String doctorId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Doctor Profile Created");
        message.setText("Your Doctor ID is: " + doctorId + "\nSave this ID for further references");
        mailSender.send(message);
    }

    public void sendPatientIdEmail(String email, String patientId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Patient Profile Created");
        message.setText("Your Patient ID is: " + patientId + "\nSave this ID for further references");
        mailSender.send(message);
    }
}
