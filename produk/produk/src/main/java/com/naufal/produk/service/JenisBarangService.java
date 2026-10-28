package com.naufal.produk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naufal.produk.entity.JenisBarang;
import com.naufal.produk.repository.JenisBarangRepository;

@Service
public class JenisBarangService {

    @Autowired
    private JenisBarangRepository jenisBarangRepository;

    public List<JenisBarang> getAllJenisBarang() {
        return jenisBarangRepository.findAll();
    }

    public JenisBarang getJenisBarangById(Long id) {
        return jenisBarangRepository.findById(id).orElse(null);
    }

    public JenisBarang saveJenisBarang(JenisBarang jenisBarang) {
        return jenisBarangRepository.save(jenisBarang);
    }

    public void deleteJenisBarang(Long id) {
        jenisBarangRepository.deleteById(id);
    }

    public JenisBarang updateJenisBarang(Long id, JenisBarang jenisBarang) {
        JenisBarang existing = jenisBarangRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setNamaJenis(jenisBarang.getNamaJenis());
            existing.setDeskripsi(jenisBarang.getDeskripsi());
            return jenisBarangRepository.save(existing);
        }
        return null;
    }
}
