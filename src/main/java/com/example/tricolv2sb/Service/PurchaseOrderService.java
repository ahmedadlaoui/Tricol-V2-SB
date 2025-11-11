package com.example.tricolv2sb.Service;

import com.example.tricolv2sb.DTO.CreatePurchaseOrderDTO;
import com.example.tricolv2sb.DTO.ReadPurchaseOrderDTO;
import com.example.tricolv2sb.DTO.UpdatePurchaseOrderDTO;
import com.example.tricolv2sb.Entity.*;
import com.example.tricolv2sb.Exception.PurchaseOrderNotFoundException;
import com.example.tricolv2sb.Repository.StockMovementRepository;
import com.example.tricolv2sb.Repository.StockLotRepository;
import com.example.tricolv2sb.Entity.Enum.OrderStatus;
import com.example.tricolv2sb.Mapper.PurchaseOrderMapper;
import com.example.tricolv2sb.Entity.Enum.StockMovementType;
import com.example.tricolv2sb.Repository.PurchaseOrderRepository;
import com.example.tricolv2sb.Repository.SupplierRepository;
import com.example.tricolv2sb.Service.ServiceInterfaces.PurchaseOrderInterface;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderService implements PurchaseOrderInterface {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final StockLotRepository stockLotRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final StockMovementRepository stockMovementRepository;

    @Transactional(readOnly = true)
    public List<ReadPurchaseOrderDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll()
                .stream()
                .map(purchaseOrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReadPurchaseOrderDTO getPurchaseOrderById(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found with id: " + id));
        return purchaseOrderMapper.toDto(purchaseOrder);
    }

    public ReadPurchaseOrderDTO createPurchaseOrder(CreatePurchaseOrderDTO createPurchaseOrderDTO) {
        Supplier supplier = supplierRepository.findById(createPurchaseOrderDTO.getSupplierId())
                .orElseThrow(() -> new RuntimeException(
                        "Supplier not found with id: " + createPurchaseOrderDTO.getSupplierId()));

        PurchaseOrder purchaseOrder = purchaseOrderMapper.toEntity(createPurchaseOrderDTO);
        purchaseOrder.setOrderDate(LocalDate.now());
        purchaseOrder.setStatus(OrderStatus.PENDING);
        purchaseOrder.setTotalAmount(0.0);
        purchaseOrder.setSupplier(supplier);

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderMapper.toDto(savedPurchaseOrder);
    }

    public ReadPurchaseOrderDTO updatePurchaseOrder(Long id, UpdatePurchaseOrderDTO updatePurchaseOrderDTO) {
        PurchaseOrder existingPurchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found with id: " + id));

        purchaseOrderMapper.updateEntity(updatePurchaseOrderDTO, existingPurchaseOrder);
        PurchaseOrder updatedPurchaseOrder = purchaseOrderRepository.save(existingPurchaseOrder);
        return purchaseOrderMapper.toDto(updatedPurchaseOrder);
    }

    public void deletePurchaseOrder(Long id) {
        if (!purchaseOrderRepository.existsById(id)) {
            throw new RuntimeException("Purchase order not found with id: " + id);
        }
        purchaseOrderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReadPurchaseOrderDTO> getPurchaseOrdersBySupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + supplierId));

        return purchaseOrderRepository.findBySupplier(supplier)
                .stream()
                .map(purchaseOrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void validateOrder(Long orderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new PurchaseOrderNotFoundException(
                        "Purchase order with ID " + orderId + " not found"));

        if (purchaseOrder.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING orders can be validated. Current status: " + purchaseOrder.getStatus());
        }

        purchaseOrder.setStatus(OrderStatus.VALIDATED);
        purchaseOrderRepository.save(purchaseOrder);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new PurchaseOrderNotFoundException(
                        "Purchase order with ID " + orderId + " not found"));

        if (purchaseOrder.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException(
                    "Cannot cancel a delivered order");
        }

        if (purchaseOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Order is already cancelled");
        }

        purchaseOrder.setStatus(OrderStatus.CANCELLED);
        purchaseOrderRepository.save(purchaseOrder);
    }

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

                Double quantity = orderLine.getQuantity();
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
