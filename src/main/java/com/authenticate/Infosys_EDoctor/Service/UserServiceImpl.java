package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Override
    @Transactional
    public String registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt the password
        String verificationCode = generateOTP(4);
        user.setVerificationCode(verificationCode);
        userRepository.save(user);

        //String verificationLink = "http://localhost:8080/api/auth/verify-email?code=" + user.getVerificationCode();
        emailService.sendEmail(user.getEmail(), "Email Verification", "Click the link to verify your email: " + verificationCode);

        return "Registration successful! Please check your email to verify your account.";
    }

    @Override
    public boolean loginUser(@Valid String username, String password, User.Role role) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!existingUser.isEnabled()) {
            System.out.println("Verify email before logging in");
            return false;
        }

        return passwordEncoder.matches(password, existingUser.getPassword()) && role.equals(existingUser.getRole());
    }

    @Override
    public boolean verifyEmail(String verificationCode) {
        Optional<User> userOpt = userRepository.findByVerificationCode(verificationCode);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEnabled(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            System.out.println("Email verified successfully!");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String sendResetPasswordToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setResetPasswordToken(UUID.randomUUID().toString());
            userRepository.save(user);

            String resetLink = "http://localhost:8080/api/auth/reset-password/confirm?token=" + user.getResetPasswordToken();
            emailService.sendEmail(user.getEmail(), "Password Reset Request", "Click the link to reset your password: " + resetLink);

            return "Password reset email sent.";
        } else {
            return "User with this email does not exist.";
        }
    }

    @Override
    public String resetPassword(String token, String newPassword) {
        Optional<User> userOpt = userRepository.findByResetPasswordToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            userRepository.save(user);
            return "Password updated successfully!";
        } else {
            return "Invalid reset token.";
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), new ArrayList<>());
    }

    private static final String NUMBERS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    public String generateOTP(int length) {
        StringBuilder otp = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            otp.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }

        return otp.toString();
    }
}
