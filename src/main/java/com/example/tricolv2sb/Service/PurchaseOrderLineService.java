package com.example.tricolv2sb.Service;

import com.example.tricolv2sb.DTO.CreatePurchaseOrderLineDTO;
import com.example.tricolv2sb.DTO.ReadPurchaseOrderLineDTO;
import com.example.tricolv2sb.DTO.UpdatePurchaseOrderLineDTO;
import com.example.tricolv2sb.Entity.Product;
import com.example.tricolv2sb.Entity.PurchaseOrder;
import com.example.tricolv2sb.Entity.PurchaseOrderLine;
import com.example.tricolv2sb.Exception.ProductNotFoundException;
import com.example.tricolv2sb.Exception.PurchaseOrderLineNotFoundException;
import com.example.tricolv2sb.Exception.PurchaseOrderNotFoundException;
import com.example.tricolv2sb.Mapper.PurchaseOrderLineMapper;
import com.example.tricolv2sb.Repository.ProductRepository;
import com.example.tricolv2sb.Repository.PurchaseOrderLineRepository;
import com.example.tricolv2sb.Repository.PurchaseOrderRepository;
import com.example.tricolv2sb.Service.ServiceInterfaces.PurchaseOrderLineServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderLineService implements PurchaseOrderLineServiceInterface {
    private final PurchaseOrderLineRepository orderLineRepository;
    private final PurchaseOrderLineMapper orderLineMapper;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Transactional(readOnly = true)
    public List<ReadPurchaseOrderLineDTO> fetchAllPurchaseOrderLines() {
        List<PurchaseOrderLine> orderLines = orderLineRepository.findAll();

        if (orderLines.isEmpty()) {
            return List.of(); // Return empty list
        }

        return orderLines.stream()
                .map(orderLineMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ReadPurchaseOrderLineDTO> fetchPurchaseOrderLineById(Long id) {
        return Optional.of(
                orderLineRepository.findById(id)
                        .map(orderLineMapper::toDto)
                        .orElseThrow(() -> new PurchaseOrderLineNotFoundException(
                                "Purchase order line with ID " + id + " not found")));
    }

    @Transactional
    public ReadPurchaseOrderLineDTO createPurchaseOrderLine(CreatePurchaseOrderLineDTO dto) {
        // Verify that the purchase order exists
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                .orElseThrow(() -> new PurchaseOrderNotFoundException(
                        "Purchase order with ID " + dto.getPurchaseOrderId() + " not found"));

        // Verify that the product exists
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with ID " + dto.getProductId() + " not found"));

        // Create the purchase order line
        PurchaseOrderLine orderLine = orderLineMapper.toEntity(dto);
        orderLine.setPurchaseOrder(purchaseOrder);
        orderLine.setProduct(product);

        PurchaseOrderLine savedOrderLine = orderLineRepository.save(orderLine);
        return orderLineMapper.toDto(savedOrderLine);
    }

    @Transactional
    public ReadPurchaseOrderLineDTO updatePurchaseOrderLine(Long id, UpdatePurchaseOrderLineDTO dto) {
        PurchaseOrderLine existingOrderLine = orderLineRepository.findById(id)
                .orElseThrow(() -> new PurchaseOrderLineNotFoundException(
                        "Purchase order line with ID " + id + " not found"));

        orderLineMapper.updateFromDto(dto, existingOrderLine);

        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(
                            "Product with ID " + dto.getProductId() + " not found"));
            existingOrderLine.setProduct(product);
        }

        PurchaseOrderLine savedOrderLine = orderLineRepository.save(existingOrderLine);
        return orderLineMapper.toDto(savedOrderLine);
    }

    @Transactional
    public void deletePurchaseOrderLine(Long id) {
        if (!orderLineRepository.existsById(id)) {
            throw new PurchaseOrderLineNotFoundException(
                    "Purchase order line with ID " + id + " not found");
        }

        long stockLotCount = orderLineRepository.countStockLotsByOrderLineId(id);

        if (stockLotCount > 0) {
            throw new IllegalStateException(
                    "Cannot delete this purchase order line because it has " + stockLotCount +
                            " associated stock lot(s). The stock has already been received and is in inventory. " +
                            "Please handle the stock lots first before deleting this line.");
        }

        orderLineRepository.deleteById(id);
    }
}
