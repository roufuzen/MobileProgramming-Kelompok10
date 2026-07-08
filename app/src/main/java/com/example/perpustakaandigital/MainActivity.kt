package com.example.perpustakaandigital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.perpustakaandigital.ui.auth.LoginScreen
import com.example.perpustakaandigital.ui.auth.RegisterScreen
import com.example.perpustakaandigital.ui.buku.KelolaBukuScreen
import com.example.perpustakaandigital.ui.buku.Buku
import com.example.perpustakaandigital.ui.home.MainMenuScreen
import com.example.perpustakaandigital.ui.transaksi.PengembalianScreen
import com.example.perpustakaandigital.ui.transaksi.PeminjamanScreen
import com.example.perpustakaandigital.ui.theme.PerpustakaanDigitalTheme
import com.example.perpustakaandigital.ui.pemustaka.FormPendaftaranScreen
import com.example.perpustakaandigital.ui.pemustaka.DetailAnggotaScreen
import com.example.perpustakaandigital.ui.laporan.LaporanScreen
import com.example.perpustakaandigital.ui.laporan.LaporanItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerpustakaanDigitalTheme {
                var currentScreen by remember { mutableStateOf("login") }
                var isAdmin by remember { mutableStateOf(false) }

                // --- WADAH PENAMPUNG DATA ANGGOTA BARU ---
                var savedName by remember { mutableStateOf("") }
                var savedNik by remember { mutableStateOf("") }
                var savedPhone by remember { mutableStateOf("") }
                var savedAddress by remember { mutableStateOf("") }
                var isUserRegistered by remember { mutableStateOf(false) }

                // --- WADAH PENAMPUNG DATA BUKU ---
                val daftarBuku = remember {
                    mutableStateListOf(
                        Buku("BK001", "Laskar Pelangi", "Andrea Hirata", "Fiksi", 5),
                        Buku("BK002", "Bumi Manusia", "Pramoedya Ananta Toer", "Sejarah", 3),
                        Buku("BK003", "Filosofi Teras", "Henry Manampiring", "Non-Fiksi", 8)
                    )
                }

                // --- WADAH PENAMPUNG RIWAYAT ---
                val riwayatPeminjaman = remember { mutableStateListOf<LaporanItem>() }
                val riwayatPengembalian = remember { mutableStateListOf<LaporanItem>() }

                when (currentScreen) {
                    "login" -> {
                        LoginScreen(
                            onLoginClick = { isLoginAdmin ->
                                isAdmin = isLoginAdmin
                                currentScreen = "home"
                            },
                            onRegisterClick = { currentScreen = "register" }
                        )
                    }
                    "register" -> {
                        RegisterScreen(
                            onRegisterClick = {
                                isAdmin = false
                                currentScreen = "home"
                            },
                            onLoginClick = { currentScreen = "login" }
                        )
                    }
                    "home" -> {
                        MainMenuScreen(
                            onLogout = { currentScreen = "login" },
                            onKelolaBuku = { currentScreen = "kelolabuku" },
                            onPeminjaman = { currentScreen = "peminjaman" },
                            onPengembalian = { currentScreen = "pengembalian" },
                            onPendaftaranAnggota = { currentScreen = "form_pendaftaran" },
                            onLaporan = { currentScreen = "laporan" },
                            isSudahDaftar = isUserRegistered || isAdmin,
                            isAdmin = isAdmin,
                            totalPeminjaman = riwayatPeminjaman.size,
                            totalPengembalian = riwayatPengembalian.size
                        )
                    }
                    "kelolabuku" -> {
                        KelolaBukuScreen(
                            bukuList = daftarBuku,
                            onBack = { currentScreen = "home" }
                        )
                    }
                    "peminjaman" -> {
                        PeminjamanScreen(
                            bukuList = daftarBuku,
                            onBorrowSuccess = { item ->
                                riwayatPeminjaman.add(0, item)
                            },
                            onBack = { currentScreen = "home" }
                        )
                    }
                    "pengembalian" -> {
                        PengembalianScreen(
                            bukuList = daftarBuku,
                            pendaftarName = if (isAdmin) "Administrator" else if (isUserRegistered) savedName else "Tamu / Belum Daftar",
                            pendaftarId = if (isAdmin) "ADM-001" else if (isUserRegistered) savedNik else "-",
                            riwayatPengembalian = riwayatPengembalian,
                            isAdmin = isAdmin,
                            onBack = { currentScreen = "home" },
                            onReturnSuccess = { item ->
                                riwayatPengembalian.add(0, item) 
                            }
                        )
                    }
                    "laporan" -> {
                        LaporanScreen(
                            riwayatPeminjaman = riwayatPeminjaman,
                            riwayatPengembalian = riwayatPengembalian,
                            bukuList = daftarBuku,
                            isAdmin = isAdmin,
                            onBack = { currentScreen = "home" }
                        )
                    }

                    "form_pendaftaran" -> {
                        FormPendaftaranScreen(
                            onNextClick = { name, nik, phone, address ->
                                savedName = name
                                savedNik = nik
                                savedPhone = phone
                                savedAddress = address
                                currentScreen = "detail_anggota"
                            }
                        )
                    }

                    "detail_anggota" -> {
                        DetailAnggotaScreen(
                            name = savedName,
                            nik = savedNik,
                            phone = savedPhone,
                            address = savedAddress,
                            onFinishClick = {
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
