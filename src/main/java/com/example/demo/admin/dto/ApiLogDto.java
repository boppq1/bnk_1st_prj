package com.example.demo.admin.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiLogDto {
	private Long log_id;
	private String url;
	private String method;
	private Long elapsed_ms;
	private String request_body;
	private Long response_code;
	private String ip_address;
	private String reg_dt;
}
