package com.naufal.produk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naufal.produk.entity.Produk;
import com.naufal.produk.repository.ProdukRepository;

@Service
public class ProdukService {

    @Autowired
    private ProdukRepository produkRepository;

    public List<Produk> getAllProduk() {
        return produkRepository.findAll();
    }

    public Produk getProdukById(Long id) {
        return produkRepository.findById(id).orElse(null);
    }

    public Produk saveProduk(Produk produk) {
        return produkRepository.save(produk);
    }

    public void deleteProduk(Long id) {
        produkRepository.deleteById(id);
    }

    public Produk updateProduk(Long id, Produk produk) {
        Produk existing = produkRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setNama(produk.getNama());
            existing.setDeskripsi(produk.getDeskripsi());
            existing.setHarga(produk.getHarga());
            existing.setJenisBarangId(produk.getJenisBarangId());
            return produkRepository.save(existing);
        }
        return null;
    }
}