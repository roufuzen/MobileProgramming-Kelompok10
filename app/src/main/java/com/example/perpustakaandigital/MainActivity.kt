package com.example.perpustakaandigital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.perpustakaandigital.ui.auth.LoginScreen
import com.example.perpustakaandigital.ui.auth.RegisterScreen
import com.example.perpustakaandigital.ui.buku.KelolaBukuScreen
import com.example.perpustakaandigital.ui.home.MainMenuScreen
import com.example.perpustakaandigital.ui.transaksi.PengembalianScreen
import com.example.perpustakaandigital.ui.theme.PerpustakaanDigitalTheme
<<<<<<< HEAD
import com.example.perpustakaandigital.ui.pemustaka.FormPendaftaranScreen
import com.example.perpustakaandigital.ui.pemustaka.DetailAnggotaScreen
=======
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerpustakaanDigitalTheme {
                var currentScreen by remember { mutableStateOf("login") }

<<<<<<< HEAD
                // --- WADAH PENAMPUNG DATA ANGGOTA BARU ---
                var savedName by remember { mutableStateOf("") }
                var savedNik by remember { mutableStateOf("") }
                var savedPhone by remember { mutableStateOf("") }
                var savedAddress by remember { mutableStateOf("") }
                var isUserRegistered by remember { mutableStateOf(false) }

=======
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
                when (currentScreen) {
                    "login" -> {
                        LoginScreen(
                            onLoginClick = { currentScreen = "home" },
                            onRegisterClick = { currentScreen = "register" }
                        )
                    }
                    "register" -> {
                        RegisterScreen(
                            onRegisterClick = { currentScreen = "home" },
                            onLoginClick = { currentScreen = "login" }
                        )
                    }
                    "home" -> {
                        MainMenuScreen(
                            onLogout = { currentScreen = "login" },
                            onKelolaBuku = { currentScreen = "kelolabuku" },
<<<<<<< HEAD
                            onPeminjaman = { /* Nanti diisi oleh teman Anda */ },
                            onPengembalian = { currentScreen = "pengembalian" },
                            onPendaftaranAnggota = { currentScreen = "form_pendaftaran" },
                            // --- KIRIM STATUS DAFTAR KE MAIN MENU ---
                            isSudahDaftar = isUserRegistered
                        )
                    }
                    "kelolabuku" -> {
                        KelolaBukuScreen(
=======
                            onPeminjaman = { /* Navigasi ke Peminjaman */ },
                            onPengembalian = { currentScreen = "pengembalian" }
                        )
                    }
                    "kelolabuku" -> {
                        KelolaBukuScreen (
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
                            onBack = { currentScreen = "home" }
                        )
                    }
                    "pengembalian" -> {
                        PengembalianScreen(
                            onBack = { currentScreen = "home" }
                        )
                    }
<<<<<<< HEAD

                    // --- RUTE 1: HALAMAN FORMULIR PENDAFTARAN ANGGOTA ---
                    "form_pendaftaran" -> {
                        FormPendaftaranScreen(
                            onNextClick = { name, nik, phone, address ->
                                // Simpan kiriman data input ke wadah penampung
                                savedName = name
                                savedNik = nik
                                savedPhone = phone
                                savedAddress = address

                                // Pindah ke layar cetak kartu & QR Code
                                currentScreen = "detail_anggota"
                            }
                        )
                    }

                    // --- RUTE 2: HALAMAN KARTU DIGITAL + GENERATE QR CODE ---
                    "detail_anggota" -> {
                        DetailAnggotaScreen(
                            name = savedName,
                            nik = savedNik,
                            phone = savedPhone,
                            address = savedAddress,
                            onFinishClick = {
                                // --- KUNCI DIUBAH MENJADI TERBUKA DI SINI ---
                                isUserRegistered = true
                                currentScreen = "home"
                            }
                        )
                    }
=======
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
                }
            }
        }
    }
}