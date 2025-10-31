package com.example.tricolv2sb.Service;

import com.example.tricolv2sb.DTO.CreateSupplierDTO;
import com.example.tricolv2sb.DTO.ReadSupplierDTO;
import com.example.tricolv2sb.Entity.Supplier;
import com.example.tricolv2sb.Mapper.SupplierMapper;
import com.example.tricolv2sb.Repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierMapper supplierMapper;
    private final SupplierRepository supplierRepository;

    @Transactional
    public ReadSupplierDTO addSupplier(CreateSupplierDTO dto) {
        try {
            Supplier supplier = supplierMapper.toEntity(dto);
            Supplier savedSupplier = supplierRepository.save(supplier);
            return supplierMapper.toDto(savedSupplier);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A supplier with this ICE already exists."
            );
        }
    }
}