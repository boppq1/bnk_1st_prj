package com.example.demo.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDto {
	private Long product_id;
	private String active;
	private String product_name;
	private String product_type;
	private Long min_period_month;
	private Long max_period_month;
	private BigDecimal min_amount;
	private BigDecimal max_amount;
	private String target_large;
	private String target_detail;
	private String product_desc;
	private String interest_payment_type;
	private String applied_exchange_rate_type;
	private LocalDate create_date;
	private LocalDate update_date;
	private String created_by;
	private String approver;
	private String approve_status;
	private String updated_by;
	private LocalDate app_start_date;
	private LocalDate app_end_date;
	private String rate_category;

	// PDF 관련
	private String basic_terms_path;
	private String category_terms_path;
	private String special_terms_path;
	private String product_guide_path;

	// REQUEST
	private String requester_id;

	// 핵심: 금리 리스트 포함
	private List<InterestRateDto> rates;

	private List<ProductCurrencyDto> currencies;
}