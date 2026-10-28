package com.naufal.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naufal.email.entity.EmailLog;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

}
