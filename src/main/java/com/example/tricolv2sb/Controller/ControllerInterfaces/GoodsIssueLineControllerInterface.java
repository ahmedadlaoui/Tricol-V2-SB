package com.example.tricolv2sb.Controller.ControllerInterfaces;

import com.example.tricolv2sb.DTO.CreateGoodsIssueLineDTO;
import com.example.tricolv2sb.DTO.ReadGoodsIssueLineDTO;
import com.example.tricolv2sb.DTO.UpdateGoodsIssueLineDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

public interface GoodsIssueLineControllerInterface {
    
    @GetMapping
    ResponseEntity<List<ReadGoodsIssueLineDTO>> getAllGoodsIssueLines();
    
    @GetMapping("/{id}")
    ResponseEntity<ReadGoodsIssueLineDTO> getGoodsIssueLineById(@PathVariable Long id);
    
    @PostMapping
    ResponseEntity<ReadGoodsIssueLineDTO> createGoodsIssueLine(@Valid @RequestBody CreateGoodsIssueLineDTO createGoodsIssueLineDTO);
    
    @PutMapping("/{id}")
    ResponseEntity<ReadGoodsIssueLineDTO> updateGoodsIssueLine(@PathVariable Long id, @Valid @RequestBody UpdateGoodsIssueLineDTO updateGoodsIssueLineDTO);
    
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteGoodsIssueLine(@PathVariable Long id);
    
    @GetMapping("/goods-issue/{id}")
    ResponseEntity<List<ReadGoodsIssueLineDTO>> getGoodsIssueLinesByGoodsIssue(@PathVariable Long id);
}