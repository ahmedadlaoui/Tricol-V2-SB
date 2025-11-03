package com.example.tricolv2sb.Service;

import com.example.tricolv2sb.DTO.ReadPurchaseOrderLineDTO;
import com.example.tricolv2sb.Entity.PurchaseOrderLine;
import com.example.tricolv2sb.Mapper.PurchaseOrderLineMapper;
import com.example.tricolv2sb.Repository.PurchaseOrderLineRepository;
import com.example.tricolv2sb.Service.ServiceInterfaces.PurchaseOrderLineServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderLineService implements PurchaseOrderLineServiceInterface {
    private final PurchaseOrderLineRepository orderLineRepository;
    private final PurchaseOrderLineMapper orderLineMapper;

    @Transactional
    public List<ReadPurchaseOrderLineDTO> fetchAllPurchaseOrderLines() {
        List<PurchaseOrderLine> orderLines = orderLineRepository.findAll();
        return orderLines.stream()
                .map(orderLineMapper::toDto)
                .toList();
    }
}
