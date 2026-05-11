package com.example.demo.product.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

//상세 페이지 전체 데이터.
@Data
public class ProductDetailResponseDto {

    private Long productId; // 상품 ID

    private String isActive; // 판매 여부 : T : 판매중 / F : 판매종료

    private String productName; // 상품명


    private String productType; // 상품 유형

    private String targetCustomer; // 가입 대상

    private Integer minPeriodMonth; // 최소 가입 기간


    private Integer maxPeriodMonth; // 최대 가입 기간

    private Long minAmount; // 최소 가입 금액

    private Long maxAmount;// 최대 가입 금액

    private String productDesc; // 상품 설명

    private String interestPaymentType; //이자 지급 방식

    private String appliedExchangeRateType; //적용 환율 기준

    private LocalDate appStartDate; // 신청 시작일

    private LocalDate appEndDate; // 신청 종료일

    private String mktMsgTop;// 마케팅 문구 상단

    private String mktMsgBottom; // 마케팅 문구 하단

    private String joinMethod; // 가입 방법

    private String autoTransferSvc; // 자동이체 서비스

    private String revolvingInfo; // 자동 갱신 안내

    private String savingMethod; // 적립 방법

    private String paymentRestriction; // 원금/이자 지급 제한

    private String precautions; // 유의 사항

    private String fxCashFee;// 외화 현찰 수수료

    private String autoReinvestment;// 자동재예치 안내

    private String taxBenefit;// 세금우대 정보

    private String paymentLimitInfo;// 지급 제한 정보

    private String dataAccessRight; // 자료열람 요구권

    private String illegalContractCancel; // 위법계약 해지권

    private String depositorProtection; //예금자 보호

    private String rateCategory; // 금리 카테고리

    private String approveStatus;// 승인 상태

    private List<ProductCurrencyResponseDto> currencies;//통화 리스트

    private List<ProductRateResponseDto> rates; // 금리 리스트

    private List<PreferentialRateResponseDto> preferentialRates; // 우대 금리 리스트
}