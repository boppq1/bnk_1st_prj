package com.example.demo.interceptor;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.admin.controller.BlacklistController;
import com.example.demo.admin.service.ForbiddenWordService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter{
	
	@Autowired
	private ProxyManager<String> proxyManager;
	
	@Autowired
	private StringRedisTemplate rt; // 벌점 및 차단 관리 위한 Redis 도구 주입
	
	@Autowired
	private ForbiddenWordService ws; // 금지어 판단 서비스 주입
	
	@Autowired
	private BlacklistController black;
	
	// 1분에 30개 토큰 채우고, 최대 30개까지 보관하는 버킷 규칙 정의
	private final BucketConfiguration bc = BucketConfiguration.builder()
			.addLimit(Bandwidth.builder()
					.capacity(20)
					.refillIntervally(20, Duration.ofMinutes(1))
					.build())
			.build();
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		
		if(uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images") || uri.startsWith("/.well-known") || uri.startsWith("/api") || uri.startsWith("/error") || uri.equals("/favicon.ico")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String clientKey = (String) request.getAttribute("clientKey");
		
		// 이미 영구/임시 벤 목록에 등록된 IP인지 Redis에서 확인
		if(Boolean.TRUE.equals(rt.hasKey("blacklist:" + clientKey))) {
			System.out.println("블랙리스트");
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Blacklisted");
		    
			return;
		}
		
		// 단기간 대량 요청 체크 (0.2초 이내)
		long currentTime = System.currentTimeMillis(); // 현재 컴퓨터 시간
		String lastRequestKey = "last-request" + clientKey;
		String lastTimeStr = rt.opsForValue().get(lastRequestKey); // Redis에서 해당 유저의 마지막 요청 시간을 꺼냄
		
		if(lastTimeStr != null) {
			long lastTime = Long.parseLong(lastTimeStr);
			if(currentTime - lastTime < 200) { // 0.2초 미만 간격으로 요청 했다면
				
				applyFastRequestPenalty(clientKey, 1);
				
			}
		}
		
		// 현재 요청 시간을 Redis에 20초 동안만 임시 기록 (연타 방지)
		// 왜 20초만? -> 20초 이상 가만히 있었다면 매크로가 아님. TTL을 주지 않는다면 레디스에 데이터가 영구히 쌓여 메모리가 꽉 참
		rt.opsForValue().set(lastRequestKey, String.valueOf(currentTime), Duration.ofSeconds(20));
		
		// Bucket4j 기반 전체 빈도 제한 체크 (1분 20회 제한)
		// Redis 기반의 버킷을 가져오거나(clientKey) 없으면 새로 생성(람다)
		Bucket bucket = proxyManager.builder().build(clientKey, (java.util.function.Supplier<BucketConfiguration>) () -> bc);
		
		// 토큰 하나 소비 시도 (요청 가능 여부 체크)
		if(bucket.tryConsume(1)) {
		} else {
			// 한도 초과 -> 429 Too Many Requests 응답 처리
			response.setStatus(429);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("Too many Requests.");
			return;
		}
		
		// 차단된 검색어 테이블(캐시)와 비교 검사
		// 쿼리 파라미터 중 검색어 키워드
		String searchKeyword = request.getParameter("q");
		if(searchKeyword != null && ws.isForbidden(searchKeyword)) {
			applyBanKeywordPenalty(clientKey, 3);
			return;
		}
		// 다음 필터로 이동
		filterChain.doFilter(request, response);
	}
	
	
	// 단기간 대량 요청
	private void applyFastRequestPenalty (String clientKey, int score) {
		String penaltyKey = "penalty:" + clientKey;
		String reason = "단기간 대량 요청";
		// Redis의 INCR 명령어로 벌점 누적
		Long currentScore = rt.opsForValue().increment(penaltyKey, score);
		
		if(currentScore != null && currentScore >= 10) { // 누적 벌점이 10점 이상이면
			// 24시간동안 blacklist:IP 키 생성 (TTL 1일)
//			rt.opsForValue().set("blacklist:" + clientKey, "BANNED_USER", Duration.ofDays(1));
			rt.opsForValue().set("blacklist:" + clientKey, "BANNED_USER", Duration.ofMinutes(1));
			rt.delete(penaltyKey); // 벌점 카운터 초기화
			black.insertBlacklist(clientKey, reason);
			
			System.out.println("블랙리스트 등록 완료 : " + clientKey);
		} else {
			// 벌점이 아직 10점 미만이면 해당 벌점 데이터의 TTL을 30분으로 세팅/유지
			rt.expire(penaltyKey, Duration.ofMinutes(1));
		}
	}
	
	
	// 차단된 검색어 검색
	private void applyBanKeywordPenalty(String clientKey, int score) {
		String penaltyKey = "penalty:" + clientKey;
		String reason = "차단된 검색어 검색 시도";
		// Redis의 INCR 명령어로 벌점 누적
		Long currentScore = rt.opsForValue().increment(penaltyKey, score);
		
		if(currentScore != null && currentScore >= 10) { // 누적 벌점이 10점 이상이면
			// 24시간동안 blacklist:IP 키 생성 (TTL 1일)
//			rt.opsForValue().set("blacklist:" + clientKey, "BANNED_USER", Duration.ofDays(1));
			rt.opsForValue().set("blacklist:" + clientKey, "BANNED_USER", Duration.ofMinutes(1));
			rt.delete(penaltyKey); // 벌점 카운터 초기화
			black.insertBlacklist(clientKey, reason);
			
			System.out.println("블랙리스트 등록 완료 : " + clientKey);
		} else {
			// 벌점이 아직 10점 미만이면 해당 벌점 데이터의 TTL을 30분으로 세팅/유지
			rt.expire(penaltyKey, Duration.ofMinutes(1));
		}
	}
		
}



