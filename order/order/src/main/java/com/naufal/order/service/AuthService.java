package com.naufal.order.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.naufal.order.dto.AuthResponse;
import com.naufal.order.dto.LoginRequest;
import com.naufal.order.dto.RegisterRequest;
import com.naufal.order.entity.User;
import com.naufal.order.repository.UserRepository;
import com.naufal.order.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Mendaftarkan pengguna baru dengan password terenkripsi BCrypt
     */
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' sudah terdaftar!");
        }

        String role = (request.getRole() != null && !request.getRole().isBlank()) 
                ? request.getRole().toUpperCase() 
                : "USER";

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(), encodedPassword, request.getNama(), role);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getNama(), user.getRole());

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getNama(),
                user.getRole(),
                "Registrasi berhasil!"
        );
    }

    /**
     * Memvalidasi login username & password, lalu mengembalikan token JWT
     */
    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Username atau password salah!");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Username atau password salah!");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getNama(), user.getRole());

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getNama(),
                user.getRole(),
                "Login berhasil!"
        );
    }
}
