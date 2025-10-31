package com.example.tricolv2sb.Mapper;

import com.example.tricolv2sb.DTO.CreateSupplierDTO;
import com.example.tricolv2sb.DTO.ReadSupplierDTO;
import com.example.tricolv2sb.Entity.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    Supplier toEntity(CreateSupplierDTO createDto);
    ReadSupplierDTO toDto(Supplier supplier);
}
