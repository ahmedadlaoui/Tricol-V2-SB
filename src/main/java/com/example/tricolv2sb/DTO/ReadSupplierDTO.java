package com.example.tricolv2sb.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadSupplierDTO {

    @NotBlank
    private String companyName;

    @Email
    private String Email;

    @NotBlank
    private String phone;
}
