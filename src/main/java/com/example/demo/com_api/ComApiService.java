//package com.example.demo.com_api;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.util.DefaultUriBuilderFactory;
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class ComApiService {
//
//    private final IComApiDao dao;
//    // 💡 키는 공공데이터포털에서 [Decoding]된 키를 그대로 넣으세요.
//    private final String SERVICE_KEY = "Pl%2FLw4ahm8a7Ds4EU%2Fprv4BZqF1%2B3Dmoccw%2Bac%2BT%2FY57WOSY1M8d1aE7u%2BDMC4iRFl18FAvXy%2BOnFOC5K5tyeg%3D%3D";
//
//    public void syncAllCompanies() {
//        // 1. 진짜 사업자번호 목록 가져오기
//        List<CompanyDto> targetList = dao.selectCompany();
//        if (targetList == null || targetList.isEmpty()) return;
//
//        for (CompanyDto company : targetList) {
//            String bNo = company.getBiz_no().replaceAll("[^0-9]", "");
//
//            try {
//                // 2. API 호출
//                String url = "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + SERVICE_KEY;
//                Map<String, Object> body = Map.of("b_no", List.of(bNo));
//
//                ApiResponseDto res = WebClient.create().post()
//                        .uri(URI.create(url))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .bodyValue(body)
//                        .retrieve()
//                        .bodyToMono(ApiResponseDto.class)
//                        .block();
//
//                // 3. 응답이 있으면 DB에 Upsert (등록 또는 수정)
//                if (res != null && res.getData() != null && !res.getData().isEmpty()) {
//                    ApiResponseDto.NtsCompanyData ntsData = res.getData().get(0);
//                    System.out.println("API 응답: b_nm=" + ntsData.getB_nm() + ", b_stt=" + ntsData.getB_stt()); // 추가
//                    CompanyDto dto = new CompanyDto();
//                    dto.setBiz_no(bNo);
//                    dto.setCom_nm(ntsData.getB_nm());
//                    dto.setStatus(ntsData.getB_stt());
//
//                    dao.upsertCompany(dto);
//                }
//
//                // 4. 과부하 방지
//                Thread.sleep(200);
//
//            } catch (Exception e) {
//                System.err.println("동기화 실패 [" + bNo + "]: " + e.getMessage());
//            }
//        }
//    }
//}