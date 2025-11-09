package com.example.tricolv2sb.DTO;

import com.example.tricolv2sb.Entity.Enum.GoodsIssueMotif;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateGoodsIssueDTO {

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Motif is required")
    private GoodsIssueMotif motif;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;
}
