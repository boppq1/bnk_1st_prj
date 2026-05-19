package com.example.demo.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.admin.dao.IAdminProductDao;
import com.example.demo.admin.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final IAdminProductDao productDao;


    // 상품 최초 등록
    @Transactional
    public void registerProduct(ProductDto dto) {

        // 1. 상품 등록
        productDao.insertPro(dto);

        // 2. 생성된 PK 조회
        Long productId =
                productDao.selectCurrentProductNo();

        // 3. DTO에 세팅
        dto.setProduct_id(productId);

        // 4. PDF 등록
        productDao.insertProPdf(dto);

        // 5. 신청 등록
        dto.setProduct_id(productId);
        productDao.insertProRequest(dto);
    }


    // 상품 목록
    public List<ProductDto> getProductList(int offset, int pageSize) {
        return productDao.listPro(offset, pageSize);
    }

    public int getTotalCount() {
        return productDao.getTotalCount();
    }


    // 상품 상세
    public ProductDto getProductDetail(Long product_id) {

        return productDao.listDetail(product_id);
    }


    // 상태별 조회
    public List<ProductDto> getProductByStatus(
            String approve_status) {

        return productDao.selectByStatus(approve_status);
    }


    // 저장
    @Transactional
    public void saveProduct(ProductDto dto) {

        productDao.saveProduct(dto);

        productDao.updateProPdf(dto);
    }


    // 제출
    @Transactional
    public void submitProduct(ProductDto dto) {

        productDao.submitProduct(dto);

        productDao.updateProPdf(dto);
    }


    // 관리자 수정
    @Transactional
    public void updateProduct(ProductDto dto) {

        productDao.updatePro(dto);

        productDao.updateProPdf(dto);
    }


    // 판매 상태 변경
    public void updateProductStatus(ProductDto dto) {

        productDao.updateProductStatus(dto);
    }


    // 삭제
    @Transactional
    public void deleteProduct(Long product_no) {

        productDao.deleteProductPdf(product_no);

        productDao.deleteProductRequest(product_no);

        productDao.deletePro(product_no);
    }

}