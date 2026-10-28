package com.naufal.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test untuk memastikan Spring context berhasil load.
 * 
 * Properties test:
 * - eureka.client.enabled=false → menonaktifkan Eureka agar test bisa berjalan
 *   tanpa Eureka server aktif
 * - spring.datasource.url → menggunakan H2 in-memory (bukan file) agar test
 *   tidak bergantung pada file database lokal
 */
@SpringBootTest(properties = {
    "eureka.client.enabled=false",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=password",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class OrderApplicationTests {

    @Test
    void contextLoads() {
        // Test ini memastikan seluruh Spring Application Context berhasil diinisialisasi.
        // Jika ada bean yang salah konfigurasi, test ini akan gagal.
    }

}
