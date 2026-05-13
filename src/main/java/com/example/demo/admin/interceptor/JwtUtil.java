//package com.example.demo.admin.interceptor;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.UUID;
//
//@Component
//public class JwtUtil {
//
//    private final String secret = "my-secret-key-my-secret-key-my-secret-key";
//    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());
//
//    // =========================
//    // ACCESS TOKEN (30분)
//    // =========================
//    public String createAccessToken(Long adminId) {
//
//        return Jwts.builder()
//                .setSubject(String.valueOf(adminId))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // =========================
//    // REFRESH TOKEN (7일)
//    // =========================
//    public String createRefreshToken(Long adminId) {
//
//        return Jwts.builder()
//                .setSubject(String.valueOf(adminId))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
//                .claim("type", "refresh")
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // =========================
//    // 검증
//    // =========================
//    public boolean validate(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // =========================
//    // ID 추출
//    // =========================
//    public Long getAdminId(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return Long.parseLong(claims.getSubject());
//    }
//
//    // =========================
//    // Refresh인지 체크
//    // =========================
//    public boolean isRefreshToken(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return "refresh".equals(claims.get("type"));
//    }
//}