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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerpustakaanDigitalTheme {
                var currentScreen by remember { mutableStateOf("login") }

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
                            onPeminjaman = { /* Navigasi ke Peminjaman */ },
                            onPengembalian = { currentScreen = "pengembalian" }
                        )
                    }
                    "kelolabuku" -> {
                        KelolaBukuScreen (
                            onBack = { currentScreen = "home" }
                        )
                    }
                    "pengembalian" -> {
                        PengembalianScreen(
                            onBack = { currentScreen = "home" }
                        )
                    }
                }
            }
        }
    }
}