package com.example.demo.admin.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AdminLogDto {
    Long log_id;
    String url;
    String method;
    String elapsed_ms;
    String request_body;
    String response_code;
}
