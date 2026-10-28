package com.naufal.produk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naufal.produk.entity.JenisBarang;

@Repository
public interface JenisBarangRepository extends JpaRepository<JenisBarang, Long> {

}
