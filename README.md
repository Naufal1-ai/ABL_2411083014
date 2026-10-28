# ABL_2411083014

Repositori Praktikum & Proyek **Arsitektur Berorientasi Layanan (ABL)**  
**Nama**: Naufal Khalil Aldeza  
**NIM**: 2411083014  
**Semester**: 5  

---

## 🏗️ Arsitektur Sistem (Microservices)

Sistem ini dibangun dengan arsitektur Microservices berbasis **Spring Boot**, **Spring Cloud**, **RabbitMQ**, dan **H2 Database**.

### Daftar Layanan:
1. **`eureka`** (Port: `8761`)
   - Service Discovery & Registry menggunakan Spring Cloud Netflix Eureka Server.
2. **`gateway`** (Port: `9001`)
   - API Gateway menggunakan Spring Cloud Gateway sebagai reverse proxy dan entry point seluruh request klien.
3. **`pelanggan`** (Port: `8082`)
   - Microservice manajemen data pelanggan (Customer Service).
4. **`produk`** (Port: `8081`)
   - Microservice manajemen data produk / katalog produk (Product Catalog Service).
5. **`order`** (Port: `8083`)
   - Microservice pemrosesan pesanan (Order Service) dengan pola **CQRS (Command Query Responsibility Segregation)** dan proteksi autentikasi **JWT (JSON Web Token)**.
6. **`produser`** (Port: `8085`)
   - Service produser pesan ke Message Broker RabbitMQ (`myQueue`).
7. **`consumer`**
   - Service background listener untuk menerima pesan dari RabbitMQ (`myQueue`).
8. **`email`** (Port: `8084`)
   - Service notifikasi email yang mendengarkan event dari RabbitMQ (`order.notification.queue`) dan mengirim email via Gmail SMTP.
9. **`docs`**
   - Dokumentasi lengkap alur sistem, diagram alur (Mermaid), dan panduan autentikasi JWT.
