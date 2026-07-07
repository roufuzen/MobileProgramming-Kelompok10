package com.example.perpustakaandigital.ui.laporan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanScreen(
    riwayatPengembalian: List<LaporanItem> = emptyList(),
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
                .background(Color(0xFFF5F5F5))
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> LaporanPeminjaman()
                1 -> LaporanPengembalian(riwayatPengembalian)
                2 -> LaporanStokBuku()
            }
        }
    }
}

@Composable
fun LaporanPeminjaman() {
    val dummyData = listOf(
        LaporanItem("Laskar Pelangi", "Budi Santoso", "12 Okt 2023"),
        LaporanItem("Bumi", "Siti Aminah", "15 Okt 2023"),
        LaporanItem("Negeri 5 Menara", "Ahmad Fauzi", "18 Okt 2023")
    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            SummaryCard("Total Peminjaman Bulan Ini", "45 Buku", Icons.Default.DateRange)
        }
        items(dummyData) { item ->
            LaporanCard(item)
        }
    }
}

@Composable
fun LaporanPengembalian(riwayat: List<LaporanItem> = emptyList()) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            SummaryCard("Total Pengembalian", "${riwayat.size} Buku", Icons.Default.Info)
        }
        items(riwayat) { item ->
            LaporanCard(item)
        }
    }
}

@Composable
fun LaporanStokBuku() {
    val dummyData = listOf(
        LaporanItem("Laskar Pelangi", "Tersedia: 5", "Total: 10"),
        LaporanItem("Bumi", "Tersedia: 2", "Total: 8"),
        LaporanItem("Negeri 5 Menara", "Tersedia: 0", "Total: 5")
    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            SummaryCard("Total Judul Buku", "120 Judul", Icons.Default.Info)
        }
        items(dummyData) { item ->
            LaporanCard(item)
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 14.sp)
                Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LaporanCard(item: LaporanItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.subtitle, fontSize = 14.sp, color = Color.Gray)
                Text(item.date, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            }
            if (item.status != null) {
                Text(
                    text = item.status,
                    fontSize = 12.sp,
                    color = if (item.status.contains("Terlambat")) Color.Red else Color(0xFF2E7D32),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

data class LaporanItem(
    val title: String,
    val subtitle: String,
    val date: String,
    val status: String? = null
)
