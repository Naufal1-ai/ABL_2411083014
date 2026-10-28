```mermaid

flowchart TD
    Client(["Client / Postman / Browser"])

    subgraph Registry ["Service Discovery"]
        Eureka["Eureka Server\n(Port: 8761)"]
    end

    subgraph Entrypoint ["API Gateway"]
        Gateway["API Gateway\n(Port: 9001)"]
    end

    subgraph Services ["Core Microservices"]
        Produk["Produk Service\n(Port: 8081)\n[H2 DB: data/produk]"]
        Pelanggan["Pelanggan Service\n(Port: 8082)\n[H2 DB: data/pelanggan]"]
        
        subgraph OrderCQRS ["Order Service (Port: 8083)"]
            CommandSide["Command Controller / Service\n(POST, PUT, DELETE)"]
            QuerySide["Query Controller / Service\n(GET All, GET by ID)"]
            OrderDB[("H2 DB: data/order")]
        end
    end

    subgraph MessageBroker ["Message Broker (Port: 5672)"]
        RabbitMQ{{"RabbitMQ"}}
        OrderQueue["Queue: order.notification.queue"]
        MyQueue["Queue: myQueue"]
    end

    subgraph Notification ["Notifikasi & Praktikum RabbitMQ"]
        EmailService["Email Service\n(Port: 8084)\n[H2 DB: data/email]"]
        Gmail[("Gmail SMTP Server")]
        
        Produser["Produser Service\n(Port: 8085)"]
        Consumer["Consumer Service\n(Background Listener)"]
    end

    %% Registrasi ke Eureka
    Gateway -.->|Daftar / Lookup| Eureka
    Produk -.->|Daftar| Eureka
    Pelanggan -.->|Daftar| Eureka
    OrderCQRS -.->|Daftar & Discovery| Eureka
    EmailService -.->|Daftar| Eureka

    %% Routing dari Gateway
    Client -->|Request ke Port 9001| Gateway
    Gateway -->|/api/produk/**| Produk
    Gateway -->|/api/pelanggan/**| Pelanggan
    Gateway -->|/api/order/**| OrderCQRS
    Gateway -->|/api/email/**| EmailService

    %% CQRS Interaksi
    CommandSide -->|Tulis Data| OrderDB
    CommandSide -->|Kirim OrderEvent| OrderQueue
    QuerySide -->|Baca Data| OrderDB
    QuerySide -.->|HTTP Lookup Produk via Eureka| Produk

    %% Notifikasi Email via RabbitMQ
    OrderQueue -->|Consume Event| EmailService
    EmailService -->|Kirim Email Asli| Gmail

    %% Modul Latihan RabbitMQ
    Produser -->|Kirim Pesan Teks| MyQueue
    MyQueue -->|Terima Pesan| Consumer
```

---

## Alur Autentikasi (JWT) pada Order Service

### 1. Registrasi Akun
* **Endpoint:** `POST /api/order/auth/register` (atau via Gateway: `http://localhost:9001/api/order/auth/register`)
* **Payload:**
  ```json
  {
    "username": "naufal",
    "password": "rahasia123",
    "nama": "Naufal",
    "role": "USER"
  }
  ```
* **Respons:** HTTP `201 Created` berisi data akun dan token JWT.

---

### 2. Login & Penerbitan Token
* **Endpoint:** `POST /api/order/auth/login` (atau via Gateway: `http://localhost:9001/api/order/auth/login`)
* **Payload:**
  ```json
  {
    "username": "naufal",
    "password": "rahasia123"
  }
  ```
* **Respons:** HTTP `200 OK` berisi token JWT:
  ```json
  {
    "token": "eyJhbGciOiJIUzM4NCJ9...",
    "type": "Bearer",
    "username": "naufal",
    "nama": "Naufal",
    "role": "USER",
    "message": "Login berhasil!"
  }
  ```

---

### 3. Akses Terproteksi (Command CQRS)
Untuk membuat order baru (`POST /api/order`), mengupdate (`PUT`), atau menghapus (`DELETE`), sertakan token di header HTTP:
* **Header:** `Authorization: Bearer <token_jwt>`
* Jika token tidak disertakan atau kedaluwarsa, sistem otomatis menolak dengan kode **`401 Unauthorized`**.
