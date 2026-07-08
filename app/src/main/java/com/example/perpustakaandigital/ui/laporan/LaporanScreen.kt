package com.example.perpustakaandigital.ui.laporan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanScreen(
    riwayatPeminjaman: List<LaporanItem> = emptyList(),
    riwayatPengembalian: List<LaporanItem> = emptyList(),
    isAdmin: Boolean = false,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Peminjaman", "Pengembalian", "Stok Buku")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laporan & Statistik", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
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
        ) {
            // Header Section dengan Gradient agar senada dengan Main Menu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                        ),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(
                        text = "Ringkasan Aktivitas",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = when(selectedTab) {
                            0 -> "Riwayat Peminjaman"
                            1 -> "Riwayat Pengembalian"
                            else -> "Status Stok Buku"
                        },
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { 
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            ) 
                        }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    0 -> LaporanPeminjaman(riwayatPeminjaman)
                    1 -> LaporanPengembalian(riwayatPengembalian)
                    2 -> LaporanStokBuku()
                }
            }
        }
    }
}

@Composable
fun LaporanPeminjaman(riwayat: List<LaporanItem> = emptyList()) {
    val displayData = if (riwayat.isEmpty()) {
        listOf(
            LaporanItem("Laskar Pelangi", "Budi Santoso", "12 Okt 2023"),
            LaporanItem("Bumi", "Siti Aminah", "15 Okt 2023"),
            LaporanItem("Negeri 5 Menara", "Ahmad Fauzi", "18 Okt 2023")
        )
    } else {
        riwayat
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailedSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Total Pinjam",
                    value = "${displayData.size}",
                    icon = Icons.Default.DateRange,
                    color = Color(0xFFE3F2FD),
                    contentColor = Color(0xFF1976D2)
                )
                DetailedSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Status",
                    value = "Aktif",
                    icon = Icons.Default.Info,
                    color = Color(0xFFFFF3E0),
                    contentColor = Color(0xFFE65100)
                )
            }
        }
        item {
            Text("Daftar Transaksi", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
        }
        items(displayData) { item ->
            LaporanCardImproved(item, type = "peminjaman")
        }
    }
}

@Composable
fun LaporanPengembalian(riwayat: List<LaporanItem> = emptyList()) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailedSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Total Kembali",
                    value = "${riwayat.size}",
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFFE8F5E9),
                    contentColor = Color(0xFF388E3C)
                )
                DetailedSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Kinerja",
                    value = "Bagus",
                    icon = Icons.Default.ThumbUp,
                    color = Color(0xFFF3E5F5),
                    contentColor = Color(0xFF7B1FA2)
                )
            }
        }
        
        if (riwayat.isEmpty()) {
            item {
                EmptyState(message = "Belum ada riwayat pengembalian.")
            }
        } else {
            item {
                Text("Daftar Pengembalian", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
            }
            items(riwayat) { item ->
                LaporanCardImproved(item, type = "pengembalian")
            }
        }
    }
}

@Composable
fun LaporanStokBuku() {
    val dummyData = listOf(
        LaporanItem("Laskar Pelangi", "Kategori: Novel", "5", status = "Tersedia"),
        LaporanItem("Bumi", "Kategori: Novel", "2", status = "Menipis"),
        LaporanItem("Negeri 5 Menara", "Kategori: Novel", "0", status = "Habis"),
        LaporanItem("Pemrograman Dasar", "Kategori: Edukasi", "8", status = "Tersedia")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailedSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Total Judul",
                    value = "120",
                    icon = Icons.Default.Menu,
                    color = Color(0xFFE0F2F1),
                    contentColor = Color(0xFF00796B)
                )
                DetailedSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Koleksi",
                    value = "Terupdate",
                    icon = Icons.Default.Refresh,
                    color = Color(0xFFE8EAF6),
                    contentColor = Color(0xFF303F9F)
                )
            }
        }
        item {
            Text("Status Stok Per Judul", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
        }
        items(dummyData) { item ->
            StockCardDetailed(item)
        }
    }
}

@Composable
fun DetailedSummaryCard(modifier: Modifier = Modifier, title: String, value: String, icon: ImageVector, color: Color, contentColor: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = contentColor)
            Text(title, fontSize = 12.sp, color = contentColor.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun LaporanCardImproved(item: LaporanItem, type: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (type == "peminjaman") Color(0xFFE3F2FD) else Color(0xFFE8F5E9),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (type == "peminjaman") Icons.Default.Add else Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = if (type == "peminjaman") Color(0xFF1976D2) else Color(0xFF388E3C)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(item.subtitle, fontSize = 13.sp, color = Color.Gray)
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(item.date, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                if (item.status != null) {
                    Text(
                        text = item.status,
                        fontSize = 11.sp,
                        color = if (item.status.contains("Terlambat")) Color.Red else Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun StockCardDetailed(item: LaporanItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(item.subtitle, fontSize = 13.sp, color = Color.Gray)
                }
                
                Surface(
                    color = when(item.status) {
                        "Tersedia" -> Color(0xFFE8F5E9)
                        "Menipis" -> Color(0xFFFFF3E0)
                        else -> Color(0xFFFFEBEE)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = item.status ?: "",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when(item.status) {
                            "Tersedia" -> Color(0xFF2E7D32)
                            "Menipis" -> Color(0xFFEF6C00)
                            else -> Color(0xFFC62828)
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                val stockValue = item.date.filter { it.isDigit() }.toFloatOrNull() ?: 0f
                val progressValue = (stockValue / 10f).coerceIn(0f, 1f)
                
                LinearProgressIndicator(
                    progress = { progressValue },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(CircleShape),
                    color = when(item.status) {
                        "Tersedia" -> Color(0xFF4CAF50)
                        "Menipis" -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    },
                    trackColor = Color(0xFFEEEEEE)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Sisa: ${item.date}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(message, color = Color.Gray, fontSize = 14.sp)
    }
}

data class LaporanItem(
    val title: String,
    val subtitle: String,
    val date: String,
    val status: String? = null
)
