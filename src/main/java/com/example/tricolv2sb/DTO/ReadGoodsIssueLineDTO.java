package com.example.tricolv2sb.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadGoodsIssueLineDTO {
    
    private Long id;
    private Double quantity;
    
    // Goods issue info
    private Long goodsIssueId;
    private String goodsIssueNumber;
    
    // Product info
    private Long productId;
    private String productReference;
    private String productName;
}