package com.example.demo.admin.dto;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminEventDto {

    Long event_no;
    String event_ttl;
    String event_cont;
    Long event_wtr_no;
    String event_dt;
    Long views;
    String admin_name;
}
