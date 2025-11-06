package com.example.tricolv2sb.Repository;

import com.example.tricolv2sb.Entity.PurchaseOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine, Long> {

    @Query("SELECT COUNT(sl) FROM StockLot sl WHERE sl.purchaseOrderLine.id = :orderLineId")
    long countStockLotsByOrderLineId(@Param("orderLineId") Long orderLineId);
}
