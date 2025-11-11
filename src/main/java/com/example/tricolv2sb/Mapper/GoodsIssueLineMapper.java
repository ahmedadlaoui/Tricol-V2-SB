package com.example.tricolv2sb.Mapper;

import com.example.tricolv2sb.DTO.CreateGoodsIssueLineDTO;
import com.example.tricolv2sb.DTO.ReadGoodsIssueLineDTO;
import com.example.tricolv2sb.DTO.UpdateGoodsIssueLineDTO;
import com.example.tricolv2sb.Entity.GoodsIssueLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GoodsIssueLineMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "goodsIssue", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "stockMovements", ignore = true)
    GoodsIssueLine toEntity(CreateGoodsIssueLineDTO createDto);
    
    @Mapping(source = "goodsIssue.id", target = "goodsIssueId")
    @Mapping(source = "goodsIssue.issueNumber", target = "goodsIssueNumber")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.reference", target = "productReference")
    @Mapping(source = "product.name", target = "productName")
    ReadGoodsIssueLineDTO toDto(GoodsIssueLine goodsIssueLine);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "goodsIssue", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "stockMovements", ignore = true)
    void updateEntity(UpdateGoodsIssueLineDTO updateDto, @MappingTarget GoodsIssueLine goodsIssueLine);
}