package com.example.perpustakaandigital.ui.transaksi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perpustakaandigital.ui.laporan.LaporanCardImproved
import com.example.perpustakaandigital.ui.laporan.LaporanItem
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengembalianScreen(
    pendaftarName: String = "Budi Santoso",
    pendaftarId: String = "M-2023001",
    riwayatPengembalian: List<LaporanItem> = emptyList(),
    isAdmin: Boolean = false,
    onReturnSuccess: (LaporanItem) -> Unit,
    onBack: () -> Unit
) {
    var bookCode by remember { mutableStateOf("") }
    var bookInfo by remember { mutableStateOf<BookReturnInfo?>(null) }
    var showReport by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var isSearchPerformed by remember { mutableStateOf(false) }

    // State untuk kondisi buku
    var selectedCondition by remember { mutableStateOf("Baik") }
    val conditions = listOf("Baik", "Rusak Ringan", "Rusak Berat", "Hilang")
    var expanded by remember { mutableStateOf(false) }

    // Sinkronisasi data dummy dengan KelolaBukuScreen
    val registeredBooks = mapOf(
        "BK001" to "Laskar Pelangi",
        "BK002" to "Bumi Manusia",
        "BK003" to "Filosofi Teras"
    )

    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
    val todayDate = Date()
    val todayStr = remember { sdf.format(todayDate) }

    if (showReport && bookInfo != null) {
        ReturnReportDialog(
            info = bookInfo!!,
            actualReturnDateStr = todayStr,
            condition = selectedCondition,
            onDismiss = {
                val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
                val dueDate = try { LocalDate.parse(bookInfo!!.dueDate, formatter) } catch (e: Exception) { LocalDate.now() }
                val returnDate = LocalDate.now()
                val period = Period.between(dueDate, returnDate)

                val lateDurationText = if (bookInfo!!.isLate) {
                    val years = period.years
                    val months = period.months
                    val days = period.days
                    buildString {
                        append(" (")
                        if (years > 0) append("$years thn ")
                        if (months > 0) append("$months bln ")
                        if (days > 0) append("$days hari")
                        if (years <= 0 && months <= 0 && days <= 0) append("0 hari")
                        append(")")
                    }.trim()
                } else ""

                val statusText = if (bookInfo!!.isLate) "Terlambat$lateDurationText - $selectedCondition" else "Tepat Waktu - $selectedCondition"
                onReturnSuccess(
                    LaporanItem(
                        title = bookInfo!!.title,
                        subtitle = bookInfo!!.borrower,
                        date = todayStr,
                        status = statusText
                    )
                )

                showReport = false
                bookInfo = null
                bookCode = ""
                isSearchPerformed = false
                selectedCondition = "Baik"
            }
        )
    }

    if (showHistory) {
        HistoryDialog(
            riwayat = riwayatPengembalian,
            onDismiss = { showHistory = false }
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
            if (isAdmin) {
                Surface(
                    color = Color(0xFFFFF9C4),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "MODE ADMIN: Anda dapat mengubah kondisi buku untuk ujicoba.",
                        modifier = Modifier.padding(12.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF57F17),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                text = "Masukkan Kode Buku",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = bookCode,
                onValueChange = {
                    bookCode = it
                    isSearchPerformed = false
                    bookInfo = null
                },
                label = { Text("Kode Buku (Contoh: BK001)") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("BK001, BK002, atau BK003") },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TOMBOL CARI
            Button(
                onClick = {
                    isSearchPerformed = true
                    val code = bookCode.trim().uppercase()
                    val judulBuku = registeredBooks[code]

                    bookInfo = if (judulBuku != null) {
                        BookReturnInfo(
                            title = judulBuku,
                            borrower = pendaftarName,
                            memberId = pendaftarId,
                            memberStatus = if (pendaftarId != "-") "Aktif" else "Tidak Aktif",
                            borrowDate = "10 Januari 2022",
                            dueDate = "17 Januari 2022",
                            isLate = true
                        )
                    } else {
                        null
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("CARI DATA PEMINJAMAN")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // TOMBOL RIWAYAT
            OutlinedButton(
                onClick = { showHistory = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("RIWAYAT PENGEMBALIAN")
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (bookInfo != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Data Peminjam",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = "Foto Profil",
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(text = "Nama: ${bookInfo!!.borrower}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                Text(text = "NIM/No. Anggota: ${bookInfo!!.memberId}", fontSize = 14.sp, color = Color.Gray)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "Status Anggota: ", fontSize = 14.sp, color = Color.Gray)
                                    Text(
                                        text = bookInfo!!.memberStatus,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if(bookInfo!!.memberStatus == "Aktif") Color(0xFF2E7D32) else Color.Red
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                        Text(text = "Informasi Peminjaman", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        InfoRow(label = "Judul Buku", value = bookInfo!!.title)
                        InfoRow(label = "Tanggal Pinjam", value = bookInfo!!.borrowDate)
                        InfoRow(label = "Tenggat Waktu", value = bookInfo!!.dueDate)
                        InfoRow(label = "Tanggal Kembali", value = todayStr)

                        Spacer(modifier = Modifier.height(16.dp))

                        // PILIHAN KONDISI BUKU
                        Text(text = "Kondisi Buku Saat Kembali", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded && isAdmin,
                            onExpandedChange = { if (isAdmin) expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedCondition,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { if (isAdmin) ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                supportingText = { if (!isAdmin) Text("Hanya admin yang dapat mengubah kondisi") }
                            )
                            if (isAdmin) {
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    conditions.forEach { condition ->
                                        DropdownMenuItem(
                                            text = { Text(condition) },
                                            onClick = {
                                                selectedCondition = condition
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        StatusBadge(isLate = bookInfo!!.isLate)

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
            } else if (isSearchPerformed && bookCode.isNotBlank()) {
                Text(
                    "Kode buku tidak terdaftar dalam sistem kelola buku.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun HistoryDialog(
    riwayat: List<LaporanItem>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Tutup") }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Riwayat Pengembalian")
            }
        },
        text = {
            if (riwayat.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    Text("Belum ada riwayat pengembalian.", color = Color.Gray)
                }
            } else {
                Box(modifier = Modifier.heightIn(max = 400.dp)) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(riwayat) { item ->
                            LaporanCardImproved(item, type = "pengembalian")
                        }
                    }
                }
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun ReturnReportDialog(
    info: BookReturnInfo,
    actualReturnDateStr: String,
    condition: String,
    onDismiss: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
    val dueDate = try { LocalDate.parse(info.dueDate, formatter) } catch (e: Exception) { LocalDate.now() }
    val returnDate = LocalDate.now()
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

    val sanctionEndDate = returnDate.plus(period).format(formatter)

    // Tenggat waktu ganti buku (7 hari dari pengembalian)
    val replacementDeadline = returnDate.plusDays(7).format(formatter)

    val isDamagedOrLost = condition == "Rusak Berat" || condition == "Hilang"

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
                    imageVector = if (info.isLate || isDamagedOrLost) Icons.Default.Warning else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (info.isLate || isDamagedOrLost) Color(0xFFC62828) else Color(0xFF2E7D32)
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
                        ReportRow("NIM/No. Anggota", info.memberId)
                        ReportRow("Judul Buku", info.title)
                        ReportRow("Kondisi Buku", condition)
                        ReportRow("Tanggal Kembali", actualReturnDateStr)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        ReportRow("Status Keterlambatan", if (info.isLate) "Terlambat" else "Tepat Waktu")

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
                                        text = "Anda dilarang meminjam buku selama $lateDurationText.",
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = "Dapat meminjam kembali mulai pada: $sanctionEndDate",
                                        color = Color.Black,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        if (isDamagedOrLost) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Surface(
                                color = Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "WAJIB GANTI BUKU BARU",
                                        color = Color(0xFFE65100),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Kondisi buku $condition. Anda wajib mengganti dengan judul yang sama atau setara harga buku.",
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Tenggat Waktu Penggantian:",
                                        color = Color.Black,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = replacementDeadline,
                                        color = Color(0xFFE65100),
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
        horizontalArrangement = Arrangement.SpaceBetween) {
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
    val memberId: String,
    val memberStatus: String,
    val borrowDate: String,
    val dueDate: String,
    val isLate: Boolean
)
