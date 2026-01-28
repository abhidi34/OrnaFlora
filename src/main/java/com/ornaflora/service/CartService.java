package com.ornaflora.service;

import com.ornaflora.dto.CartItemDTO;
import com.ornaflora.dto.CartItemRequest;
import com.ornaflora.model.CartItem;
import com.ornaflora.model.Product;
import com.ornaflora.model.User;
import com.ornaflora.repository.CartItemRepository;
import com.ornaflora.repository.ProductRepository;
import com.ornaflora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartItemDTO addToCart(Long userId, CartItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if item already in cart
        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());

        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(request.getQuantity())
                    .selectedImageUrl(request.getSelectedImageUrl())
                    .build();
        }

        CartItem savedItem = cartItemRepository.save(cartItem);
        return convertToDTO(savedItem);
    }

    public CartItemDTO updateCartItem(Long itemId, CartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItem.setQuantity(request.getQuantity());
        cartItem.setSelectedImageUrl(request.getSelectedImageUrl());

        CartItem updatedItem = cartItemRepository.save(cartItem);
        return convertToDTO(updatedItem);
    }

    public void removeFromCart(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public void removeByProductId(Long userId, Long productId) {
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);
    }

    public List<CartItemDTO> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    private CartItemDTO convertToDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .product(null) // Product details can be fetched separately if needed
                .quantity(cartItem.getQuantity())
                .selectedImageUrl(cartItem.getSelectedImageUrl())
                .createdAt(cartItem.getCreatedAt())
                .updatedAt(cartItem.getUpdatedAt())
                .build();
    }
}
