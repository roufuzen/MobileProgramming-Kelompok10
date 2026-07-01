package com.example.perpustakaandigital.ui.transaksi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeminjamanScreen(
    onBack: () -> Unit
) {
    var bookCode by remember { mutableStateOf("") }
    var borrowerName by remember { mutableStateOf("") }
    
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
    val calendar = Calendar.getInstance()
    
    val borrowDate = remember { sdf.format(calendar.time) }
    
    // Perkiraan kembali 7 hari kemudian
    val calReturn = Calendar.getInstance()
    calReturn.add(Calendar.DAY_OF_YEAR, 7)
    val estimatedReturnDate = remember { sdf.format(calReturn.time) }
    
    // Tanggal Kembali (biasanya kosong saat pinjam baru)
    var returnDate by remember { mutableStateOf("-") }

    Scaffold(
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
                onValueChange = { bookCode = it },
                label = { Text("Kode Buku") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nama Peminjam
            OutlinedTextField(
                value = borrowerName,
                onValueChange = { borrowerName = it },
                label = { Text("Nama Peminjam") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
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
                label = { Text("Perkiraan Kembali (7 Hari)") },
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

            // Tanggal Kembali (Aktual)
            OutlinedTextField(
                value = returnDate,
                onValueChange = { },
                label = { Text("Tanggal Kembali (Aktual)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                readOnly = true,
                enabled = false,
                placeholder = { Text("Belum dikembalikan") }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Save Peminjaman */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("SIMPAN PEMINJAMAN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
