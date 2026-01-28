package com.ornaflora.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String avatarUrl;
    
    @JsonProperty("isActive")
    private Boolean isActive;
    
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
