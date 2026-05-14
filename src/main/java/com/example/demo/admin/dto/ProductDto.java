package com.example.demo.admin.dto;

import java.time.LocalDateTime;

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
	String is_active;
	String product_name;
	String product_type;
	Long min_period_month;
	Long max_period_month;
	Long min_amount;
	Long max_amount;
	String target_large;
	String target_detail;
	String product_desc;
	String interest_payment_type;
	String applied_exchange_rate_type;
	LocalDateTime created_at;
	LocalDateTime updated_at;
	String created_by;
	String approver;
	String approve_status;
	String updated_by;
	LocalDateTime app_start_date;
	LocalDateTime app_end_date;
	String rate_category;
}
