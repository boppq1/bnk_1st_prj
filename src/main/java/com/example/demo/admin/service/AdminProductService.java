package com.example.demo.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.admin.dao.IAdminProductDao;
import com.example.demo.admin.dto.InterestRateDto;
import com.example.demo.admin.dto.PrefRateDto;
import com.example.demo.admin.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final IAdminProductDao productDao;

    // 상품 최초 등록 (계층형 데이터 처리)
    @Transactional
    public void registerProduct(ProductDto dto) {
        // 1. 상품 등록
        productDao.insertPro(dto);
        Long productId = productDao.selectCurrentProductNo();
        dto.setProduct_id(productId);

        // 2. 금리 및 우대금리 리스트 순회 처리
        if (dto.getRates() != null) {
            for (InterestRateDto rate : dto.getRates()) {
                rate.setProduct_id(productId);
                productDao.insertInterestRate(rate);

                Long rateId = productDao.selectCurrentRateNo();

                // 우대금리 등록
                if (rate.getPrefRateConditions() != null) {
                    for (PrefRateDto pref : rate.getPrefRateConditions()) {
                        pref.setRate_id(rateId);
                        productDao.insertPrefRate(pref);
                    }
                }
            }
        }

        // 3. PDF 및 신청 등록
        productDao.insertProPdf(dto);
        productDao.insertProRequest(dto);
    }

    // 상품 목록
    public List<ProductDto> getProductList(int offset, int pageSize) {
        return productDao.listPro(offset, pageSize);
    }

    public int getTotalCount() {
        return productDao.getTotalCount();
    }

    // 상품 상세 (MyBatis resultMap이 계층형 데이터를 한 번에 가져옴)
    public ProductDto getProductDetail(Long product_id) {
        return productDao.listDetail(product_id);
    }

    // 상태별 조회
    public List<ProductDto> getProductByStatus(String approve_status) {
        return productDao.selectByStatus(approve_status);
    }

    // 저장 및 제출 (PDF 수정 포함)
    @Transactional
    public void saveProduct(ProductDto dto) {
        productDao.saveProduct(dto);
        productDao.updateProPdf(dto);
    }

    @Transactional
    public void submitProduct(ProductDto dto) {
        productDao.submitProduct(dto);
        productDao.updateProPdf(dto);
    }

    // 관리자 수정 (복잡한 리스트 수정은 삭제 후 재등록이 가장 깔끔합니다)
    @Transactional
    public void updateProduct(ProductDto dto) {
        productDao.saveProduct(dto);
        productDao.submitProductRequest(dto.getProduct_id());
        productDao.updateProPdf(dto);

        // 기존 금리/우대금리 전체 삭제 후 재등록 방식
        productDao.deletePrefRateByProduct(dto.getProduct_id());
        productDao.deleteInterestRatesByProduct(dto.getProduct_id());

        // 다시 리스트 등록
        if (dto.getRates() != null) {
            for (InterestRateDto rate : dto.getRates()) {
                rate.setProduct_id(dto.getProduct_id());
                productDao.insertInterestRate(rate);

                Long rateId = productDao.selectCurrentRateNo();
                if (rate.getPrefRateConditions() != null) {
                    for (PrefRateDto pref : rate.getPrefRateConditions()) {
                        pref.setRate_id(rateId);
                        productDao.insertPrefRate(pref);
                    }
                }
            }
        }
    }

    public void updateProductStatus(ProductDto dto) {
        // XML 쿼리 구현 필요 시 추가
    }

    // 삭제 (자식부터 삭제)
    @Transactional
    public void deleteProduct(Long product_id) {
        productDao.deletePrefRateByProduct(product_id);
        productDao.deleteInterestRatesByProduct(product_id);
        productDao.deleteProductPdf(product_id);
        productDao.deleteProductRequest(product_id);
        productDao.deletePro(product_id);
    }

    public int getCountByType(String product_type) {
        return productDao.getCountByType(product_type);
    }

    public int getCountByStatus(String approve_status) {
        return productDao.getCountByStatus(approve_status);
    }

    // 단독 CRUD용 메서드들
    public List<InterestRateDto> getInterestRatesByProduct(Long product_id) {
        return productDao.listInterestRates(product_id);
    }

    public InterestRateDto getInterestRateDetail(Long rate_id) {
        return productDao.selectInterestRate(rate_id);
    }

    public List<PrefRateDto> getPrefRatesByRateId(Long rate_id) {
        return productDao.listPreRate(rate_id);
    }
}