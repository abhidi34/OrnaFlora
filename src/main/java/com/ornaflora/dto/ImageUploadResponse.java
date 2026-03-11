package com.ornaflora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for image upload response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponse {
    
    /**
     * List of base64 encoded images
     */
    private List<String> imageUrls;
    
    /**
     * Total size of uploaded images in bytes
     */
    private Long totalSize;
    
    /**
     * Number of images uploaded
     */
    private Integer imageCount;
    
    /**
     * Success message
     */
    private String message;
}
