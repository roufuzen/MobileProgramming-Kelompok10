# Aplikasi Perpustakaan 📚

Project repository untuk pengembangan sistem informasi manajemen perpustakaan yang dirancang untuk mempermudah pendaftaran anggota, pengelolaan peminjaman, pengembalian buku, hingga kearsipan secara terstruktur.

## 👥 Anggota Kelompok
Berikut adalah daftar anggota pengembang projek ini:
* **Deo Ary Anggara**
* **Fatih Ahmad Hosam Abiyasa**
* **Abdurrouf Faiz Al Farisyi**
* **Ahmad Agung Maulana**
* **Farrel Irawan**

## 📺 Video Penjelasan Project
Tonton video penjelasan project kami di sini:
https://www.youtube.com/watch?v=50Qb8_K8N0g  
---

## 🚀 Fitur Utama Sistem
Aplikasi ini dilengkapi dengan beberapa modul utama, antara lain:
1. **Pendaftaran Anggota**: Proses registrasi bagi masyarakat umum agar terdata sebagai anggota resmi perpustakaan.
2. **Peminjaman & Pengembalian Buku**: Pencatatan sirkulasi buku yang dipinjam dan dikembalikan secara real-time.
3. **Histori Peminjaman**: Rekam jejak riwayat peminjaman buku untuk melacak data historis pengguna.
4. **Kearsipan**: Manajemen arsip dokumen dan data buku lama agar terdokumentasi dengan aman secara digital.

---

## 🎭 Hak Akses & Aktor Sistem (Roles)
Sistem ini memisahkan hak akses berdasarkan beberapa tingkatan pengguna berikut untuk menjaga keamanan data:

### 1. Admin Dinas (Pemilik Sistem)
* Memiliki hak akses penuh (*Superadmin*) terhadap seluruh sistem informasi perpustakaan.
* Mengawasi implementasi sistem di tingkat instansi/dinas serta melihat laporan global.

### 2. Admin & Layanan
* **Admin**: Mengelola konfigurasi sistem, data master buku, manajemen akun petugas, serta penarikan laporan operasional.
* **Layanan**: Menangani aspek bantuan teknis, keluhan pengguna, dan integrasi operasional harian.

### 3. Petugas
Aktor operasional yang bertanggung jawab langsung pada alur transaksi di lapangan, meliputi:
* **Pendaftaran**: Memproses dan memverifikasi pendaftaran anggota baru.
* **Peminjaman**: Melayani dan mencatat transaksi peminjaman buku.
* **Pengembalian**: Melayani proses pengembalian buku dan mencatat statusnya.

### 4. Umum (Masyarakat)
* Mengakses aplikasi untuk melihat katalog buku yang tersedia.
* Mengakses fitur pendaftaran mandiri dan memantau **Histori Peminjaman** pribadi secara transparan.

---

## 🛠️ Pengembangan Sistem (Tech Stack)
* **Platform Target:** Android
* **Bahasa Pemrograman:** Kotlin
* **Development Environment:** Android Studio
* **Version Control System:** Git & GitHub

---

## ⚙️ Panduan Kontribusi Kelompok (Git Workflow)
Untuk menjaga kerapian repository, pastikan seluruh anggota kelompok mengikuti aturan berikut:
1. Sebelum mulai bekerja, lakukan `git checkout main` dan `git pull origin main` untuk mendapatkan kode paling update.
2. Buat branch baru untuk tugas masing-masing: `git checkout -b nama-anggota/fitur-yang-dikerjakan`.
3. Lakukan commit secara berkala dengan pesan yang jelas: `git commit -m "Menambahkan fitur X"`.
4. Setelah selesai, push branch kalian ke GitHub: `git push origin nama-branch-kalian`.
5. Buka **Pull Request (PR)** di GitHub agar bisa diperiksa bersama sebelum di-merge ke branch `main`.
