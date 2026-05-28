package com.example.demo.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.admin.dao.IAdminProductDao;
import com.example.demo.admin.dto.InterestRateDto;
import com.example.demo.admin.dto.PrefRateDto;
import com.example.demo.admin.dto.ProductCurrencyDto;
import com.example.demo.admin.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final IAdminProductDao productDao;

    // 통화 리스트 전체 재등록

    private void reinsertCurrencies(Long productId, List<ProductCurrencyDto> currencies) {
        productDao.deleteProductCurrenciesByProduct(productId);
        if (currencies != null) {
            for (ProductCurrencyDto cur : currencies) {
                cur.setProduct_id(productId);
                productDao.insertProductCurrency(cur);
            }
        }
    }

    // 상품 최초 등록
    @Transactional
    public void registerProduct(ProductDto dto) {
        productDao.insertPro(dto);
        Long productId = dto.getProduct_id();

        reinsertCurrencies(productId, dto.getCurrencies());

        if (dto.getRates() != null) {
            for (InterestRateDto rate : dto.getRates()) {
                rate.setProduct_id(productId);

                // 금리 등록
                productDao.insertInterestRate(rate);
                Long rateId = rate.getRate_id();

                if (rate.getPrefRateConditions() != null) {
                    for (PrefRateDto pref : rate.getPrefRateConditions()) {
                        pref.setRate_id(rateId);
                        productDao.insertPrefRate(pref);
                    }
                }
            }
        }

        productDao.insertProPdf(dto);
        productDao.insertProRequest(dto);
    }

    // 상품 목록 / 상세
    public List<ProductDto> getProductListWithCurrencies(int offset, int pageSize) {
        return productDao.listProWithCurrencies(offset, pageSize);
    }

    public List<ProductDto> getProductList(int offset, int pageSize) {
        return productDao.listPro(offset, pageSize);
    }

    public int getTotalCount() {
        return productDao.getTotalCount();
    }

    public ProductDto getProductDetail(Long product_id) {
        return productDao.listDetail(product_id);
    }

    public List<ProductDto> getProductByStatus(String approve_status) {
        return productDao.selectByStatus(approve_status);
    }

    // 저장 (임시 저장)
    @Transactional
    public void saveProduct(ProductDto dto) {
        productDao.saveProduct(dto);
        productDao.insertProPdf(dto);

        reinsertCurrencies(dto.getProduct_id(), dto.getCurrencies());

        productDao.deletePrefRateByProduct(dto.getProduct_id());
        productDao.deleteInterestRatesByProduct(dto.getProduct_id());

        if (dto.getRates() != null && !dto.getRates().isEmpty()) {
            for (InterestRateDto rate : dto.getRates()) {
                rate.setProduct_id(dto.getProduct_id());

                productDao.insertInterestRate(rate);
                Long rateId = rate.getRate_id(); // (앞서 selectKey 적용한 부분)

                if (rate.getPrefRateConditions() != null) {
                    for (PrefRateDto pref : rate.getPrefRateConditions()) {
                        pref.setRate_id(rateId);
                        productDao.insertPrefRate(pref);
                    }
                }
            }
        }
    }

    // 제출 (승인 요청)- 약관은 매번 insert (이력 누적)
    @Transactional
    public void submitProduct(ProductDto dto) {
        productDao.submitProduct(dto);
        productDao.insertProPdf(dto);

        reinsertCurrencies(dto.getProduct_id(), dto.getCurrencies());

        productDao.deletePrefRateByProduct(dto.getProduct_id());
        productDao.deleteInterestRatesByProduct(dto.getProduct_id());

        if (dto.getRates() != null && !dto.getRates().isEmpty()) {
            for (InterestRateDto rate : dto.getRates()) {
                rate.setProduct_id(dto.getProduct_id());

                productDao.insertInterestRate(rate);
                Long rateId = rate.getRate_id(); // (앞서 selectKey 적용한 부분)

                if (rate.getPrefRateConditions() != null) {
                    for (PrefRateDto pref : rate.getPrefRateConditions()) {
                        pref.setRate_id(rateId);
                        productDao.insertPrefRate(pref);
                    }
                }
            }
        }
    }

    // 수정 - 약관은 매번 insert (이력 누적)
    @Transactional
    public void updateProduct(ProductDto dto) {
        productDao.saveProduct(dto);
        productDao.submitProductRequest(dto.getProduct_id());
        productDao.insertProPdf(dto);

        // 통화 재등록
        reinsertCurrencies(dto.getProduct_id(), dto.getCurrencies());

        // 금리/우대금리 전체 삭제 후 재등록
        productDao.deletePrefRateByProduct(dto.getProduct_id());
        productDao.deleteInterestRatesByProduct(dto.getProduct_id());

        if (dto.getRates() != null) {
            for (InterestRateDto rate : dto.getRates()) {
                rate.setProduct_id(dto.getProduct_id());
                productDao.insertInterestRate(rate);

                Long rateId = rate.getRate_id();

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

    // 삭제
    @Transactional
    public void deleteProduct(Long product_id) {
        productDao.deletePrefRateByProduct(product_id);
        productDao.deleteInterestRatesByProduct(product_id);
        productDao.deleteProductCurrenciesByProduct(product_id);   // ← 통화 삭제 추가
        productDao.deleteProductPdf(product_id);
        productDao.deleteProductRequest(product_id);
        productDao.deletePro(product_id);
    }

    // 통화 단독 조회
    public List<ProductCurrencyDto> getCurrenciesByProduct(Long product_id) {
        return productDao.listProductCurrencies(product_id);
    }

    // ================================================================
    public int getCountByType(String product_type) {
        return productDao.getCountByType(product_type);
    }

    public int getCountByStatus(String approve_status) {
        return productDao.getCountByStatus(approve_status);
    }

    public List<InterestRateDto> getInterestRatesByProduct(Long product_id) {
        return productDao.listInterestRates(product_id);
    }

    public InterestRateDto getInterestRateDetail(Long rate_id) {
        return productDao.selectInterestRate(rate_id);
    }

    public List<PrefRateDto> getPrefRatesByRateId(Long rate_id) {
        return productDao.listPreRate(rate_id);
    }

    public List<ProductDto> searchProducts(Map<String, Object> param) {
        return productDao.searchProducts(param);
    }

    public int searchProductCount(Map<String, Object> param) {
        return productDao.searchProductCount(param);
    }

    // 약관 PDF 추가 등록
    @Transactional
    public int addProductPdf(ProductDto dto) {
        return productDao.insertProPdf(dto);
    }

    // 약관 이력 조회
    public List<Map<String, Object>> listProductPdfHistory(Long product_id) {
        return productDao.listProductPdfHistory(product_id);
    }
}