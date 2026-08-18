package com.ornaflora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private UserDTO user;
    private Long addressId;
    private AddressDTO shippingAddress;
    private String status;
    private String paymentMethod;
    private BigDecimal totalAmount;
    private BigDecimal deliveryCharge;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
