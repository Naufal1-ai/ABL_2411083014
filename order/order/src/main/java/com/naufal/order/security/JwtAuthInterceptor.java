package com.naufal.order.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 1. Lewatkan request OPTIONS (preflight CORS)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // 2. Lewatkan endpoint publik registrasi & login
        if (uri.contains("/api/order/auth/") || uri.contains("/h2-console")) {
            return true;
        }

        // 3. Khusus operasi Command CQRS (POST, PUT, DELETE) pada /api/order: wajib autentikasi JWT
        // (Operasi GET pembacaan data tetap dibuka agar query katalog/riwayat tetap fleksibel)
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                sendUnauthorizedError(response, "Akses ditolak: Token JWT wajib disertakan pada header 'Authorization: Bearer <token>'");
                return false;
            }

            String token = authHeader.substring(7);

            if (!jwtUtil.validateToken(token)) {
                sendUnauthorizedError(response, "Akses ditolak: Token JWT tidak valid atau sudah kedaluwarsa");
                return false;
            }

            // Simpan info user ke request attribute agar bisa diakses controller jika diperlukan
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);
            request.setAttribute("authenticatedUser", username);
            request.setAttribute("authenticatedRole", role);
        }

        return true;
    }

    private void sendUnauthorizedError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = String.format("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"%s\"}", message);
        response.getWriter().write(json);
    }
}
