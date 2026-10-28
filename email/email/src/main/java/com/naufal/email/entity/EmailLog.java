package com.naufal.email.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * EmailLog = catatan setiap email yang dikirim.
 *
 * Setiap kali ada event order, Email Service mengirim email
 * lalu menyimpan buktinya di tabel ini (untuk riwayat/audit).
 */
@Entity
@Table(name = "email_log")
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long pelangganId;
    private String penerima;   // alamat email tujuan
    private String subjek;
    private String isi;
    private String waktuKirim;
    private String status;     // "SUKSES" atau "GAGAL"

    public EmailLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPelangganId() {
        return pelangganId;
    }

    public void setPelangganId(Long pelangganId) {
        this.pelangganId = pelangganId;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }

    public String getSubjek() {
        return subjek;
    }

    public void setSubjek(String subjek) {
        this.subjek = subjek;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getWaktuKirim() {
        return waktuKirim;
    }

    public void setWaktuKirim(String waktuKirim) {
        this.waktuKirim = waktuKirim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
