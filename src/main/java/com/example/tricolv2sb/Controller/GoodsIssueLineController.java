package com.example.tricolv2sb.Controller;

import com.example.tricolv2sb.Controller.ControllerInterfaces.GoodsIssueLineControllerInterface;
import com.example.tricolv2sb.DTO.CreateGoodsIssueLineDTO;
import com.example.tricolv2sb.DTO.ReadGoodsIssueLineDTO;
import com.example.tricolv2sb.DTO.UpdateGoodsIssueLineDTO;
import com.example.tricolv2sb.Service.GoodsIssueLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/goods-issue-lines")
@RequiredArgsConstructor
public class GoodsIssueLineController implements GoodsIssueLineControllerInterface {
    
    private final GoodsIssueLineService goodsIssueLineService;
    
    @Override
    public ResponseEntity<List<ReadGoodsIssueLineDTO>> getAllGoodsIssueLines() {
        List<ReadGoodsIssueLineDTO> goodsIssueLines = goodsIssueLineService.getAllGoodsIssueLines();
        return ResponseEntity.ok(goodsIssueLines);
    }
    
    @Override
    public ResponseEntity<ReadGoodsIssueLineDTO> getGoodsIssueLineById(Long id) {
        ReadGoodsIssueLineDTO goodsIssueLine = goodsIssueLineService.getGoodsIssueLineById(id);
        return ResponseEntity.ok(goodsIssueLine);
    }
    
    @Override
    public ResponseEntity<ReadGoodsIssueLineDTO> createGoodsIssueLine(@Valid CreateGoodsIssueLineDTO createGoodsIssueLineDTO) {
        ReadGoodsIssueLineDTO createdGoodsIssueLine = goodsIssueLineService.createGoodsIssueLine(createGoodsIssueLineDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGoodsIssueLine);
    }
    
    @Override
    public ResponseEntity<ReadGoodsIssueLineDTO> updateGoodsIssueLine(Long id, @Valid UpdateGoodsIssueLineDTO updateGoodsIssueLineDTO) {
        ReadGoodsIssueLineDTO updatedGoodsIssueLine = goodsIssueLineService.updateGoodsIssueLine(id, updateGoodsIssueLineDTO);
        return ResponseEntity.ok(updatedGoodsIssueLine);
    }
    
    @Override
    public ResponseEntity<Void> deleteGoodsIssueLine(Long id) {
        goodsIssueLineService.deleteGoodsIssueLine(id);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<List<ReadGoodsIssueLineDTO>> getGoodsIssueLinesByGoodsIssue(Long id) {
        List<ReadGoodsIssueLineDTO> goodsIssueLines = goodsIssueLineService.getGoodsIssueLinesByGoodsIssue(id);
        return ResponseEntity.ok(goodsIssueLines);
    }
}