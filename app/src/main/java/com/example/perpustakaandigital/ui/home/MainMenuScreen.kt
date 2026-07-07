package com.example.perpustakaandigital.ui.home

<<<<<<< HEAD
import android.widget.Toast
=======
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
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
<<<<<<< HEAD
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
=======
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
<<<<<<< HEAD
import androidx.compose.ui.platform.LocalContext
=======
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

<<<<<<< HEAD
// Struktur data MenuItem yang disesuaikan agar mendukung status kunci (isLocked)
data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val isLocked: Boolean = false,
=======
data class MenuItem(
    val title: String,
    val icon: ImageVector,
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onLogout: () -> Unit,
    onKelolaBuku: () -> Unit,
    onPeminjaman: () -> Unit,
<<<<<<< HEAD
    onPengembalian: () -> Unit,
    onPendaftaranAnggota: () -> Unit,
    isSudahDaftar: Boolean // <-- TANGKAP STATUS DARI MAIN ACTIVITY DI SINI
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
            isLocked = !isSudahDaftar, // Gunakan isSudahDaftar dari MainActivity
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
            isLocked = !isSudahDaftar, // Gunakan isSudahDaftar dari MainActivity
            onClick = {
                if (isSudahDaftar) {
                    onPengembalian()
                } else {
                    Toast.makeText(context, "Akses Ditolak! Anda harus mendaftar keanggotaan terlebih dahulu.", Toast.LENGTH_LONG).show()
                }
            }
        )
=======
    onPengembalian: () -> Unit
) {
    val menuItems = listOf(
        MenuItem("Kelola Buku", Icons.Default.Edit, onKelolaBuku),
        MenuItem("Peminjaman", Icons.Default.Add, onPeminjaman),
        MenuItem("Pengembalian", Icons.Default.Refresh, onPengembalian)
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
    )

    Scaffold(
        topBar = {
            TopAppBar(
<<<<<<< HEAD
                title = {
                    Text(
                        "Perpustakaan Digital",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
=======
                title = { 
                    Text(
                        "Perpustakaan Digital", 
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ) 
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
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
<<<<<<< HEAD
            .clickable { item.onClick() }, // Aksi klik tetap berjalan untuk mengecek kondisi kunci di dalam MenuItem
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            // Jika statusnya terkunci (isLocked = true), warnanya diubah menjadi abu-abu pudar
            containerColor = if (item.isLocked) Color.LightGray.copy(alpha = 0.4f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (item.isLocked) 0.dp else 4.dp)
=======
            .clickable { item.onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(4.dp)
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
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
<<<<<<< HEAD
                // Jika terkunci, warna ikon ikut pudar menjadi abu-abu
                tint = if (item.isLocked) Color.Gray else MaterialTheme.colorScheme.primary
=======
                tint = MaterialTheme.colorScheme.primary
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
<<<<<<< HEAD
                // Jika terkunci, warna teks ikut pudar
                color = if (item.isLocked) Color.Gray else MaterialTheme.colorScheme.primary
            )
        }
    }
}
=======
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
>>>>>>> 491efb48f1640f4aac828275bfa160a9a73783d3
