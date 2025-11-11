package com.example.tricolv2sb.Service;

import com.example.tricolv2sb.DTO.CreateGoodsIssueLineDTO;
import com.example.tricolv2sb.DTO.ReadGoodsIssueLineDTO;
import com.example.tricolv2sb.DTO.UpdateGoodsIssueLineDTO;
import com.example.tricolv2sb.Entity.GoodsIssueLine;
import com.example.tricolv2sb.Entity.GoodsIssue;
import com.example.tricolv2sb.Entity.Product;
import com.example.tricolv2sb.Entity.Enum.GoodsIssueStatus;
import com.example.tricolv2sb.Mapper.GoodsIssueLineMapper;
import com.example.tricolv2sb.Repository.GoodsIssueLineRepository;
import com.example.tricolv2sb.Repository.GoodsIssueRepository;
import com.example.tricolv2sb.Repository.ProductRepository;
import com.example.tricolv2sb.Service.ServiceInterfaces.GoodsIssueLineInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsIssueLineService implements GoodsIssueLineInterface {
    
    private final GoodsIssueLineRepository goodsIssueLineRepository;
    private final GoodsIssueRepository goodsIssueRepository;
    private final ProductRepository productRepository;
    private final GoodsIssueLineMapper goodsIssueLineMapper;
    
    @Transactional(readOnly = true)
    public List<ReadGoodsIssueLineDTO> getAllGoodsIssueLines() {
        return goodsIssueLineRepository.findAll()
                .stream()
                .map(goodsIssueLineMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ReadGoodsIssueLineDTO getGoodsIssueLineById(Long id) {
        GoodsIssueLine goodsIssueLine = goodsIssueLineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goods issue line not found with id: " + id));
        return goodsIssueLineMapper.toDto(goodsIssueLine);
    }
    
    public ReadGoodsIssueLineDTO createGoodsIssueLine(CreateGoodsIssueLineDTO createGoodsIssueLineDTO) {
        GoodsIssue goodsIssue = goodsIssueRepository.findById(createGoodsIssueLineDTO.getGoodsIssueId())
                .orElseThrow(() -> new RuntimeException("Goods issue not found with id: " + createGoodsIssueLineDTO.getGoodsIssueId()));
        
        // Only allow adding lines to DRAFT goods issues
        if (goodsIssue.getStatus() != GoodsIssueStatus.DRAFT) {
            throw new RuntimeException("Cannot add lines to goods issue with status: " + goodsIssue.getStatus());
        }
        
        Product product = productRepository.findById(createGoodsIssueLineDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + createGoodsIssueLineDTO.getProductId()));
        
        GoodsIssueLine goodsIssueLine = goodsIssueLineMapper.toEntity(createGoodsIssueLineDTO);
        goodsIssueLine.setGoodsIssue(goodsIssue);
        goodsIssueLine.setProduct(product);
        
        GoodsIssueLine savedGoodsIssueLine = goodsIssueLineRepository.save(goodsIssueLine);
        return goodsIssueLineMapper.toDto(savedGoodsIssueLine);
    }
    
    public ReadGoodsIssueLineDTO updateGoodsIssueLine(Long id, UpdateGoodsIssueLineDTO updateGoodsIssueLineDTO) {
        GoodsIssueLine existingGoodsIssueLine = goodsIssueLineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goods issue line not found with id: " + id));
        
        // Only allow updates if goods issue is DRAFT
        if (existingGoodsIssueLine.getGoodsIssue().getStatus() != GoodsIssueStatus.DRAFT) {
            throw new RuntimeException("Cannot update line for goods issue with status: " + existingGoodsIssueLine.getGoodsIssue().getStatus());
        }
        
        goodsIssueLineMapper.updateEntity(updateGoodsIssueLineDTO, existingGoodsIssueLine);
        GoodsIssueLine updatedGoodsIssueLine = goodsIssueLineRepository.save(existingGoodsIssueLine);
        return goodsIssueLineMapper.toDto(updatedGoodsIssueLine);
    }
    
    public void deleteGoodsIssueLine(Long id) {
        GoodsIssueLine goodsIssueLine = goodsIssueLineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goods issue line not found with id: " + id));
        
        // Only allow deletion if goods issue is DRAFT
        if (goodsIssueLine.getGoodsIssue().getStatus() != GoodsIssueStatus.DRAFT) {
            throw new RuntimeException("Cannot delete line for goods issue with status: " + goodsIssueLine.getGoodsIssue().getStatus());
        }
        
        goodsIssueLineRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<ReadGoodsIssueLineDTO> getGoodsIssueLinesByGoodsIssue(Long goodsIssueId) {
        GoodsIssue goodsIssue = goodsIssueRepository.findById(goodsIssueId)
                .orElseThrow(() -> new RuntimeException("Goods issue not found with id: " + goodsIssueId));
        
        return goodsIssueLineRepository.findByGoodsIssue(goodsIssue)
                .stream()
                .map(goodsIssueLineMapper::toDto)
                .collect(Collectors.toList());
    }
}