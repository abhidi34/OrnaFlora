package com.ornaflora.controller;

import com.ornaflora.dto.CartItemDTO;
import com.ornaflora.dto.CartItemRequest;
import com.ornaflora.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartItemDTO> addToCart(
            @RequestParam Long userId,
            @RequestBody CartItemRequest request) {
        try {
            CartItemDTO item = cartService.addToCart(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItems(@RequestParam Long userId) {
        List<CartItemDTO> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CartItemDTO> updateCartItem(
            @PathVariable Long itemId,
            @RequestBody CartItemRequest request) {
        try {
            CartItemDTO item = cartService.updateCartItem(itemId, request);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long itemId) {
        try {
            cartService.removeFromCart(itemId);
            return ResponseEntity.ok("Item removed from cart");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart(@RequestParam Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart cleared successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/product")
    public ResponseEntity<String> removeProduct(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        try {
            cartService.removeByProductId(userId, productId);
            return ResponseEntity.ok("Product removed from cart");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
