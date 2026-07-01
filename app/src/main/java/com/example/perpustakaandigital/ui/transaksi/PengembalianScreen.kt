package com.example.perpustakaandigital.ui.transaksi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengembalianScreen(
    onBack: () -> Unit
) {
    var bookCode by remember { mutableStateOf("") }
    var bookInfo by remember { mutableStateOf<BookReturnInfo?>(null) }
    var showReport by remember { mutableStateOf(false) }
    
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
    val todayDate = Date()
    val todayStr = remember { sdf.format(todayDate) }

    if (showReport && bookInfo != null) {
        ReturnReportDialog(
            info = bookInfo!!,
            actualReturnDateStr = todayStr,
            onDismiss = { 
                showReport = false
                bookInfo = null
                bookCode = ""
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengembalian Buku", color = Color.White, fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Scan atau Masukkan Kode Buku",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = bookCode,
                onValueChange = { bookCode = it },
                label = { Text("Kode Buku") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { /* Implement Scan Logic */ }) {
                        Icon(Icons.Default.Info, contentDescription = "Scan QR")
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Simulasi data ditemukan dengan keterlambatan lama untuk menunjukkan Tahun, Bulan, Hari
                    bookInfo = if (bookCode.isNotEmpty()) {
                        BookReturnInfo(
                            title = "Laskar Pelangi",
                            borrower = "Budi Santoso",
                            borrowDate = "10 Januari 2022",
                            dueDate = "17 Januari 2022",
                            isLate = true
                        )
                    } else null
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("CARI DATA PEMINJAMAN")
            }

            Spacer(modifier = Modifier.height(32.dp))

            bookInfo?.let { info ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Informasi Peminjaman Aktif", fontWeight = FontWeight.Bold, color = Color.Gray)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        InfoRow(label = "Judul Buku", value = info.title)
                        InfoRow(label = "Peminjam", value = info.borrower)
                        InfoRow(label = "Tanggal Pinjam", value = info.borrowDate)
                        InfoRow(label = "Tenggat Waktu", value = info.dueDate)
                        InfoRow(label = "Tanggal Kembali", value = todayStr)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        StatusBadge(isLate = info.isLate)
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { showReport = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("PROSES PENGEMBALIAN", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReturnReportDialog(
    info: BookReturnInfo,
    actualReturnDateStr: String,
    onDismiss: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
    
    // Parse tanggal jatuh tempo dan tanggal kembali aktual
    // Catatan: Gunakan try-catch jika format tanggal tidak pasti, namun di sini kita asumsikan konsisten
    val dueDate = try { LocalDate.parse(info.dueDate, formatter) } catch (e: Exception) { LocalDate.now() }
    val returnDate = LocalDate.now()
    
    // Hitung selisih durasi keterlambatan (Tahun, Bulan, Hari)
    val period = Period.between(dueDate, returnDate)
    val years = period.years
    val months = period.months
    val days = period.days
    
    val lateDurationText = buildString {
        if (years > 0) append("$years Tahun ")
        if (months > 0) append("$months Bulan ")
        if (days > 0) append("$days Hari")
        if (years <= 0 && months <= 0 && days <= 0) append("0 Hari")
    }.trim()

    // Hitung tanggal berakhir sanksi (Hari ini + durasi keterlambatan)
    val sanctionEndDate = returnDate.plus(period).format(formatter)
    
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Selesai")
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (info.isLate) Icons.Default.Warning else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (info.isLate) Color(0xFFC62828) else Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Laporan Pengembalian")
            }
        },
        text = {
            Column {
                Text("Buku telah berhasil dikembalikan.", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ReportRow("Peminjam", info.borrower)
                        ReportRow("Judul Buku", info.title)
                        ReportRow("Tanggal Pinjam", info.borrowDate)
                        ReportRow("Tenggat Waktu", info.dueDate)
                        ReportRow("Tanggal Kembali", actualReturnDateStr)
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        
                        ReportRow("Status", if (info.isLate) "Terlambat" else "Tepat Waktu")
                        
                        if (info.isLate) {
                            ReportRow("Total Keterlambatan", lateDurationText)
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Surface(
                                color = Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "SANKSI PENANGGUHAN",
                                        color = Color(0xFFC62828),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Berdasarkan waktu keterlambatan dari tanggal ${info.dueDate} sampai ${actualReturnDateStr},",
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "Anda dilarang meminjam buku selama $lateDurationText.",
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Dapat meminjam kembali mulai pada:",
                                        color = Color.Black,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = sanctionEndDate,
                                        color = Color(0xFFC62828),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun ReportRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = Color.Gray)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.SemiBold, color = Color.Black, fontSize = 14.sp)
    }
}

@Composable
fun StatusBadge(isLate: Boolean) {
    val backgroundColor = if (isLate) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
    val textColor = if (isLate) Color(0xFFC62828) else Color(0xFF2E7D32)
    val statusText = if (isLate) "STATUS: TERLAMBAT" else "STATUS: TEPAT WAKTU"

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = statusText,
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp
        )
    }
}

data class BookReturnInfo(
    val title: String,
    val borrower: String,
    val borrowDate: String,
    val dueDate: String,
    val isLate: Boolean
)
