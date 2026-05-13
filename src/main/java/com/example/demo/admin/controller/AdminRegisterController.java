package com.example.demo.admin.controller;

import com.example.demo.admin.dao.IAdminActionDao;
import com.example.demo.admin.dao.IAdminDao;
import com.example.demo.admin.dto.AdminActionLogDto;
import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.service.AdminMergeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AdminRegisterController {

    private final AdminMergeService serv;
    private final IAdminDao dao;
    private final IAdminActionDao actionDao;

    //private final JwtUtil jwtUtil;
    @GetMapping("/adminJoin")
    public String joinPage(Model model) {
        model.addAttribute("admin", new AdminDto());
        return "admin/adminJoin";
    }

    @PostMapping("/admin/join")
    public String join(AdminDto dto) {
        int result = serv.join(dto);  // ✅ dao → serv 로 변경

        actionDao.insertActionLog(
                new AdminActionLogDto(
                        "UPDATE_USER",
                        "user_id=3"
                )
        );

        System.out.println("result = " + result);
        return "redirect:/adminLogin"; // 가입 후 로그인 페이지로 리다이렉트
    }

    @GetMapping("/adminLogin")
    public String loginPage(Model model) {
        model.addAttribute("admin", new AdminDto());
        return "admin/adminLogin";
    }

//    @PostMapping("/admin/login") // 로그인 엔드포인트
//    @ResponseBody
//    public ResponseEntity<?> login(@RequestBody AdminDto dto, HttpSession session) {
//        AdminDto result = serv.login(dto);
//
////        if (result == null) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
////                    .body("로그인에 실패했습니다. 아이디 또는 비밀번호를 확인해주세요.");
////        }
////
////        String accessToken = jwtUtil.createAccessToken(result.getAdmin_id());
////        String refreshToken = jwtUtil.createRefreshToken(result.getAdmin_id());
////
////        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
//
//            // 로그인 실패 시 응답
//            if (result == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body("아이디 또는 비밀번호가 올바르지 않습니다.");
//            }
//
//            // 로그인 성공 시 세션에 사용자 정보 저장
//            session.setAttribute("loginUser", result);
//            return ResponseEntity.ok("로그인 성공");
//    }



@PostMapping("/admin/login")
public String login(@RequestParam("login_id") String loginId,
                    @RequestParam("password") String password,
                    HttpSession session) {

    AdminDto dto = new AdminDto();
    dto.setLogin_id(loginId);
    dto.setPassword(password);

    AdminDto admin = serv.login(dto);

    // 로그인 성공
    if(admin != null) {

        session.setAttribute("adminId", admin.getAdmin_id());
        session.setAttribute("loginId", admin.getLogin_id());
        session.setAttribute("name", admin.getName());

        System.out.println("Session ID: " + session.getId());

        return "redirect:/admin/adminMain";
    }

    // 로그인 실패
    return "redirect:/adminLogin?error=true";
}



    @GetMapping("/adminMyPage")
    public String myPage(HttpSession session,
                         Model model) {

        Long adminId =
                (Long) session.getAttribute("adminId");

        System.out.println(adminId);

        // 로그인 안 됨
        if(adminId == null) {
            return "redirect:/adminLogin";
        }

        AdminDto dto = new AdminDto();
        dto.setAdmin_id(adminId);

        AdminDto admin =
                serv.selectMyPage(dto);

        model.addAttribute("admin", admin);

        return "admin/adminMyPage";
    }

    @PostMapping("/admin/update")
    public String updateMyPage(@ModelAttribute AdminDto dto) {

        serv.updateMyPage(dto);

        return "redirect:/admin/adminMyPage";
    }

    @PostMapping("/admin/passwordUpdate")
    public String updatePassword(Long admin_id,
                                 String password) {

        serv.updatePassword(admin_id,
                password);

        return "redirect:/admin/adminMyPage";
    }

    @PostMapping("/admin/updateAdminPw")
    public String updateAdminPw(Long admin_id,
                                String admin_pw) {

        serv.updateAdminPw(admin_id,
                admin_pw);

        return "redirect:/admin/adminMyPage";
    }





}

