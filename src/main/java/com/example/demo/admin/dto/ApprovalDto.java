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
	private Long product_id;
	private String requester_id;
	private String approver_id;
	private String request_status;
	private String request_type;
	private String rejection_reason;
	private LocalDateTime requested_at;
	private LocalDateTime processed_at;
}
