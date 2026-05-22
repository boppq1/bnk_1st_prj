package com.example.demo.admin.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.example.demo.admin.dto.ProductDto;
import com.example.demo.admin.dto.InterestRateDto;
import com.example.demo.admin.dto.PrefRateDto;

@Mapper
public interface IAdminProductDao {

	// ==================== PRODUCTS ====================
	int insertPro(ProductDto dto);
	Long selectCurrentProductNo();
	int insertProPdf(ProductDto dto);
	int insertProRequest(ProductDto dto);

	List<ProductDto> listPro(@Param("offset") int offset, @Param("pageSize") int pageSize);
	int getTotalCount();

	// 이 메서드는 resultMap을 통해 List들을 자동으로 채워줍니다.
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
	// 금리 등록 (InterestRateDto 사용)
	int insertInterestRate(InterestRateDto dto);

	Long selectCurrentRateNo();

	// 리스트 반환 타입 수정
	List<InterestRateDto> listInterestRates(Long product_id);

	// 단건 반환 타입 수정
	InterestRateDto selectInterestRate(Long rate_id);

	// 수정 시 InterestRateDto 사용
	int updateInterestRate(InterestRateDto dto);
	int deleteInterestRate(Long rate_id);
	int deleteInterestRatesByProduct(Long product_id);


	// ==================== PREFERENTIAL_RATE_CONDITIONS ====================
	// 우대 금리 등록 (PrefRateDto 사용)
	int insertPrefRate(PrefRateDto dto);

	// 리스트 반환 타입 수정
	List<PrefRateDto> listPreRate(Long rate_id);

	// 수정 시 PrefRateDto 사용
	int updatePrefRate(PrefRateDto dto);

	int deletePrefRate(Long pref_rate_id);
	int deletePrefRateByProduct(Long product_id);
	int deletePrefRateByRate(Long rate_id);
}