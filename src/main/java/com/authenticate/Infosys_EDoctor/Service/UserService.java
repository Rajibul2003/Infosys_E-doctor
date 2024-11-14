package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.Entity.User;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    String registerUser(User user);
    boolean loginUser(@Valid String username, String password, User.Role role);
    boolean verifyEmail(String token);
    String resetPassword(String email, String newPassword);
    String sendResetPasswordToken(String email);
}
