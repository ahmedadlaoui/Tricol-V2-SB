package com.example.tricolv2sb.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateSupplierDTO {
    private String CompanyName;

    private String Address;

    private String Email;
}
