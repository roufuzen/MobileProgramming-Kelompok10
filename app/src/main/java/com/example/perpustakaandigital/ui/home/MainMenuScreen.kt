package com.example.perpustakaandigital.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Struktur data MenuItem yang disesuaikan agar mendukung status kunci (isLocked)
data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val isLocked: Boolean = false,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onLogout: () -> Unit,
    onKelolaBuku: () -> Unit,
    onPeminjaman: () -> Unit,
    onPengembalian: () -> Unit,
    onPendaftaranAnggota: () -> Unit,
    onLaporan: () -> Unit,
    isSudahDaftar: Boolean
) {
    val context = LocalContext.current

    // Susunan daftar menu menggunakan parameter isSudahDaftar langsung
    val menuItems = listOf(
        MenuItem(
            title = "Pendaftaran Anggota",
            icon = Icons.Default.Person,
            isLocked = false,
            onClick = onPendaftaranAnggota
        ),
        MenuItem(
            title = "Kelola Buku",
            icon = Icons.Default.Edit,
            isLocked = false,
            onClick = onKelolaBuku
        ),
        MenuItem(
            title = "Peminjaman",
            icon = Icons.Default.Add,
            isLocked = !isSudahDaftar,
            onClick = {
                if (isSudahDaftar) {
                    onPeminjaman()
                } else {
                    Toast.makeText(context, "Akses Ditolak! Anda harus mendaftar keanggotaan terlebih dahulu.", Toast.LENGTH_LONG).show()
                }
            }
        ),
        MenuItem(
            title = "Pengembalian",
            icon = Icons.Default.Refresh,
            isLocked = !isSudahDaftar,
            onClick = {
                if (isSudahDaftar) {
                    onPengembalian()
                } else {
                    Toast.makeText(context, "Akses Ditolak! Anda harus mendaftar keanggotaan terlebih dahulu.", Toast.LENGTH_LONG).show()
                }
            }
        ),
        MenuItem(
            title = "Laporan",
            icon = Icons.Default.Info,
            isLocked = false,
            onClick = onLaporan
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Perpustakaan Digital",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                text = "Menu Utama",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(menuItems) { item ->
                    MenuCard(item)
                }
            }
        }
    }
}

@Composable
fun MenuCard(item: MenuItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { item.onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isLocked) Color.LightGray.copy(alpha = 0.4f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (item.isLocked) 0.dp else 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(48.dp),
                tint = if (item.isLocked) Color.Gray else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (item.isLocked) Color.Gray else MaterialTheme.colorScheme.primary
            )
        }
    }
}
