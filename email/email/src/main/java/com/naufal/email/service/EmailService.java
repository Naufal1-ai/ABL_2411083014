package com.naufal.email.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.naufal.email.entity.EmailLog;
import com.naufal.email.event.OrderEvent;
import com.naufal.email.repository.EmailLogRepository;

/**
 * EmailService = otak dari Email Service.
 *
 * Tugasnya:
 * 1. Menerima event order
 * 2. Menyusun isi email
 * 3. MENGIRIM email sungguhan via JavaMailSender (Gmail SMTP)
 * 4. Menyimpan riwayat ke database
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailLogRepository emailLogRepository;

    /**
     * Alamat email PENGIRIM (akun Gmail Anda).
     * Diambil dari application.yaml -> app.email.pengirim
     */
    @Value("${app.email.pengirim}")
    private String pengirim;

    /**
     * Alamat email PENERIMA.
     * Diambil dari application.yaml -> app.email.penerima
     * (karena email pelanggan belum ada, kita pakai email tetap dulu)
     */
    @Value("${app.email.penerima}")
    private String penerima;

    /**
     * Dipanggil setiap kali ada event order baru dari RabbitMQ.
     */
    public void kirimEmailOrder(OrderEvent event) {
        String subjek = "Konfirmasi Pesanan #" + event.getOrderId();
        String isi = "Terima kasih telah memesan!\n\n"
                + "Detail pesanan Anda:\n"
                + "- Order ID   : " + event.getOrderId() + "\n"
                + "- Produk ID  : " + event.getProdukId() + "\n"
                + "- Jumlah     : " + event.getJumlah() + "\n"
                + "- Total      : Rp " + event.getTotal() + "\n\n"
                + "Pesanan Anda sedang kami proses.";

        String status = "GAGAL";

        // --- Proses pengiriman email ---
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(pengirim);
            message.setTo(penerima);
            message.setSubject(subjek);
            message.setText(isi);

            mailSender.send(message);
            status = "SUKSES";

            System.out.println();
            System.out.println("########################################");
            System.out.println("#            EMAIL SERVICE             #");
            System.out.println("########################################");
            System.out.println("Email BERHASIL dikirim ke : " + penerima);
            System.out.println("Subjek                    : " + subjek);
            System.out.println("########################################");

        } catch (Exception e) {
            // Jika gagal kirim (mis. password salah / internet mati),
            // aplikasi TIDAK crash. Kita catat saja sebagai GAGAL.
            System.out.println();
            System.out.println("!!! GAGAL mengirim email: " + e.getMessage());
            e.printStackTrace();
        }

        // --- Simpan riwayat ke database ---
        EmailLog log = new EmailLog();
        log.setOrderId(event.getOrderId());
        log.setPelangganId(event.getPelangganId());
        log.setPenerima(penerima);
        log.setSubjek(subjek);
        log.setIsi(isi);
        log.setWaktuKirim(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.setStatus(status);
        emailLogRepository.save(log);
    }

    /**
     * Mengambil seluruh riwayat email.
     */
    public List<EmailLog> getAll() {
        return emailLogRepository.findAll();
    }

    /**
     * Mengambil satu riwayat email berdasarkan ID.
     */
    public EmailLog getById(Long id) {
        return emailLogRepository.findById(id).orElse(null);
    }
}
