package com.ornaflora.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
    private String email;

    @JsonAlias({"newPassword", "password"})
    private String newPassword;

    @JsonAlias({"resetToken", "token"})
    private String token;

    @JsonAlias({"otpCode", "otp"})
    private String otp;
}
