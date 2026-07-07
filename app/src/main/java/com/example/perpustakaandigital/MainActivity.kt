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
import com.example.perpustakaandigital.ui.pemustaka.FormPendaftaranScreen
import com.example.perpustakaandigital.ui.pemustaka.DetailAnggotaScreen
import com.example.perpustakaandigital.ui.laporan.LaporanScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerpustakaanDigitalTheme {
                var currentScreen by remember { mutableStateOf("login") }

                // --- WADAH PENAMPUNG DATA ANGGOTA BARU ---
                var savedName by remember { mutableStateOf("") }
                var savedNik by remember { mutableStateOf("") }
                var savedPhone by remember { mutableStateOf("") }
                var savedAddress by remember { mutableStateOf("") }
                var isUserRegistered by remember { mutableStateOf(false) }

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
                            onPeminjaman = { /* Nanti diisi oleh teman Anda */ },
                            onPengembalian = { currentScreen = "pengembalian" },
                            onPendaftaranAnggota = { currentScreen = "form_pendaftaran" },
                            onLaporan = { currentScreen = "laporan" },
                            // --- KIRIM STATUS DAFTAR KE MAIN MENU ---
                            isSudahDaftar = isUserRegistered
                        )
                    }
                    "kelolabuku" -> {
                        KelolaBukuScreen(
                            onBack = { currentScreen = "home" }
                        )
                    }
                    "pengembalian" -> {
                        PengembalianScreen(
                            onBack = { currentScreen = "home" }
                        )
                    }
                    "laporan" -> {
                        LaporanScreen(
                            onBack = { currentScreen = "home" }
                        )
                    }

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
                }
            }
        }
    }
}