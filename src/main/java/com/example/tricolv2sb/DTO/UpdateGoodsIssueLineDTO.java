package com.example.tricolv2sb.DTO;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateGoodsIssueLineDTO {
    
    @Positive(message = "Quantity must be positive")
    private Double quantity;
}