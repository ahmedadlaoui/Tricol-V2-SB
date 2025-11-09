package com.example.tricolv2sb.Repository;

import com.example.tricolv2sb.Entity.StockLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockLotRepository extends JpaRepository<StockLot, Long> {

    @Query("SELECT COUNT(sl) FROM StockLot sl WHERE sl.lotNumber = :lotNumber")
    long countByLotNumber(@Param("lotNumber") String lotNumber);
}
