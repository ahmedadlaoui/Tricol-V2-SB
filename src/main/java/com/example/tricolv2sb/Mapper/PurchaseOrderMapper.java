package com.example.tricolv2sb.Mapper;

import com.example.tricolv2sb.DTO.CreatePurchaseOrderDTO;
import com.example.tricolv2sb.DTO.ReadPurchaseOrderDTO;
import com.example.tricolv2sb.DTO.UpdatePurchaseOrderDTO;
import com.example.tricolv2sb.Entity.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "receptionDate", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "orderLines", ignore = true)
    PurchaseOrder toEntity(CreatePurchaseOrderDTO createDto);
    
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.companyName", target = "supplierCompanyName")
    ReadPurchaseOrderDTO toDto(PurchaseOrder purchaseOrder);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "orderLines", ignore = true)
    void updateEntity(UpdatePurchaseOrderDTO updateDto, @MappingTarget PurchaseOrder purchaseOrder);
}