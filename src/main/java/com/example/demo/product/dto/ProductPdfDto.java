package com.example.demo.product.dto;

import lombok.Data;

@Data
public class ProductPdfDto {

    private Long pdfId;
    private Long productId;

    private String basicTermsPath;
    private String categoryTermsPath;
    private String specialTermsPath;
    private String productGuidePath;
}
