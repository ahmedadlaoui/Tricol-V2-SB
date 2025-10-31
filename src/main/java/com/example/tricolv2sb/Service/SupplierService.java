package com.example.tricolv2sb.Service;

import com.example.tricolv2sb.DTO.CreateSupplierDTO;
import com.example.tricolv2sb.DTO.ReadSupplierDTO;
import com.example.tricolv2sb.Entity.Supplier;
import com.example.tricolv2sb.Exception.SupplierAlreadyExistsException;
import com.example.tricolv2sb.Exception.SupplierNotFoundException;
import com.example.tricolv2sb.Mapper.SupplierMapper;
import com.example.tricolv2sb.Repository.SupplierRepository;
import com.example.tricolv2sb.Service.ServiceInterfaces.SupplierServiceInterface;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService implements SupplierServiceInterface {

    private final SupplierMapper supplierMapper;
    private final SupplierRepository supplierRepository;

    @Transactional
    public ReadSupplierDTO addSupplier(CreateSupplierDTO dto) {
        supplierRepository.findByIce(dto.getIce())
                .ifPresent(s -> {
                    throw new SupplierAlreadyExistsException(
                            "Supplier with this ICE: " + dto.getIce() + " already exists"
                    );
                });

        Supplier supplier = supplierMapper.toEntity(dto);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return supplierMapper.toDto(savedSupplier);
    }


    @Transactional(readOnly = true)
    public Optional<ReadSupplierDTO> fetchSupplier(Long id) {
        Optional<ReadSupplierDTO> supplierDto = supplierRepository.findById(id)
                .map(supplierMapper::toDto);
        if (supplierDto.isEmpty()) {
            throw new SupplierNotFoundException("Supplier with ID " + id + " not found");
        }
        return supplierDto;
    }


    @Transactional
    public Optional<ReadSupplierDTO> deleteSupplier(Long id){
        Optional<ReadSupplierDTO> supplierDto = supplierRepository.findById(id)
                .map(supplierMapper::toDto);
        if (supplierDto.isEmpty()) {
            throw new SupplierNotFoundException("Supplier with ID " + id + " not found");
        }

    }

}
