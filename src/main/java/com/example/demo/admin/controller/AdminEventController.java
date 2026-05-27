package com.example.demo.admin.controller;

import com.example.demo.admin.dto.AdminEventDto;
import com.example.demo.admin.service.AdminMergeService;
import com.example.demo.interceptor.AdminLog;
import org.springframework.ui.Model;
import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.service.AdminEventService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService serv;
    private final AdminMergeService adminServ;

    @GetMapping("/admin/adminEventPage")
    public String adminEventPage(Model model, HttpSession session) {
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        if (admin == null) return "redirect:/adminLogin";

        model.addAttribute("admin", admin);
        return "admin/adminEventPage"; // 실제 뷰 경로
    }

    @AdminLog(action="관리자 이벤트 등록")
    @PostMapping("/admin/eventRegister")
    public String adminEventRegister(AdminEventDto dto, HttpSession session) {
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        if (admin == null) return "redirect:/adminLogin";

        // 세션에 있는 관리자 고유번호를 DTO에 세팅
        dto.setEvent_wtr_no(admin.getAdmin_id()); // DTO 필드명 확인 필요
        serv.insertEvent(dto);

        return "redirect:/admin/adminEventListPage"; // 등록 후 목록으로 이동
    }

    @GetMapping("/admin/adminEventListPage")
    public String adminEventList(Model model, HttpSession session) {
        AdminDto admin = (AdminDto) session.getAttribute("admin");
        if (admin == null) return "redirect:/adminLogin";

        List<AdminEventDto> events = serv.selectList();

        model.addAttribute("admin", admin);
        model.addAttribute("events", events);
        model.addAttribute("totalCount", serv.countEvents());
        model.addAttribute("adminCount", serv.countAdmins());
        return "admin/adminEventListPage";
    }

    @AdminLog(action="관리자 이벤트 수정")
    @PostMapping("/admin/updateEvent/{no}")
    public String eventUpdate(@PathVariable("no") Long event_no, AdminEventDto dto) {
        dto.setEvent_no(event_no);
        serv.updateEvent(dto);
        return "redirect:/admin/adminEventListPage";
    }


    @AdminLog(action="관리자 이벤트 삭제")
    @GetMapping("/admin/deleteEvent/{no}")
    public String eventDelete(@PathVariable("no") Long event_no) {
        serv.deleteEvent(event_no);
        return "redirect:/admin/adminEventListPage";
    }

}
