package com.example.tricolv2sb.Mapper;

import com.example.tricolv2sb.DTO.ReadPurchaseOrderLineDTO;
import com.example.tricolv2sb.Entity.PurchaseOrderLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseOrderLineMapper{
    ReadPurchaseOrderLineDTO toDto(PurchaseOrderLine purchaseOrderLine);
}
