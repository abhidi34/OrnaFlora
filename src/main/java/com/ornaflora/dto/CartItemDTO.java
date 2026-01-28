package com.ornaflora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long productId;
    private ProductDTO product;
    private Integer quantity;
    private String selectedImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
