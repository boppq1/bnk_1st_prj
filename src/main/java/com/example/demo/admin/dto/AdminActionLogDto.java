package com.example.demo.admin.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AdminActionLogDto {
    String log_id;
    String action;
    String target;
    String created_at;
}
