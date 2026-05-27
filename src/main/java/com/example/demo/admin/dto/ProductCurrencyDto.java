package com.example.demo.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductCurrencyDto {

    private Long    prod_cur_no;   // PK
    private Long    product_id;    // FK → products.prod_no
    private String  cur_cd;        // 통화 코드  (e.g. USD, EUR)
    private String  cur_nm;        // 통화 명    (e.g. 미국 달러)
    private String  avail;         // Y / N
    private LocalDateTime reg_dt;
    private LocalDateTime updated_at;
}