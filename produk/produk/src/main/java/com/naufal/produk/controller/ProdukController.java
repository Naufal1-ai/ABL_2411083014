package com.naufal.produk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naufal.produk.entity.JenisBarang;
import com.naufal.produk.entity.Produk;
import com.naufal.produk.service.JenisBarangService;
import com.naufal.produk.service.ProdukService;

@RestController
@RequestMapping("/api/produk")
public class ProdukController {

    @Autowired
    private ProdukService produkService;

    @Autowired
    private JenisBarangService jenisBarangService;

    // --- ENDPOINTS PRODUK ---

    @GetMapping
    public List<Produk> getAllProduk() {
        return produkService.getAllProduk();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produk> getProdukById(@PathVariable Long id) {
        Produk produk = produkService.getProdukById(id);
        if (produk != null) {
            return ResponseEntity.ok(produk);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Produk> createProduk(@RequestBody Produk produk) {
        Produk savedProduk = produkService.saveProduk(produk);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduk);
    }

    @PostMapping("/jenis/{jenisId}")
    public ResponseEntity<Produk> createProdukWithJenisId(@PathVariable Long jenisId, @RequestBody Produk produk) {
        produk.setJenisBarangId(jenisId);
        Produk savedProduk = produkService.saveProduk(produk);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduk);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produk> updateProduk(@PathVariable Long id, @RequestBody Produk produk) {
        Produk updated = produkService.updateProduk(id, produk);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduk(@PathVariable Long id) {
        produkService.deleteProduk(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS JENIS BARANG ---

    @GetMapping("/jenis-barang")
    public List<JenisBarang> getAllJenisBarang() {
        return jenisBarangService.getAllJenisBarang();
    }

    @GetMapping("/jenis-barang/{id}")
    public ResponseEntity<JenisBarang> getJenisBarangById(@PathVariable Long id) {
        JenisBarang jenisBarang = jenisBarangService.getJenisBarangById(id);
        if (jenisBarang != null) {
            return ResponseEntity.ok(jenisBarang);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/jenis-barang")
    public ResponseEntity<JenisBarang> createJenisBarang(@RequestBody JenisBarang jenisBarang) {
        JenisBarang savedJenisBarang = jenisBarangService.saveJenisBarang(jenisBarang);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJenisBarang);
    }

    @PutMapping("/jenis-barang/{id}")
    public ResponseEntity<JenisBarang> updateJenisBarang(@PathVariable Long id, @RequestBody JenisBarang jenisBarang) {
        JenisBarang updated = jenisBarangService.updateJenisBarang(id, jenisBarang);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/jenis-barang/{id}")
    public ResponseEntity<Void> deleteJenisBarang(@PathVariable Long id) {
        jenisBarangService.deleteJenisBarang(id);
        return ResponseEntity.noContent().build();
    }
}