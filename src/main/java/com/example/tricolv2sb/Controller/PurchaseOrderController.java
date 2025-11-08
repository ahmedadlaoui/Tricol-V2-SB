package com.example.tricolv2sb.Controller;

import com.example.tricolv2sb.Service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    /**
     * PUT /api/v1/purchase-orders/{id}/reception
     * Receives a purchase order and creates stock lots for FIFO
     */
    @PutMapping("/{id}/reception")
    public ResponseEntity<String> receiveOrder(@PathVariable Long id) {
        purchaseOrderService.receiveOrder(id);
        return ResponseEntity.ok("Purchase order " + id + " has been successfully received and stock lots created");
    }
}
