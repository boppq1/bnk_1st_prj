package com.example.demo.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.example.demo.admin.dto.ProductDto;
import com.example.demo.admin.dto.InterestRateDto;
import com.example.demo.admin.dto.PrefRateDto;
import com.example.demo.admin.dto.ProductCurrencyDto;

@Mapper
public interface IAdminProductDao {

    // ==================== PRODUCTS ====================
    int insertPro(ProductDto dto);
    int insertProPdf(ProductDto dto);
    int insertProRequest(ProductDto dto);

    /** 기존 단순 목록 (currencies 미포함) */
    List<ProductDto> listPro(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /** 목록 + 취급 통화 포함 (productListPage 용) */
    List<ProductDto> listProWithCurrencies(@Param("offset") int offset, @Param("pageSize") int pageSize);

    int getTotalCount();
    ProductDto listDetail(Long product_id);
    List<ProductDto> selectByStatus(String approve_status);
    int saveProduct(ProductDto dto);
    int submitProduct(ProductDto dto);
    void submitProductRequest(Long product_id);
    int updateProPdf(ProductDto dto);
    int deleteProductPdf(Long product_id);
    int deleteProductRequest(Long product_id);
    int deletePro(Long product_id);
    int getCountByType(String product_type);
    int getCountByStatus(String approve_status);

    // ==================== INTEREST_RATES ====================
    int insertInterestRate(InterestRateDto dto);
    Long selectCurrentRateNo();
    List<InterestRateDto> listInterestRates(Long product_id);
    InterestRateDto selectInterestRate(Long rate_id);
    int updateInterestRate(InterestRateDto dto);
    int deleteInterestRate(Long rate_id);
    int deleteInterestRatesByProduct(Long product_id);

    // ==================== PREFERENTIAL_RATE_CONDITIONS ====================
    int insertPrefRate(PrefRateDto dto);
    List<PrefRateDto> listPreRate(Long rate_id);
    int updatePrefRate(PrefRateDto dto);
    int deletePrefRate(Long pref_rate_id);
    int deletePrefRateByProduct(Long product_id);
    int deletePrefRateByRate(Long rate_id);

    // ==================== PRODUCT_CURRENCIES ====================
    int insertProductCurrency(ProductCurrencyDto dto);
    int deleteProductCurrenciesByProduct(Long product_id);
    List<ProductCurrencyDto> listProductCurrencies(Long product_id);
    int disableProductCurrency(Long prod_cur_no);

    // ==================== 검색 ====================
    List<ProductDto> searchProducts(Map<String, Object> param);
    int searchProductCount(Map<String, Object> param);
}