package com.example.tricolv2sb.Service;

import com.example.tricolv2sb.DTO.CreateProductDTO;
import com.example.tricolv2sb.DTO.ReadProductDTO;
import com.example.tricolv2sb.DTO.UpdateProductDTO;
import com.example.tricolv2sb.Entity.Product;
import com.example.tricolv2sb.Mapper.ProductMapper;
import com.example.tricolv2sb.Repository.ProductRepository;
import com.example.tricolv2sb.Service.interfaces.ProductInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService implements ProductInterface {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    @Transactional(readOnly = true)
    public List<ReadProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ReadProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }
    
    public ReadProductDTO createProduct(CreateProductDTO createProductDTO) {
        if (productRepository.existsByReference(createProductDTO.getReference())) {
            throw new RuntimeException("Product with reference " + createProductDTO.getReference() + " already exists");
        }
        
        Product product = productMapper.toEntity(createProductDTO);
        product.setCurrentStock(0.0);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }
    
    public ReadProductDTO updateProduct(Long id, UpdateProductDTO updateProductDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        productMapper.updateEntity(updateProductDTO, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }
    
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
