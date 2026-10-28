# 🚀 Panduan Git, Push, dan Context Handoff untuk Agent Berikutnya

Dokumen ini ditujukan untuk **developer, dosen, dan AI Coding Agent** yang akan melanjutkan pengembangan atau melakukan operasi Git (commit, push, review) pada repositori **ABL (Arsitektur Berorientasi Layanan)**.

---

## 🔒 1. Aturan Keamanan Sebelum Melakukan Push (Security Checklist)

Sebelum menjalankan perintah `git push`, selalu lakukan verifikasi keamanan berikut:

1. **Pastikan File `.env` Tidak Ter-track Git:**
   - Repositori ini menggunakan `.env` untuk menyimpan `GMAIL_APP_PASSWORD` dan konfigurasi RabbitMQ.
   - File `.env` sudah masuk ke `.gitignore`.
   - Jalankan perintah berikut untuk memastikan tidak ada credential yang lolos:
     ```powershell
     git status
     ```
   - Pastikan file `.env` **TIDAK PERNAH** muncul di daftar *Changes to be committed* atau *Untracked files*.
2. **Template Konfigurasi:**
   - Selalu gunakan file `.env.example` sebagai referensi aman tanpa mencantumkan password asli.
3. **Database Lokal H2:**
   - Folder `data/` dan file-file `*.mv.db` / `*.trace.db` sudah diabaikan via `.gitignore` agar database lokal tidak mengotori repositori Git.

---

## 🛠️ 2. Langkah-Langkah Git Commit & Push yang Benar

Jika Anda telah melakukan perubahan kode, ikuti langkah standar berikut:

```powershell
# 1. Periksa status file yang berubah
git status

# 2. Tambahkan perubahan kode (hindari menambahkan file sensitif)
git add .

# 3. Buat commit dengan pesan yang deskriptif dan profesional
git commit -m "feat: update documentation, README, and email service configuration"

# 4. Tarik update terbaru dari remote repository (mencegah conflict)
git pull origin main --rebase

# 5. Push perubahan ke remote repository
git push origin main
```

---

## 🧠 3. Handoff & Konteks Teknis untuk Agent / Developer Berikutnya

Bagi AI Agent atau developer yang melanjutkan proyek ini, berikut konteks teknis krusial yang harus dipahami:

### A. Spesifikasi Lingkungan
- **JDK:** Java 25 (Adoptium Hotspot)
- **Framework:** Spring Boot 4.1.1 & Spring Cloud 2025.1.3
- **Message Broker:** RabbitMQ (Running via Docker di port `5672` dan UI di `15672`)
- **Database:** In-memory / File-based H2 Database per microservice
- **Registry & Gateway:** Eureka Server (`8761`) & Spring Cloud Gateway MVC (`9001`)

### B. Nuansa Konfigurasi Spring Cloud Gateway WebMVC
- Proyek ini menggunakan **Spring Cloud Gateway MVC** (berbasis Servlet/Tomcat biasa), **bukan** WebFlux (Netty).
- Namespace konfigurasi routes di `application.yaml` adalah:
  ```yaml
  spring:
    cloud:
      gateway:
        mvc:
          routes:   # WAJIB "mvc", BUKAN "webmvc" dan BUKAN "routes" langsung
  ```

### C. Pola CQRS pada Order Service (`com.naufal.order`)
- **Command Side (Write):**
  - Controller: `OrderCommandController` (`POST /api/order`, `PUT /api/order/{id}`, `DELETE /api/order/{id}`)
  - Service: `OrderCommandService`
  - Diproteksi menggunakan **JWT Authentication**.
  - Setiap order baru yang dibuat akan mengirimkan event `OrderEvent` ke queue `order.notification.queue` di RabbitMQ.
- **Query Side (Read):**
  - Controller: `OrderQueryController` (`GET /api/order`, `GET /api/order/{id}`)
  - Service: `OrderQueryService`
  - Melakukan lookup data produk ke microservice `PRODUK` secara inter-service via Eureka `DiscoveryClient` dan `RestTemplate`.

### D. Notifikasi Email Service (`com.naufal.email`)
- Port: `8084`
- Listener: `OrderEventListener` mendengarkan `order.notification.queue`.
- Menggunakan `JavaMailSender` (Gmail SMTP port `587` dengan TLS).
- Riwayat notifikasi disimpan di database H2 lokal dan dapat dilihat melalui `GET http://localhost:9001/api/email`.

---

## 📋 4. Urutan Menjalankan Seluruh Microservice

Jalankan service secara bertahap dengan urutan berikut:

1. **Docker RabbitMQ:**
   ```powershell
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
   ```
2. **Eureka Server** (`eureka/` - Port `8761`)
3. **Produk Service** (`produk/produk/` - Port `8081`)
4. **Pelanggan Service** (`pelanggan/pelanggan/` - Port `8082`)
5. **Order Service** (`order/order/` - Port `8083`)
6. **Email Service** (`email/email/` - Port `8084`)
7. **Producer & Consumer Service** (`produser/` - Port `8085`)
8. **API Gateway** (`gateway/gateway/` - Port `9001`)

