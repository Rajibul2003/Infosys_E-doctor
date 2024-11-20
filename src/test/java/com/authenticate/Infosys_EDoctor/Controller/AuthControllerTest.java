package com.authenticate.Infosys_EDoctor.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AuthController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private UserService userService;

    /**
     * Test {@link AuthController#registerUser(User)}.
     * <ul>
     *   <li>Given {@link UserService} {@link UserService#registerUser(User)} return
     * {@code Register User}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AuthController#registerUser(User)}
     */
    @Test
    @DisplayName("Test registerUser(User); given UserService registerUser(User) return 'Register User'; then status isOk()")
    void testRegisterUser_givenUserServiceRegisterUserReturnRegisterUser_thenStatusIsOk() throws Exception {
        // Arrange
        when(userService.registerUser(Mockito.<User>any())).thenReturn("Register User");

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setId(1L);
        user.setPassword("iloveyou");
        user.setResetPasswordToken("ABC123");
        user.setRole(User.Role.PATIENT);
        user.setUsername("janedoe");
        user.setVerificationCode("Verification Code");
        String content = (new ObjectMapper()).writeValueAsString(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("User registered successfully. Please verify your email."));
    }

    /**
     * Test {@link AuthController#registerUser(User)}.
     * <ul>
     *   <li>Then status four hundred.</li>
     * </ul>
     * <p>
     * Method under test: {@link AuthController#registerUser(User)}
     */
    @Test
    @DisplayName("Test registerUser(User); then status four hundred")
    void testRegisterUser_thenStatusFourHundred() throws Exception {
        // Arrange
        when(userService.registerUser(Mockito.<User>any())).thenThrow(new RuntimeException("U.U.U"));

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setId(1L);
        user.setPassword("iloveyou");
        user.setResetPasswordToken("ABC123");
        user.setRole(User.Role.PATIENT);
        user.setUsername("janedoe");
        user.setVerificationCode("Verification Code");
        String content = (new ObjectMapper()).writeValueAsString(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("U.U.U"));
    }

    /**
     * Test {@link AuthController#loginUser(Map)}.
     * <p>
     * Method under test: {@link AuthController#loginUser(Map)}
     */
    @Test
    @DisplayName("Test loginUser(Map)")
    void testLoginUser() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        AuthController authController = new AuthController();

        // Act
        ResponseEntity<String> actualLoginUserResult = authController.loginUser(new HashMap<>());

        // Assert
        HttpStatusCode statusCode = actualLoginUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(
                "Cannot invoke \"com.authenticate.Infosys_EDoctor.Service.UserService.loginUser(String, String)\" because"
                        + " \"this.userService\" is null",
                actualLoginUserResult.getBody());
        assertEquals(400, actualLoginUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertTrue(actualLoginUserResult.hasBody());
        assertTrue(actualLoginUserResult.getHeaders().isEmpty());
    }

    /**
     * Test {@link AuthController#verifyEmail(String, String)}.
     * <p>
     * Method under test: {@link AuthController#verifyEmail(String, String)}
     */
    @Test
    @DisplayName("Test verifyEmail(String, String)")
    void testVerifyEmail() throws Exception {
        // Arrange
        when(userService.verifyEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new RuntimeException("Enter valid credentials"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/verify-email")
                .param("code", "foo")
                .param("username", "foo");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Enter valid credentials"));
    }

    /**
     * Test {@link AuthController#verifyEmail(String, String)}.
     * <p>
     * Method under test: {@link AuthController#verifyEmail(String, String)}
     */
    @Test
    @DisplayName("Test verifyEmail(String, String)")
    void testVerifyEmail2() throws Exception {
        // Arrange
        when(userService.verifyEmail(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new UsernameNotFoundException("Enter valid credentials"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/verify-email")
                .param("code", "foo")
                .param("username", "foo");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Enter valid credentials"));
    }

    /**
     * Test {@link AuthController#verifyEmail(String, String)}.
     * <ul>
     *   <li>Given {@link UserService} {@link UserService#verifyEmail(String, String)}
     * return {@code false}.</li>
     *   <li>Then status four hundred.</li>
     * </ul>
     * <p>
     * Method under test: {@link AuthController#verifyEmail(String, String)}
     */
    @Test
    @DisplayName("Test verifyEmail(String, String); given UserService verifyEmail(String, String) return 'false'; then status four hundred")
    void testVerifyEmail_givenUserServiceVerifyEmailReturnFalse_thenStatusFourHundred() throws Exception {
        // Arrange
        when(userService.verifyEmail(Mockito.<String>any(), Mockito.<String>any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/verify-email")
                .param("code", "foo")
                .param("username", "foo");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Enter valid credentials"));
    }

    /**
     * Test {@link AuthController#verifyEmail(String, String)}.
     * <ul>
     *   <li>Given {@link UserService} {@link UserService#verifyEmail(String, String)}
     * return {@code true}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AuthController#verifyEmail(String, String)}
     */
    @Test
    @DisplayName("Test verifyEmail(String, String); given UserService verifyEmail(String, String) return 'true'; then status isOk()")
    void testVerifyEmail_givenUserServiceVerifyEmailReturnTrue_thenStatusIsOk() throws Exception {
        // Arrange
        when(userService.verifyEmail(Mockito.<String>any(), Mockito.<String>any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/verify-email")
                .param("code", "foo")
                .param("username", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Email verified Successfully"));
    }

    /**
     * Test {@link AuthController#resetPassword(String, String, String)}.
     * <p>
     * Method under test:
     * {@link AuthController#resetPassword(String, String, String)}
     */
    @Test
    @DisplayName("Test resetPassword(String, String, String)")
    void testResetPassword() throws Exception {
        // Arrange
        when(userService.resetPassword(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new RuntimeException("Enter valid credentials"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/reset-password/confirm")
                .param("email", "foo")
                .param("newPassword", "foo")
                .param("token", "foo");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Enter valid credentials"));
    }

    /**
     * Test {@link AuthController#resetPassword(String, String, String)}.
     * <ul>
     *   <li>Given {@link UserService}
     * {@link UserService#resetPassword(String, String, String)} return
     * {@code false}.</li>
     *   <li>Then status four hundred.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link AuthController#resetPassword(String, String, String)}
     */
    @Test
    @DisplayName("Test resetPassword(String, String, String); given UserService resetPassword(String, String, String) return 'false'; then status four hundred")
    void testResetPassword_givenUserServiceResetPasswordReturnFalse_thenStatusFourHundred() throws Exception {
        // Arrange
        when(userService.resetPassword(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/reset-password/confirm")
                .param("email", "foo")
                .param("newPassword", "foo")
                .param("token", "foo");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Enter valid credentials"));
    }

    /**
     * Test {@link AuthController#resetPassword(String, String, String)}.
     * <ul>
     *   <li>Given {@link UserService}
     * {@link UserService#resetPassword(String, String, String)} return
     * {@code true}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link AuthController#resetPassword(String, String, String)}
     */
    @Test
    @DisplayName("Test resetPassword(String, String, String); given UserService resetPassword(String, String, String) return 'true'; then status isOk()")
    void testResetPassword_givenUserServiceResetPasswordReturnTrue_thenStatusIsOk() throws Exception {
        // Arrange
        when(userService.resetPassword(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/reset-password/confirm")
                .param("email", "foo")
                .param("newPassword", "foo")
                .param("token", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Your password has been successfully changes"));
    }

    /**
     * Test {@link AuthController#sendResetPasswordToken(String)}.
     * <p>
     * Method under test: {@link AuthController#sendResetPasswordToken(String)}
     */
    @Test
    @DisplayName("Test sendResetPasswordToken(String)")
    void testSendResetPasswordToken() throws Exception {
        // Arrange
        when(userService.sendResetPasswordToken(Mockito.<String>any()))
                .thenThrow(new RuntimeException("Enter valid credentials"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/reset-password")
                .param("email", "foo");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Enter valid credentials"));
    }

    /**
     * Test {@link AuthController#sendResetPasswordToken(String)}.
     * <ul>
     *   <li>Given {@link UserService}
     * {@link UserService#sendResetPasswordToken(String)} return {@code false}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AuthController#sendResetPasswordToken(String)}
     */
    @Test
    @DisplayName("Test sendResetPasswordToken(String); given UserService sendResetPasswordToken(String) return 'false'")
    void testSendResetPasswordToken_givenUserServiceSendResetPasswordTokenReturnFalse() throws Exception {
        // Arrange
        when(userService.sendResetPasswordToken(Mockito.<String>any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/reset-password")
                .param("email", "foo");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Enter valid credentials"));
    }

    /**
     * Test {@link AuthController#sendResetPasswordToken(String)}.
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AuthController#sendResetPasswordToken(String)}
     */
    @Test
    @DisplayName("Test sendResetPasswordToken(String); then status isOk()")
    void testSendResetPasswordToken_thenStatusIsOk() throws Exception {
        // Arrange
        when(userService.sendResetPasswordToken(Mockito.<String>any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/auth/reset-password")
                .param("email", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(
                        MockMvcResultMatchers.content().string("Reset password OTP has been successfully sent to your email"));
    }
}
