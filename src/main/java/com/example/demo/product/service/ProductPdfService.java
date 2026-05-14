package com.example.demo.product.service;

import org.springframework.stereotype.Service;

import com.example.demo.product.dao.ProductPdfDao;
import com.example.demo.product.dto.ProductPdfDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductPdfService {

    private final ProductPdfDao productPdfDao;

    public ProductPdfDto getPdfByProductId(Long productId) {
        return productPdfDao.findByProductId(productId);
    }
}
