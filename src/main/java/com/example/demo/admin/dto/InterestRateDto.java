package com.example.demo.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class InterestRateDto {
    private Long rate_id;
    private Long product_id;
    private String currency_code;
    private String customer_type;
    private String resident_type;
    private Long period_month;
    private BigDecimal base_rate;
    private BigDecimal maturity_rate;
    private BigDecimal early_cancel_rate;
    private BigDecimal preferential_rate;
    private LocalDate rate_start_date;
    private LocalDate rate_end_date;
    private String is_active;
    private String rate_type;
    private LocalDate reg_date;
    private LocalDate update_date;

    // 핵심: 우대금리 리스트 포함
    private List<PrefRateDto> prefRateConditions;
}