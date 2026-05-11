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
	String product_name;
	String is_active;
	String product_type;
	Long min_period_month;
	Long max_period_month;
	Long min_amount;
	Long max_amount;
	String target_customer;
	String product_desc;
	String interest_payment_type;
	String applied_exchange_rate_type;
	LocalDateTime created_at;
	LocalDateTime updated_at;
	String created_by;
	String updated_by;
	// 추가
	String approver;
	String approve_status;
	LocalDateTime app_start_date;
	LocalDateTime app_end_date;
	String mkt_msg_top;
	String mkt_msg_bottom;
	String join_method;
	String auto_transfer_svc;
	String revolving_info;
	String saving_method;
	String payment_restriction;
	String precautions;
	String fx_cash_fee;
	String auto_reinvestment;
	String tax_benefit;
	String payment_limit_info;
	String data_access_right;
	String illegal_contract_cancel;
	String depositor_protection;
	String rate_category;
}
