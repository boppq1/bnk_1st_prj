package com.example.demo.admin.dto;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PrefRateDto {
    private Long pref_rate_id;
    private Long rate_id;
    private String condition_type;
    private String condition_desc;
    private BigDecimal preferential_rate;
}