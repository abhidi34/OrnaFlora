package com.ornaflora.util;

import java.util.Base64;

/**
 * Utility class for handling base64 image encoding/decoding
 */
public class ImageUtil {

    private static final String BASE64_PREFIX = "data:image/";
    
    /**
     * Check if a string is already a base64 image
     */
    public static boolean isBase64Image(String imageString) {
        return imageString != null && imageString.startsWith(BASE64_PREFIX);
    }

    /**
     * Encode binary image data to base64 string with MIME type prefix
     */
    public static String encodeToBase64(byte[] imageBytes, String mimeType) {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        
        String base64String = Base64.getEncoder().encodeToString(imageBytes);
        return BASE64_PREFIX + mimeType + ";base64," + base64String;
    }

    /**
     * Decode base64 image string to binary data
     */
    public static byte[] decodeFromBase64(String base64ImageString) {
        if (base64ImageString == null || !isBase64Image(base64ImageString)) {
            return null;
        }
        
        // Extract the base64 part after the prefix (e.g., "data:image/png;base64,")
        int commaIndex = base64ImageString.lastIndexOf(',');
        if (commaIndex == -1) {
            return null;
        }
        
        String base64Data = base64ImageString.substring(commaIndex + 1);
        try {
            return Base64.getDecoder().decode(base64Data);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get MIME type from base64 image string
     */
    public static String getMimeTypeFromBase64(String base64ImageString) {
        if (base64ImageString == null || !isBase64Image(base64ImageString)) {
            return null;
        }
        
        // Extract MIME type from "data:image/png;base64,..."
        int startIndex = BASE64_PREFIX.length();
        int endIndex = base64ImageString.indexOf(';', startIndex);
        
        if (endIndex == -1) {
            endIndex = base64ImageString.indexOf(',', startIndex);
        }
        
        if (endIndex > startIndex) {
            return base64ImageString.substring(startIndex, endIndex);
        }
        
        return null;
    }

    /**
     * Validate base64 image string
     */
    public static boolean isValidBase64Image(String base64ImageString) {
        if (!isBase64Image(base64ImageString)) {
            return false;
        }
        
        try {
            decodeFromBase64(base64ImageString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get size of base64 image in bytes (approximate)
     */
    public static long getBase64ImageSize(String base64ImageString) {
        if (base64ImageString == null || !isBase64Image(base64ImageString)) {
            return 0;
        }
        
        // Size is approximately 4/3 of the base64 string length (minus the prefix)
        int commaIndex = base64ImageString.lastIndexOf(',');
        if (commaIndex == -1) {
            return 0;
        }
        
        int base64PartLength = base64ImageString.length() - commaIndex - 1;
        return (base64PartLength * 3) / 4; // Approximate binary size
    }
}
