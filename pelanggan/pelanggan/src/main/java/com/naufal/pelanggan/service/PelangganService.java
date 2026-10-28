package com.naufal.pelanggan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naufal.pelanggan.entity.Pelanggan;
import com.naufal.pelanggan.repository.PelangganRepository;

@Service
public class PelangganService {

    @Autowired
    private PelangganRepository pelangganRepository;

    public List<Pelanggan> getAllPelanggan() {
        return pelangganRepository.findAll();
    }

    public Pelanggan getPelangganById(Long id) {
        return pelangganRepository.findById(id).orElse(null);
    }

    public Pelanggan savePelanggan(Pelanggan pelanggan) {
        return pelangganRepository.save(pelanggan);
    }

    public Pelanggan updatePelanggan(Long id, Pelanggan pelanggan) {
        Pelanggan existing = pelangganRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setNama(pelanggan.getNama());
            existing.setAlamat(pelanggan.getAlamat());
            existing.setJenisKelamin(pelanggan.getJenisKelamin());
            return pelangganRepository.save(existing);
        }
        return null;
    }

    public void deletePelanggan(Long id) {
        pelangganRepository.deleteById(id);
    }
}
