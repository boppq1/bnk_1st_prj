    package com.example.demo.admin.controller;

    import com.example.demo.admin.dao.IAdminActionDao;
    import com.example.demo.admin.dao.IAdminDao;
    import com.example.demo.admin.dto.AdminActionLogDto;
    import com.example.demo.admin.dto.AdminDto;
    import com.example.demo.admin.service.AdminMergeService;
    import com.example.demo.jwt.JwtAuthFilter;
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
        private final JwtAuthFilter jwtFilter;

        @GetMapping("/adminJoin")
        public String joinPage(Model model) {
            model.addAttribute("admin", new AdminDto());
            return "admin/adminJoin";
        }

        @PostMapping("/admin/join")
        public String join(AdminDto dto) {
            int result = serv.join(dto);

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

            AdminDto admin = serv.login(loginId, password);

            if (admin == null) {
                return "redirect:/adminLogin?error=true";
            }

            Map<String,Object> claims = new HashMap<>();
            claims.put("adminId",   admin.getAdmin_id());
            claims.put("login_id",   admin.getLogin_id());
            claims.put("name",       admin.getName());
            claims.put("role",       admin.getAdminRole());
            claims.put("department", admin.getDepartment());

            String token = jwt.generateToken(admin.getName(), claims);

            Cookie cookie = new Cookie("accessToken", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/admin/adminMyPage";
        }


        @GetMapping("/admin/adminMyPage")
        public String myPage(HttpServletRequest request, Model model, @CookieValue(value = "accessToken") String token ) {
//        	String id = "";
//        	// 쿠키는 기존에 어떤 정보가 있기때문에 내가 토큰 하나를 넣더라도
//        	// 배열로 받아야한다.
//        	Cookie[] cookies = request.getCookies();
//        	// 향샹된 for문으로 쿠키를 찾는다
//        	for(Cookie c : cookies) {
//
//        		// accessToken이 key에 있다면 value에서 값을 꺼낸다.
//        		if("accessToken".equals(c.getName())) {
//        			String token = c.getValue();
//        			id = jwt.getLoginId(token);
//                    String name = jwt.getUsername(token);
//                    System.out.println("name = " + token);
//                    System.out.println("name = " + id);
//                    System.out.println("name = " + name);
//        		}
//        	}
            String id = jwt.getLoginId(token);
            AdminDto dto = serv.selectMyPage(id);

            if( dto ==  null ) {
                return "redirect:/adminLogin?error=true";
            }
            model.addAttribute("admin", dto);
            return "admin/adminMyPage";
        }

        @GetMapping("/admin/headMyPage")
        public String headPage(HttpServletRequest request, Model model, @CookieValue(value = "accessToken") String token){
            String id = jwt.getLoginId(token);
            AdminDto dto = serv.selectMyPage(id);

            if( dto ==  null ) {
                return "redirect:/adminLogin?error=true";
            }
            model.addAttribute("admin", dto);
            return "admin/headMyPage";
        }

        @GetMapping("/admin/executiveMyPage")
        public String executivePage(HttpServletRequest request, Model model, @CookieValue(value = "accessToken") String token){
            String id = jwt.getLoginId(token);
            AdminDto dto = serv.selectMyPage(id);

            if( dto ==  null ) {
                return "redirect:/adminLogin?error=true";
            }
            model.addAttribute("admin", dto);
            return "admin/executiveMyPage";
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
            cookie.setHttpOnly(true);
            cookie.setSecure(true);

            response.addCookie(cookie);
            return "redirect:/adminLogin";
        }

        @GetMapping("/admin/access-denied")
        public String denied(HttpServletResponse response, @RequestParam String role) {
            if(role.equals("chief")){
                return "/admin/adminMyPage";
            }
            return "/admin/access-denied";
        }


    }

