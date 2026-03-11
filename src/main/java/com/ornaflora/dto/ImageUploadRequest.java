package com.ornaflora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for handling image upload requests with base64 encoded images
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadRequest {
    
    /**
     * List of base64 encoded images (format: data:image/png;base64,...)
     */
    private List<String> base64Images;
    
    /**
     * Optional description or title for the images
     */
    private String description;
}
