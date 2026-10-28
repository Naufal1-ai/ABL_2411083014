package com.naufal.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * EMAIL SERVICE
 *
 * Service mandiri yang bertugas:
 * 1. Mendengarkan event order dari RabbitMQ (order.notification.queue)
 * 2. Mengirim email notifikasi sungguhan ke pelanggan (via Gmail SMTP)
 * 3. Menyimpan riwayat email ke database sendiri
 *
 * @EnableDiscoveryClient -> mendaftarkan service ini ke Eureka
 * agar terlihat di dashboard dan bisa ditemukan service lain.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}

}
