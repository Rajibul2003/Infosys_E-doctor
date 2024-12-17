package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Notification;
import com.authenticate.Infosys_EDoctor.Repository.NotificationRepository;
import com.authenticate.Infosys_EDoctor.Service.Impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationScheduler {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    // Run every day at 7 AM to send pending notifications
    @Scheduled(cron = "0 0 7 * * ?") // Cron expression for 7:00 AM daily
    public void processPendingNotifications() {
        // Fetch all notifications with "Pending" status
        List<Notification> pendingNotifications = notificationRepository.findByStatus("Pending");

        for (Notification notification : pendingNotifications) {
            try {
                // Send email
                emailService.sendEmail(notification.getRecipientEmail(), "Appointment Notification", notification.getMessage());

                // Update notification status to "Sent"
                notification.setStatus("Sent");
                notificationRepository.save(notification);

            } catch (Exception e) {
                // Log the exception (optional: add retry mechanism)
                System.err.println("Failed to send notification to " + notification.getRecipientEmail());
            }
        }
    }
}
