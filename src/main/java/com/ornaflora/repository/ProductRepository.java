package com.ornaflora.repository;

import com.ornaflora.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.createdAt DESC")
    List<Product> findAllActiveProducts();
    
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.isActive = true ORDER BY p.name")
    List<Product> findByCategory(@Param("category") String category);
    
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND p.isActive = true")
    List<Product> searchByName(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.isActive = true ORDER BY p.category")
    List<String> findAllCategories();
    
    @Query("SELECT p FROM Product p WHERE p.stock > 0 AND p.isActive = true")
    List<Product> findAllAvailableProducts();
    
    Optional<Product> findByIdAndIsActiveTrue(Long id);
}
