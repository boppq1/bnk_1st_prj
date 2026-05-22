package com.example.demo.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class BlacklistDto {
	Long blk_no;
	String cli_key;
	String ip_addr;
	String reason;
	String status;
	String reg_dt;
	String upd_dt;
	String lift_reason;
	Long lift_adm_no;
}
