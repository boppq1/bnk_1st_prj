//package com.example.demo.com_api;
//
//import lombok.Data;
//import java.util.List;
//
//@Data
//public class ApiResponseDto {
//    private String status_code;
//    private Integer request_cnt;
//    private Integer match_cnt;
//    private List<NtsCompanyData> data;
//
//    @Data
//    public static class NtsCompanyData {
//        private String b_no;         // 사업자등록번호
//        private String b_stt;        // 사업자 상태 (계속사업자 / 휴업 / 폐업)
//        private String b_stt_cd;     // 상태 코드 (01: 계속, 02: 휴업, 03: 폐업)
//        private String tax_type;     // 과세유형 (부가가치세 일반과세자 등)
//        private String end_dt;       // 폐업일 (폐업자인 경우만)
//        private String b_nm;         // 국세청에 등록된 진짜 [회사 상호명] ⭐️
//    }
//}