//package com.example.demo.com_api;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller; // 💡 Controller로 변경
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody; // 💡 추가
//
//@Controller
//@RequestMapping("/api/company")
//@RequiredArgsConstructor
//public class ComApiController {
//
//    private final ComApiService comApiService;
//
//    // 1. 관리자 페이지로 이동
//    @GetMapping("/adminApiPage")
//    public String adminApiPage() {
//        return "admin/adminApiPage";
//    }
//
//    // 2. 버튼 클릭 시 데이터 동기화 (서비스 호출)
//    @PostMapping("/sync-all")
//    @ResponseBody
//    public ResponseEntity<String> syncAllCompaniesNow() {
//        try {
//            // 위에서 합친 서비스 메서드 호출
//            comApiService.syncAllCompanies();
//
//            return ResponseEntity.ok("성공적으로 전체 데이터를 업데이트했습니다.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("업데이트 실패: " + e.getMessage());
//        }
//    }
//}
