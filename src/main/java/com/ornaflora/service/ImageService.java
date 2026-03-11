package com.ornaflora.service;

import com.ornaflora.dto.ImageUploadRequest;
import com.ornaflora.dto.ImageUploadResponse;
import com.ornaflora.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for handling image uploads and conversions
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5 MB per image
    private static final long MAX_TOTAL_SIZE = 50 * 1024 * 1024; // 50 MB total

    /**
     * Validate and process uploaded base64 images
     */
    public ImageUploadResponse processImages(ImageUploadRequest request) {
        if (request.getBase64Images() == null || request.getBase64Images().isEmpty()) {
            throw new RuntimeException("No images provided");
        }

        List<String> validImages = new ArrayList<>();
        long totalSize = 0;

        for (String base64Image : request.getBase64Images()) {
            try {
                // Validate base64 format
                if (!ImageUtil.isValidBase64Image(base64Image)) {
                    throw new RuntimeException("Invalid base64 image format");
                }

                // Check image size
                long imageSize = ImageUtil.getBase64ImageSize(base64Image);
                if (imageSize > MAX_IMAGE_SIZE) {
                    throw new RuntimeException("Image size exceeds maximum limit of 5 MB");
                }

                totalSize += imageSize;
                if (totalSize > MAX_TOTAL_SIZE) {
                    throw new RuntimeException("Total images size exceeds maximum limit of 50 MB");
                }

                validImages.add(base64Image);
            } catch (Exception e) {
                throw new RuntimeException("Image validation failed: " + e.getMessage());
            }
        }

        return ImageUploadResponse.builder()
                .imageUrls(validImages)
                .imageCount(validImages.size())
                .totalSize(totalSize)
                .message("Images uploaded successfully")
                .build();
    }

    /**
     * Get image MIME type from base64 string
     */
    public String getImageMimeType(String base64Image) {
        return ImageUtil.getMimeTypeFromBase64(base64Image);
    }

    /**
     * Check if image is base64 encoded
     */
    public boolean isBase64Image(String image) {
        return ImageUtil.isBase64Image(image);
    }

    /**
     * Filter and return only base64 images from a list
     */
    public List<String> filterBase64Images(List<String> images) {
        if (images == null) {
            return new ArrayList<>();
        }
        return images.stream()
                .filter(ImageUtil::isBase64Image)
                .collect(Collectors.toList());
    }

    /**
     * Get all base64 image sizes
     */
    public long getTotalImageSize(List<String> images) {
        if (images == null) {
            return 0;
        }
        return images.stream()
                .mapToLong(ImageUtil::getBase64ImageSize)
                .sum();
    }
}
