package com.ornaflora.controller;

import com.ornaflora.dto.ImageUploadRequest;
import com.ornaflora.dto.ImageUploadResponse;
import com.ornaflora.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling image uploads and retrieval
 */
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * Upload images as base64 encoded strings
     * 
     * @param request ImageUploadRequest containing list of base64 images
     * @return ImageUploadResponse with validated images and metadata
     */
    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> uploadImages(@RequestBody ImageUploadRequest request) {
        try {
            ImageUploadResponse response = imageService.processImages(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ImageUploadResponse.builder()
                            .message("Upload failed: " + e.getMessage())
                            .build());
        }
    }

    /**
     * Validate a single base64 image
     * 
     * @param base64Image the base64 encoded image
     * @return whether the image is valid
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateImage(@RequestParam String base64Image) {
        try {
            boolean isValid = imageService.isBase64Image(base64Image);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    /**
     * Get MIME type of a base64 image
     * 
     * @param base64Image the base64 encoded image
     * @return the MIME type (e.g., "png", "jpeg")
     */
    @PostMapping("/mime-type")
    public ResponseEntity<String> getMimeType(@RequestParam String base64Image) {
        try {
            String mimeType = imageService.getImageMimeType(base64Image);
            if (mimeType == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.ok(mimeType);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Health check endpoint for image service
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Image service is operational");
    }
}
