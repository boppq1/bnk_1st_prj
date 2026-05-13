package com.example.demo.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRequestDto {
	private Long exchange_id;
	private String type;
	private Long from_account;
	private Long to_account;
	private String from_currency;
	private String to_currency;
	private Long from_amount;
	private Long to_amount;
	private Long applied_rate;
}
