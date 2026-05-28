package com.example.demo.product.service;

import org.springframework.stereotype.Service;

import com.example.demo.product.dao.ProductPdfDao;
import com.example.demo.product.dto.ProductPdfDto;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPdfService {

    private final ProductPdfDao productPdfDao;

    public ProductPdfDto getPdfByProductId(Long productId) {
        // 2. Dao에서 여러 개(List)를 안전하게 받아옵니다.
        List<ProductPdfDto> pdfList = productPdfDao.findByProductId(productId);

        // 3. 리스트가 비어있지 않다면 가장 첫 번째(index 0) 데이터 딱 1개만 꺼내서 반환합니다.
        if (pdfList != null && !pdfList.isEmpty()) {
            return pdfList.get(0);
        }

        // 4. 만약 데이터가 아무것도 없다면 null을 반환합니다.
        return null;
    }
}
