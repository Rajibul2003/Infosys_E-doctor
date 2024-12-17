package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.Entity.Payment;
import com.authenticate.Infosys_EDoctor.Service.Impl.PaymentService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment")
    public Payment createPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            return paymentService.createOrder(paymentRequest.getAppointmentId(), paymentRequest.getAmount());
        } catch (Exception e) {
            throw new RuntimeException("Error creating payment: " + e.getMessage());
        }
    }
}

// Request Body class
@Setter
@Getter
class PaymentRequest {
    private String appointmentId;
    private double amount;

}
