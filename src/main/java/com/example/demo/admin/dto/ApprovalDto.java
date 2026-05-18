package com.example.demo.admin.dto;

import java.time.LocalDateTime;

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
public class ApprovalDto {
	private Long request_id;
	private String product_id;
	private String product_name;
	private String requester_name;
	private String approver_name;
	private String request_status;
	private String request_type;
	private String rejection_reason;
	private LocalDateTime requested_at;
	private LocalDateTime processed_at;
}
