package com.example.demo.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.admin.dao.IAdminLogDao;
import com.example.demo.admin.dto.AdminActionLogDto;
import com.example.demo.admin.dto.AdminDto;
import com.example.demo.admin.service.AdminMergeService;
import com.example.demo.jwt.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminLogAspect {
	
	private final IAdminLogDao dao;
	private final JwtUtil jwt;
	private final AdminMergeService mergeServ;
	
	@AfterReturning("@annotation(adminLog)")
	public void saveAdminLog(JoinPoint joinPoint, AdminLog adminLog) {
		System.out.println("로그 기록하쟈잉");
		
		String token = null;
		
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    if (attributes != null) {
	        HttpServletRequest request = attributes.getRequest();
	        
			Cookie[] cookies = request.getCookies();
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if ("accessToken".equals(cookie.getName())) {
	                    token = cookie.getValue();
	                    break;
	                }
	            }
	        }
        
	    }
		
	    if (token != null) {
	        String id = jwt.getLoginId(token);
	        AdminDto dtoo = mergeServ.selectMyPage(id);
	        
	        AdminActionLogDto dto = new AdminActionLogDto();
	        dto.setAction(adminLog.action());
	        dto.setTarget(Integer.toString(dtoo.getAdmin_id().intValue()));
	        
	        dao.insertAdminLog(dto);
	    } else {
	        System.out.println("현재 요청에서 accessToken을 찾을 수 없습니다.");
	    }
	}
}
