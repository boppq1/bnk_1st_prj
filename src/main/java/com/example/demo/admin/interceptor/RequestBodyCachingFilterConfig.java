//package com.example.demo.admin.interceptor;
//
//import com.example.demo.admin.dao.IAdminLogDao;
//import com.example.demo.admin.dto.AdminLogDto;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class RequestBodyCachingFilterConfig extends OncePerRequestFilter {
//
//    @Autowired
//    private IAdminLogDao adminLogDao;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//// ✅ 두 번째 인자 추가 (캐시 크기, 바이트)
//        ContentCachingRequestWrapper wrapped =
//                new ContentCachingRequestWrapper(request, 10240);
//
//        long startTime = System.currentTimeMillis();
//
//        filterChain.doFilter(wrapped, response);  // 여기서 컨트롤러까지 실행 완료
//
//        // ✅ 이 시점에는 body가 이미 읽혔으므로 버퍼에 데이터가 있음
//        byte[] bytes = wrapped.getContentAsByteArray();
//        String body = (bytes != null && bytes.length > 0)
//                ? new String(bytes, StandardCharsets.UTF_8)
//                : "";
//
//
//        AdminLogDto log = new AdminLogDto();
//        log.setUrl(request.getRequestURI());
//        log.setMethod(request.getMethod());
//        log.setElapsed_ms(System.currentTimeMillis() - startTime);
//        log.setResponse_code(response.getStatus());
//        log.setRequest_body(body);
//
//        adminLogDao.insertLog(log);
//    }
//}