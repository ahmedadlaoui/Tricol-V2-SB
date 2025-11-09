package com.example.tricolv2sb.Service;

import com.example.tricolv2sb.DTO.CreatePurchaseOrderDTO;
import com.example.tricolv2sb.DTO.ReadPurchaseOrderDTO;
import com.example.tricolv2sb.DTO.UpdatePurchaseOrderDTO;
import com.example.tricolv2sb.Entity.PurchaseOrder;
import com.example.tricolv2sb.Entity.Supplier;
import com.example.tricolv2sb.Entity.Enum.OrderStatus;
import com.example.tricolv2sb.Mapper.PurchaseOrderMapper;
import com.example.tricolv2sb.Repository.PurchaseOrderRepository;
import com.example.tricolv2sb.Repository.SupplierRepository;
import com.example.tricolv2sb.Service.ServiceInterfaces.PurchaseOrderInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseOrderService implements PurchaseOrderInterface {
    
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    
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
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + createPurchaseOrderDTO.getSupplierId()));
        
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
}