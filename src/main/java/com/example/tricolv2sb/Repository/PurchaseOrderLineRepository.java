package com.example.tricolv2sb.Repository;

import com.example.tricolv2sb.Entity.PurchaseOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine,Long> {
}
