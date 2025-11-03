package com.example.tricolv2sb.Controller;

import com.example.tricolv2sb.DTO.ReadPurchaseOrderLineDTO;
import com.example.tricolv2sb.Service.PurchaseOrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchase-order-lines")
public class PurchaseOrderLineController {

    private final PurchaseOrderLineService purchaseOrderLineService;

    /**
     * GET /api/v1/purchase-order-lines
     * Gets a list of all purchase order lines.
     */
    @GetMapping
    public ResponseEntity<?> getAllPurchaseOrderLines() {
        try {
            List<ReadPurchaseOrderLineDTO> orderLines = purchaseOrderLineService.fetchAllPurchaseOrderLines();
            return ResponseEntity.ok(orderLines);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database error occurred: " + e.getMessage());
        }
    }
}
