package com.example.demo.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductApprovalDto {
	
	private Long product_id;

	private String product_name;
	private String product_type;
	private String is_active;

	private String target_large;
	private String target_detail;

	private String product_desc;

	private Long min_period_month;
	private Long max_period_month;

	private BigDecimal min_amount;
	private BigDecimal max_amount;

	private String interest_payment_type;
	private String applied_exchange_rate_type;
	private String rate_category;
	private String approve_status;

	private LocalDate app_start_date;
	private LocalDate app_end_date;

	private String currency_code;
	private String currency_name;
	private String is_available;

	private Long rate_id;

	private String customer_type;
	private String residency_type;

	private Long period_month;

	private BigDecimal base_rate;
	private BigDecimal maturity_rate;
	private BigDecimal early_cancel_rate;

	private String rate_type;

	private BigDecimal preferential_rate;

	private LocalDate rate_start_date;
	private LocalDate rate_end_date;

	private Long pref_rate_id;

	private String condition_type;

	private String condition_desc;

	private BigDecimal pref_rate;
	
}
