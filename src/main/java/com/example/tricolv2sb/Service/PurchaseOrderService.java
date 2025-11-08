package com.example.tricolv2sb.Service;

import com.example.tricolv2sb.Entity.*;
import com.example.tricolv2sb.Entity.Enum.OrderStatus;
import com.example.tricolv2sb.Entity.Enum.StockMovementType;
import com.example.tricolv2sb.Exception.PurchaseOrderNotFoundException;
import com.example.tricolv2sb.Repository.PurchaseOrderRepository;
import com.example.tricolv2sb.Repository.StockLotRepository;
import com.example.tricolv2sb.Repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final StockLotRepository stockLotRepository;
    private final StockMovementRepository stockMovementRepository;

    @Transactional
    public void receiveOrder(Long orderId) {
        // Find the purchase order
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new PurchaseOrderNotFoundException(
                        "Purchase order with ID " + orderId + " not found"));

        // Check if order is in correct status
        if (purchaseOrder.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException(
                    "Purchase order with ID " + orderId + " has already been received");
        }

        if (purchaseOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot receive a cancelled purchase order");
        }

        // Change status to DELIVERED
        purchaseOrder.setStatus(OrderStatus.DELIVERED);

        // Process each order line
        LocalDate today = LocalDate.now();

        if (purchaseOrder.getOrderLines() != null && !purchaseOrder.getOrderLines().isEmpty()) {
            for (PurchaseOrderLine orderLine : purchaseOrder.getOrderLines()) {
                // Create a new StockLot for each order line
                StockLot stockLot = new StockLot();

                // Generate unique lot number
                String lotNumber = generateLotNumber(orderId, orderLine.getId());
                stockLot.setLotNumber(lotNumber);

                // Set entry date to today
                stockLot.setEntryDate(today);

                // Set quantities
                Double quantity = orderLine.getQuantity().doubleValue();
                stockLot.setInitialQuantity(quantity);
                stockLot.setRemainingQuantity(quantity);

                // Set purchase price from order line unit price
                stockLot.setPurchasePrice(orderLine.getUnitPrice());

                // Link to product and purchase order line
                stockLot.setProduct(orderLine.getProduct());
                stockLot.setPurchaseOrderLine(orderLine);

                // Save the stock lot
                StockLot savedStockLot = stockLotRepository.save(stockLot);

                // Create stock movement IN for this reception
                StockMovement stockMovement = new StockMovement();
                stockMovement.setMovementDate(today);
                stockMovement.setQuantity(quantity);
                stockMovement.setMovementType(StockMovementType.IN);
                stockMovement.setProduct(orderLine.getProduct());
                stockMovement.setStockLot(savedStockLot);
                stockMovement.setPurchasseOrderLine(orderLine);

                // Save the stock movement
                stockMovementRepository.save(stockMovement);
            }
        }

        // Save the updated purchase order
        purchaseOrderRepository.save(purchaseOrder);
    }

    /**
     * Generate a unique lot number for the stock lot
     * Format: LOT-ORDERID-LINEID-YYYYMMDD
     */
    private String generateLotNumber(Long orderId, Long lineId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "LOT-" + orderId + "-" + lineId + "-" + date;
    }
}
