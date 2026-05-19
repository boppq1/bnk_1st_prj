package com.example.demo.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final SecretKey secretKey;
	private final long expiration;
	
	public JwtUtil(@Value("${jwt.secret}") String secret,
					@Value("${jwt.expiration}")long expiration) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expiration = expiration;
	}
	
	// 토큰 생성
	public String generateToken(String username, Map<String, Object> info) {
	    return Jwts.builder()
	               .claims(info)           // ✅ claims() 먼저
	               .subject(username)      // ✅ subject()는 반드시 그 다음에
	               .issuedAt(new Date())
	               .expiration(new Date(System.currentTimeMillis() + expiration))
	               .signWith(secretKey)
	               .compact();
	}

	//토큰 파싱 토큰을 푸는거
	private Claims parseToken(String token) {
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
	}
	
	//유효성 검사
	public boolean isValid(String token){
		try {
			parseToken(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
	
	//Clams에서 username 꺼내기
	public String getUsername(String token) {
		return parseToken(token).getSubject();
	}
	
	//Claims에서 role 꺼내기 
	public String getRole(String token) {
		return parseToken(token).get("role",String.class);			
	}
	
	public String getCompanyName(String token) {
		return parseToken(token).get("company_nm", String.class);
	}
}
