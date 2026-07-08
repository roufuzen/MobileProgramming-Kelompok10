package com.example.perpustakaandigital.ui.transaksi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perpustakaandigital.ui.buku.Buku
import com.example.perpustakaandigital.ui.laporan.LaporanItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Data buku hasil pencarian kode buku pada form Peminjaman.
 */
data class BookLoanInfo(
    val kode: String,
    val title: String,
    val stock: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeminjamanScreen(
    bukuList: SnapshotStateList<Buku>,
    onBorrowSuccess: (LaporanItem) -> Unit,
    onBack: () -> Unit
) {
    var bookCode by remember { mutableStateOf("") }
    var borrowerName by remember { mutableStateOf("") }
    var bookInfo by remember { mutableStateOf<BookLoanInfo?>(null) }
    var searchAttempted by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))

    val borrowDate = remember { sdf.format(Calendar.getInstance().time) }
    val estimatedReturnDate = remember {
        val calReturn = Calendar.getInstance()
        calReturn.add(Calendar.DAY_OF_YEAR, 14)
        sdf.format(calReturn.time)
    }

    val isBookAvailable = bookInfo != null && bookInfo!!.stock > 0
    val isFormValid = isBookAvailable && borrowerName.isNotBlank()

    if (showConfirmation && bookInfo != null) {
        LoanConfirmationDialog(
            info = bookInfo!!,
            borrowerName = borrowerName,
            borrowDate = borrowDate,
            estimatedReturnDate = estimatedReturnDate,
            onDismiss = {
                // Kurangi stok buku di list global
                val index = bukuList.indexOfFirst { it.kode == bookInfo!!.kode }
                if (index != -1) {
                    val currentBuku = bukuList[index]
                    if (currentBuku.stok > 0) {
                        bukuList[index] = currentBuku.copy(stok = currentBuku.stok - 1)
                    }
                }

                val newItem = LaporanItem(
                    title = bookInfo!!.title,
                    subtitle = borrowerName,
                    date = borrowDate,
                    status = "Dipinjam"
                )
                onBorrowSuccess(newItem)

                showConfirmation = false
                // Reset form setelah peminjaman berhasil disimpan
                bookCode = ""
                borrowerName = ""
                bookInfo = null
                searchAttempted = false
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Peminjaman berhasil disimpan")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Peminjaman Buku", color = Color.White, fontWeight = FontWeight.Bold) },
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Form Peminjaman Baru",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Kode Buku
            OutlinedTextField(
                value = bookCode,
                onValueChange = {
                    bookCode = it
                    // Reset hasil pencarian setiap kali kode diubah
                    bookInfo = null
                    searchAttempted = false
                },
                label = { Text("Kode Buku") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    searchAttempted = true
                    // Mencari data buku berdasarkan kode dari bukuList global
                    val foundBuku = bukuList.find { it.kode.equals(bookCode.trim(), ignoreCase = true) }
                    bookInfo = foundBuku?.let {
                        BookLoanInfo(
                            kode = it.kode,
                            title = it.judul,
                            stock = it.stok
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(46.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("CARI DATA BUKU")
            }

            if (searchAttempted && bookInfo == null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Buku dengan kode tersebut tidak ditemukan.",
                    color = Color(0xFFC62828),
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            bookInfo?.let { info ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Informasi Buku", fontWeight = FontWeight.Bold, color = Color.Gray)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        LoanInfoRow(label = "Judul Buku", value = info.title)
                        LoanInfoRow(label = "Kode Buku", value = info.kode)
                        LoanInfoRow(label = "Stok Tersedia", value = info.stock.toString())
                        Spacer(modifier = Modifier.height(12.dp))
                        StockBadge(available = info.stock > 0)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nama Peminjam
            OutlinedTextField(
                value = borrowerName,
                onValueChange = { borrowerName = it },
                label = { Text("Nama Peminjam") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                enabled = isBookAvailable
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tanggal Pinjam
            OutlinedTextField(
                value = borrowDate,
                onValueChange = { },
                label = { Text("Tanggal Pinjam") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    disabledLabelColor = Color.Gray,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Perkiraan Kembali
            OutlinedTextField(
                value = estimatedReturnDate,
                onValueChange = { },
                label = { Text("Perkiraan Kembali (2 Minggu)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    disabledLabelColor = Color.Gray,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { showConfirmation = true },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("SIMPAN PEMINJAMAN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            if (bookInfo != null && !isBookAvailable) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Stok buku habis, peminjaman tidak dapat diproses.",
                    color = Color(0xFFC62828),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun LoanConfirmationDialog(
    info: BookLoanInfo,
    borrowerName: String,
    borrowDate: String,
    estimatedReturnDate: String,
    onDismiss: () -> Unit
) {
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
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Bukti Peminjaman")
            }
        },
        text = {
            Column {
                Text("Peminjaman buku berhasil dicatat.", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        LoanInfoRow("Peminjam", borrowerName)
                        LoanInfoRow("Judul Buku", info.title)
                        LoanInfoRow("Kode Buku", info.kode)
                        LoanInfoRow("Tanggal Pinjam", borrowDate)
                        LoanInfoRow("Perkiraan Kembali", estimatedReturnDate)
                    }
                }
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun LoanInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.SemiBold, color = Color.Black, fontSize = 14.sp)
    }
}

@Composable
fun StockBadge(available: Boolean) {
    val backgroundColor = if (available) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val textColor = if (available) Color(0xFF2E7D32) else Color(0xFFC62828)
    val icon = if (available) Icons.Default.CheckCircle else Icons.Default.Warning
    val text = if (available) "TERSEDIA" else "STOK HABIS"

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = text, color = textColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}
