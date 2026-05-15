package com.example.demo.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDto {

	Long product_id;

	String ia_active;
	String product_name;
	String product_type;

	Long min_period_month;
	Long max_period_month;

	BigDecimal min_amount;
	BigDecimal max_amount;

	String target_large;
	String target_detail;

	String product_desc;

	String interest_payment_type;
	String applied_exchange_rate_type;

	LocalDate create_date;
	LocalDate update_date;

	String created_by;
	String approver;
	String approve_status;
	String updated_by;

	LocalDate app_start_date;
	LocalDate app_end_date;

	String rate_category;

	// PDF PATH
	String basic_terms_path;
	String category_terms_path;
	String special_terms_path;
	String product_guide_path;

	// REQUEST
	String requester_id;
}