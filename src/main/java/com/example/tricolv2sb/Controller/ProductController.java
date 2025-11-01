package com.example.tricolv2sb.Controller;

import com.example.tricolv2sb.Controller.ControllerInterfaces.ProductControllerInterface;
import com.example.tricolv2sb.DTO.CreateProductDTO;
import com.example.tricolv2sb.DTO.ReadProductDTO;
import com.example.tricolv2sb.DTO.UpdateProductDTO;
import com.example.tricolv2sb.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/produits")
@RequiredArgsConstructor
public class    ProductController implements ProductControllerInterface {
    
    private final ProductService productService;
    
    @Override
    public ResponseEntity<List<ReadProductDTO>> getAllProducts() {
        List<ReadProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @Override
    public ResponseEntity<ReadProductDTO> getProductById(Long id) {
        ReadProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    @Override
    public ResponseEntity<ReadProductDTO> createProduct(@Valid CreateProductDTO createProductDTO) {
        ReadProductDTO createdProduct = productService.createProduct(createProductDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    
    @Override
    public ResponseEntity<ReadProductDTO> updateProduct(Long id, @Valid UpdateProductDTO updateProductDTO) {
        ReadProductDTO updatedProduct = productService.updateProduct(id, updateProductDTO);
        return ResponseEntity.ok(updatedProduct);
    }
    
    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
