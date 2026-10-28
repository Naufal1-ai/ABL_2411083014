package com.naufal.produk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Produk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nama;
    private String deskripsi;
    private double harga;
    private Long jenisBarangId; // Relasi manual simpan ID saja tanpa JPA @ManyToOne

    public Produk() {
    }

    public Produk(Long id, String nama, String deskripsi, double harga, Long jenisBarangId) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.jenisBarangId = jenisBarangId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public Long getJenisBarangId() {
        return jenisBarangId;
    }

    public void setJenisBarangId(Long jenisBarangId) {
        this.jenisBarangId = jenisBarangId;
    }
}