package com.example.tricolv2sb.Entity;

import com.example.tricolv2sb.Entity.Enum.StockMovementType;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "stock_movements")
@Data
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate movementDate;

    @Column(nullable = false)
    private Double quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockMovementType movementType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_lot_id")
    private StockLot stockLot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_issue_line_id")
    private GoodsIssueLine goodsIssueLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_line_id")
    private PurchaseOrderLine purchasseOrderLine;
}