package com.example.tricolv2sb.Controller;

import com.example.tricolv2sb.DTO.ProductStockDetailDTO;
import com.example.tricolv2sb.DTO.StockSummaryDTO;
import com.example.tricolv2sb.DTO.StockValuationDTO;
import com.example.tricolv2sb.Service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock")
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockSummaryDTO>> getGlobalStock() {
        List<StockSummaryDTO> stock = stockService.getGlobalStock();
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductStockDetailDTO> getProductStockDetail(@PathVariable Long id) {
        ProductStockDetailDTO detail = stockService.getProductStockDetail(id);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/valuation")
    public ResponseEntity<StockValuationDTO> getTotalValuation() {
        StockValuationDTO valuation = stockService.getTotalValuation();
        return ResponseEntity.ok(valuation);
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<StockSummaryDTO>> getStockAlerts() {
        List<StockSummaryDTO> alerts = stockService.getStockAlerts();
        return ResponseEntity.ok(alerts);
    }
}
