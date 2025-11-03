package com.example.tricolv2sb.Service.ServiceInterfaces;

import com.example.tricolv2sb.DTO.ReadPurchaseOrderLineDTO;

import java.util.List;

public interface PurchaseOrderLineServiceInterface {
    public List<ReadPurchaseOrderLineDTO> fetchAllPurchaseOrderLines();
}
