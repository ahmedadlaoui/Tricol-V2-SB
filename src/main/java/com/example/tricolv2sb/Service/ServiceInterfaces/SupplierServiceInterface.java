package com.example.tricolv2sb.Service.ServiceInterfaces;

import com.example.tricolv2sb.DTO.CreateSupplierDTO;
import com.example.tricolv2sb.DTO.ReadSupplierDTO;

import java.util.Optional;

public interface SupplierServiceInterface {
    public Optional<ReadSupplierDTO> fetchSupplier(Long id);
    public ReadSupplierDTO addSupplier(CreateSupplierDTO dto);
    public void deleteSupplier(Long id);
}
