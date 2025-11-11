package com.example.tricolv2sb.Service.ServiceInterfaces;

import com.example.tricolv2sb.DTO.CreateGoodsIssueLineDTO;
import com.example.tricolv2sb.DTO.ReadGoodsIssueLineDTO;
import com.example.tricolv2sb.DTO.UpdateGoodsIssueLineDTO;

import java.util.List;

public interface GoodsIssueLineInterface {
    
    List<ReadGoodsIssueLineDTO> getAllGoodsIssueLines();
    
    ReadGoodsIssueLineDTO getGoodsIssueLineById(Long id);
    
    ReadGoodsIssueLineDTO createGoodsIssueLine(CreateGoodsIssueLineDTO createGoodsIssueLineDTO);
    
    ReadGoodsIssueLineDTO updateGoodsIssueLine(Long id, UpdateGoodsIssueLineDTO updateGoodsIssueLineDTO);
    
    void deleteGoodsIssueLine(Long id);
    
    List<ReadGoodsIssueLineDTO> getGoodsIssueLinesByGoodsIssue(Long goodsIssueId);
}