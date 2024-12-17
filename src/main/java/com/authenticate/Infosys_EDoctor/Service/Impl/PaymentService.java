package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Payment;
import com.authenticate.Infosys_EDoctor.Repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    private RazorpayClient razorpayClient;

    public PaymentService() throws Exception {
        // Initialize Razorpay Client with your API Key and Secret
        this.razorpayClient = new RazorpayClient("rzp_test_A3WY0WOJLejK3e", "i7MaH2mMDECRFBTQ8LANbNmT");
    }

    public Payment createOrder(String appointmentId, double amount) throws Exception {
        // Create Razorpay Order
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (amount * 100)); // Convert to paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_rcptid_" + appointmentId);

        Order order = razorpayClient.Orders.create(orderRequest);

        // Save payment details to the database
        Payment payment = new Payment();
        payment.setAppointmentId(appointmentId);
        payment.setAmount(amount);
        payment.setPaymentId(order.get("id")); // Razorpay Order ID
        payment.setStatus("CREATED");

        return paymentRepository.save(payment);
    }
}
