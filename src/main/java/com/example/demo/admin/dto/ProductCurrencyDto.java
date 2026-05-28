package com.example.demo.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductCurrencyDto {

    private Long    prod_cur_no;
    private Long    product_id;
    private String  cur_cd;
    private String  cur_nm;
    private String  avail;
    private LocalDateTime reg_dt;
    private LocalDateTime updated_at;
}