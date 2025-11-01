package com.example.tricolv2sb.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String reference;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Column(nullable = false)
    private Integer unitPrice;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private Double currentStock = 0.0;
    
    @Column(nullable = false)
    private Double reorderPoint;
    
    @Column(nullable = false)
    private String unitOfMeasure;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PurchaseOrderLine> purchaseOrderLines;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<GoodsIssueLine> goodsIssueLines;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<StockLot> stockLots;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<StockMovement> stockMovements;
}