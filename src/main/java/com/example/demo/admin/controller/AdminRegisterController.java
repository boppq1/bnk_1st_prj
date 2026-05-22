    package com.example.demo.admin.controller;

    import com.example.demo.admin.dao.IAdminActionDao;
    import com.example.demo.admin.dao.IAdminDao;
    import com.example.demo.admin.dto.AdminActionLogDto;
    import com.example.demo.admin.dto.AdminDto;
    import com.example.demo.admin.service.AdminMergeService;
    import com.example.demo.interceptor.JwtFilter;
    import com.example.demo.jwt.JwtUtil;
    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.servlet.http.HttpSession;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.util.HashMap;
    import java.util.Map;

    @Controller
    @RequiredArgsConstructor
    public class AdminRegisterController {

        private final AdminMergeService serv;
        private final IAdminDao dao;
        private final IAdminActionDao actionDao;
        private final JwtUtil jwt;
        private final JwtFilter jwtFilter;

        @GetMapping("/adminJoin")
        public String joinPage(Model model) {
            model.addAttribute("admin", new AdminDto());
            return "admin/adminJoin";
        }

        @PostMapping("/admin/join")
        public String join(AdminDto dto) {
            int result = serv.join(dto);

            actionDao.insertActionLog(
                    new AdminActionLogDto("UPDATE_USER", "user_id=3")
            );

            System.out.println("result = " + result);
            return "redirect:/adminLogin"; // 가입 후 로그인 페이지로 리다이렉트
        }

        @GetMapping("/adminLogin")
        public String loginPage(Model model) {
            model.addAttribute("admin", new AdminDto());
            return "admin/adminLogin";
        }

        @PostMapping("/admin/login")
        public String login(@RequestParam("login_id") String loginId,
                            @RequestParam("password") String password,
                            HttpServletResponse response) {

            AdminDto dto = new AdminDto();
            dto.setLogin_id(loginId);
            dto.setPassword(password);

            AdminDto admin = serv.login(dto);

            if (admin == null) {
                return "redirect:/adminLogin?error=true";
            }

            Map<String,Object> claims = new HashMap<>();
            claims.put("admin_id",   admin.getAdmin_id());
            claims.put("login_id",   admin.getLogin_id());
            claims.put("name",       admin.getName());
            claims.put("role",       admin.getAdminRole());
            claims.put("department", admin.getDepartment());

            String token = jwt.generateToken(admin.getLogin_id(), claims);

            Cookie cookie = new Cookie("accessToken", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setSecure(true);
            response.addCookie(cookie);

            return "redirect:/admin/adminMyPage";
        }


        @PostMapping("/admin/update")
        public String updateMyPage(@ModelAttribute AdminDto dto) {

            serv.updateMyPage(dto);

            return "redirect:/admin/adminMyPage";
        }

        @PostMapping("/admin/passwordUpdate")
        public String updatePassword(@RequestParam Long admin_id, @RequestParam String password) {

            serv.updatePassword(admin_id, password);
            return "redirect:/admin/adminMyPage";
        }

        @PostMapping("/admin/updateAdminPw")
        public String updateAdminPw(@RequestParam Long admin_id, @RequestParam String admin_pw) {

            serv.updateAdminPw(admin_id, admin_pw);
            return "redirect:/admin/adminMyPage";
        }


        @GetMapping("/admin/logout")
        public String logout(HttpServletResponse response) {
            Cookie cookie = new Cookie("accessToken", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/adminLogin";
        }


    }

