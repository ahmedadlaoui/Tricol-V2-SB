package com.example.tricolv2sb.Repository;

import com.example.tricolv2sb.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByReference(String reference);
}
