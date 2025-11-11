package com.example.tricolv2sb.Repository;

import com.example.tricolv2sb.Entity.StockLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockLotRepository extends JpaRepository<StockLot, Long> {

    @Query("SELECT COUNT(sl) FROM StockLot sl WHERE sl.lotNumber = :lotNumber")
    long countByLotNumber(@Param("lotNumber") String lotNumber);

    /**
     * Find available stock lots for a product, ordered by entry date (FIFO)
     * Only returns lots with remaining quantity > 0
     */
    @Query("SELECT sl FROM StockLot sl WHERE sl.product.id = :productId AND sl.remainingQuantity > 0 ORDER BY sl.entryDate ASC, sl.id ASC")
    List<StockLot> findAvailableLotsByProductIdOrderByEntryDate(@Param("productId") Long productId);

    /**
     * Calculate total available stock for a product
     */
    @Query("SELECT COALESCE(SUM(sl.remainingQuantity), 0) FROM StockLot sl WHERE sl.product.id = :productId AND sl.remainingQuantity > 0")
    Double calculateTotalAvailableStock(@Param("productId") Long productId);
}
