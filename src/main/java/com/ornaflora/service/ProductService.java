package com.ornaflora.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ornaflora.dto.ProductDTO;
import com.ornaflora.dto.ProductRequest;
import com.ornaflora.model.Product;
import com.ornaflora.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ProductDTO createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrls(convertListToJson(request.getImageUrls()))
                .isActive(true)
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAllActiveProducts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String searchTerm) {
        return productRepository.searchByName(searchTerm).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    public ProductDTO updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrls(convertListToJson(request.getImageUrls()));

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setIsActive(false);
        productRepository.save(product);
    }

    public void updateStock(Long id, Integer newStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(newStock);
        productRepository.save(product);
    }

    public List<ProductDTO> getAvailableProducts() {
        return productRepository.findAllAvailableProducts().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrls(convertJsonToList(product.getImageUrls()))
                .isActive(product.getIsActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            return "[]";
        }
    }

    private List<String> convertJsonToList(String json) {
        if (json == null || json.isEmpty() || json.equals("[]")) {
            return List.of();
        }
        try {
            return Arrays.asList(objectMapper.readValue(json, String[].class));
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Add images to an existing product (append to existing images)
     */
    public ProductDTO addImagesToProduct(Long id, List<String> newImages) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<String> existingImages = convertJsonToList(product.getImageUrls());
        List<String> allImages = new ArrayList<>(existingImages);
        
        if (newImages != null) {
            allImages.addAll(newImages);
        }

        product.setImageUrls(convertListToJson(allImages));
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    /**
     * Replace all images for a product
     */
    public ProductDTO replaceProductImages(Long id, List<String> newImages) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setImageUrls(convertListToJson(newImages));
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    /**
     * Get all images for a product
     */
    public List<String> getProductImages(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertJsonToList(product.getImageUrls());
    }
}
