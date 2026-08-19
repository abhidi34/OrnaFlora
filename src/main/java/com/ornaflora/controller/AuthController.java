package com.ornaflora.controller;

import com.ornaflora.auth.JwtUtil;
import com.ornaflora.dto.LoginRequest;
import com.ornaflora.dto.LoginResponse;
import com.ornaflora.dto.PasswordResetRequest;
import com.ornaflora.dto.SignupRequest;
import com.ornaflora.dto.UserDTO;
import com.ornaflora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {

            LoginResponse errorResponse = LoginResponse.builder()
                    .message("Login failed: " + e.getMessage())
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupRequest request) {
        try {
            UserDTO user = userService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // New endpoint for admin registration
    @PostMapping("/admin/register")
    public ResponseEntity<UserDTO> registerAdmin(@RequestBody SignupRequest request) {
        try {
            UserDTO user = userService.signupAdmin(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody PasswordResetRequest request) {
        if (request == null || request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        userService.requestPasswordReset(request.getEmail());

        return ResponseEntity.ok(Map.of(
                "message", "If an account exists for this email, password reset instructions have been sent."
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody PasswordResetRequest request) {
        if (request == null || request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        String newPassword = request.getNewPassword();
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "New password is required"));
        }

        userService.resetPassword(request.getEmail(), newPassword, request.getToken(), request.getOtp());

        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable Long userId) {
        try {
            UserDTO user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
