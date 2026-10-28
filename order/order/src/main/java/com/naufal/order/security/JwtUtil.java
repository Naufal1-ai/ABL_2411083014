package com.naufal.order.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Kunci rahasia minimal 256-bit untuk algoritma HMAC-SHA256
    private static final String DEFAULT_SECRET = "ABL_SUPER_SECRET_KEY_SEMESTER_5_NAUFAL_2026_JWT_TOKEN_123456";
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000L; // 24 jam dalam milidetik

    private final SecretKey key;

    public JwtUtil(@Value("${app.jwt.secret:" + DEFAULT_SECRET + "}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Menghasilkan token JWT dengan payload username, nama, dan role
     */
    public String generateToken(String username, String nama, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(username)
                .claim("nama", nama)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * Membaca klaim (payload) dari token JWT
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Mengambil username dari token JWT
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Mengambil role dari token JWT
     */
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    /**
     * Validasi apakah token JWT valid dan belum kedaluwarsa
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
