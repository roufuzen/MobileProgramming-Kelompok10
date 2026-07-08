package com.example.perpustakaandigital.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Struktur data MenuItem yang disesuaikan
data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
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
    isSudahDaftar: Boolean,
    isAdmin: Boolean = false,
    totalPeminjaman: Int = 0,
    totalPengembalian: Int = 0
) {
    val context = LocalContext.current

    val menuItems = mutableListOf(
        MenuItem(
            title = "Pendaftaran",
            icon = Icons.Default.Person,
            color = Color(0xFF4CAF50),
            onClick = onPendaftaranAnggota
        ),
        MenuItem(
            title = "Kelola Buku",
            icon = Icons.Default.Edit,
            color = Color(0xFF2196F3),
            onClick = onKelolaBuku
        ),
        MenuItem(
            title = "Peminjaman",
            icon = Icons.Default.Add,
            isLocked = !isSudahDaftar,
            color = Color(0xFFFF9800),
            onClick = {
                if (isSudahDaftar) onPeminjaman()
                else Toast.makeText(context, "Akses Ditolak! Daftar anggota dulu.", Toast.LENGTH_SHORT).show()
            }
        ),
        MenuItem(
            title = "Pengembalian",
            icon = Icons.Default.Refresh,
            isLocked = !isSudahDaftar,
            color = Color(0xFFE91E63),
            onClick = {
                if (isSudahDaftar) onPengembalian()
                else Toast.makeText(context, "Akses Ditolak! Daftar anggota dulu.", Toast.LENGTH_SHORT).show()
            }
        ),
        MenuItem(
            title = if (isAdmin) "Laporan (Admin)" else "Laporan Saya",
            icon = Icons.Default.Info,
            color = Color(0xFF9C27B0),
            onClick = onLaporan
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Perpustakaan Digital", fontWeight = FontWeight.Bold, color = Color.White)
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Logout", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                        ),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(if (isAdmin) Icons.Default.Star else Icons.Default.AccountCircle, null, tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Halo, Selamat Datang", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        Text(if (isAdmin) "Administrator" else "Pustakawan Digital", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Dashboard Statistik
            Text(
                if (isAdmin) "Ringkasan Statistik Global" else "Ringkasan Statistik Saya",
                modifier = Modifier.padding(horizontal = 24.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Peminjaman",
                    value = totalPeminjaman.toString(),
                    icon = Icons.Default.Star,
                    color = Color(0xFFE3F2FD),
                    contentColor = Color(0xFF1976D2)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Pengembalian",
                    value = totalPengembalian.toString(),
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFFE8F5E9),
                    contentColor = Color(0xFF388E3C)
                )
            }

            // News / Information Section
            Text(
                "Informasi & Berita",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(110.dp)
            ) {
                val newsList = listOf(
                    "Peminjaman buku sekarang bisa sampai 14 hari!",
                    "Koleksi buku baru kategori Teknologi sudah tersedia.",
                    "Jangan lupa kembalikan buku tepat waktu ya!",
                    "Event: Diskusi Buku Bulanan akan diadakan hari Jumat."
                )
                items(newsList) { news ->
                    NewsCard(news)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu Section
            Text(
                "Menu Utama",
                modifier = Modifier.padding(horizontal = 24.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            // Grid menu menggunakan chunks karena dalam vertical scroll
            menuItems.chunked(2).forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { item ->
                        MenuCardImproved(modifier = Modifier.weight(1f), item = item)
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, title: String, value: String, icon: ImageVector, color: Color, contentColor: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = contentColor)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = contentColor)
            Text(title, fontSize = 12.sp, color = contentColor.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun NewsCard(text: String) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFFFF3E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Notifications, null, tint = Color(0xFFFF9800), modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 3,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun MenuCardImproved(modifier: Modifier, item: MenuItem) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { item.onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isLocked) Color.LightGray.copy(alpha = 0.3f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (item.isLocked) 0.dp else 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (item.isLocked) Color.Gray.copy(alpha = 0.2f) else item.color.copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    modifier = Modifier.size(20.dp),
                    tint = if (item.isLocked) Color.Gray else item.color
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.isLocked) Color.Gray else Color.Black
                )
                if (item.isLocked) {
                    Icon(Icons.Default.Lock, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                }
            }
        }
    }
}
