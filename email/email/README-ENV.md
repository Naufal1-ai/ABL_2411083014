# Cara Mengatur Environment Variable (Email Service)

File rahasia (Gmail & App Password) **tidak disimpan di kode**. Nilainya ditaruh
di file `.env` yang **tidak akan ikut ter-push ke GitHub**.

## Langkah Setup

1. **Copy file template** menjadi `.env`:

   ```
   copy .env.example .env
   ```
   (Linux/Mac: `cp .env.example .env`)

2. **Buka `.env`** lalu isi nilai asli:

   ```
   GMAIL_USERNAME=emailasli@gmail.com
   GMAIL_APP_PASSWORD=abcdefghijklmnop
   EMAIL_PENERIMA=emailtujuan@gmail.com
   ```

   - `GMAIL_APP_PASSWORD` = App Password 16 karakter dari
     https://myaccount.google.com/apppasswords
     (bukan password login Gmail biasa!)

3. **Jalankan aplikasi seperti biasa** — file `.env` akan otomatis dibaca.

## Istilah di application.yaml

| Di application.yaml | Diambil dari .env |
|---|---|
| `${GMAIL_USERNAME}` | `GMAIL_USERNAME` |
| `${GMAIL_APP_PASSWORD}` | `GMAIL_APP_PASSWORD` |
| `${EMAIL_PENERIMA}` | `EMAIL_PENERIMA` |
| `${RABBITMQ_HOST:localhost}` | `RABBITMQ_HOST` (default: localhost) |

## Kenapa Aman?

- `.env` sudah masuk `.gitignore` -> **tidak akan ke-push**
- `.env.example` **boleh** di-push (tidak berisi rahasia, hanya template)
- Kode & konfigurasi tetap aman dibagikan ke GitHub

## Hal Penting

- **JANGAN pernah** menulis Gmail/App Password langsung di `application.yaml`.
- Kalau `.env` tidak sengaja ke-commit, segera **ganti App Password** di akun Google Anda.
