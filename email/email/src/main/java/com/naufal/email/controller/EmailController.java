package com.naufal.email.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naufal.email.entity.EmailLog;
import com.naufal.email.service.EmailService;

/**
 * EmailController = pintu REST untuk MELIHAT riwayat email.
 *
 * Endpoint:
 * - GET /api/email       -> semua riwayat email
 * - GET /api/email/{id}  -> satu riwayat email
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping
    public List<EmailLog> getAllEmail() {
        return emailService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailLog> getEmailById(@PathVariable Long id) {
        EmailLog log = emailService.getById(id);
        if (log != null) {
            return ResponseEntity.ok(log);
        }
        return ResponseEntity.notFound().build();
    }
}
