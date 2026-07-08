package com.example.perpustakaandigital.ui.buku

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Buku(
    val kode: String,
    val judul: String,
    val penulis: String,
    val kategori: String,
    val stok: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelolaBukuScreen(
    bukuList: SnapshotStateList<Buku>,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFormDialog by remember { mutableStateOf(false) }
    var bukuToEdit by remember { mutableStateOf<Buku?>(null) }
    var bukuToDelete by remember { mutableStateOf<Buku?>(null) }

    val filteredList = bukuList.filter {
        it.judul.contains(searchQuery, ignoreCase = true) ||
                it.kode.contains(searchQuery, ignoreCase = true)
    }

    // Dialog form tambah/ubah buku
    if (showFormDialog) {
        BukuFormDialog(
            initialData = bukuToEdit,
            onDismiss = {
                showFormDialog = false
                bukuToEdit = null
            },
            onSave = { newBuku ->
                if (bukuToEdit != null) {
                    val index = bukuList.indexOfFirst { it.kode == bukuToEdit!!.kode }
                    if (index != -1) bukuList[index] = newBuku
                } else {
                    bukuList.add(newBuku)
                }
                showFormDialog = false
                bukuToEdit = null
            }
        )
    }

    // Dialog konfirmasi hapus
    bukuToDelete?.let { buku ->
        AlertDialog(
            onDismissRequest = { bukuToDelete = null },
            title = { Text("Hapus Buku") },
            text = { Text("Apakah Anda yakin ingin menghapus \"${buku.judul}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    bukuList.remove(buku)
                    bukuToDelete = null
                }) {
                    Text("Hapus", color = Color(0xFFC62828))
                }
            },
            dismissButton = {
                TextButton(onClick = { bukuToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Buku", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    bukuToEdit = null
                    showFormDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Buku", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cari judul atau kode buku") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tidak ada data buku ditemukan", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredList) { buku ->
                        BukuCard(
                            buku = buku,
                            onEdit = {
                                bukuToEdit = buku
                                showFormDialog = true
                            },
                            onDelete = { bukuToDelete = buku }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BukuCard(
    buku: Buku,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(buku.judul, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(buku.penulis, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Kode: ${buku.kode} • ${buku.kategori}", fontSize = 12.sp, color = Color.Gray)
                Text(
                    "Stok: ${buku.stok}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (buku.stok > 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Ubah", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color(0xFFC62828))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BukuFormDialog(
    initialData: Buku?,
    onDismiss: () -> Unit,
    onSave: (Buku) -> Unit
) {
    var kode by remember { mutableStateOf(initialData?.kode ?: "") }
    var judul by remember { mutableStateOf(initialData?.judul ?: "") }
    var penulis by remember { mutableStateOf(initialData?.penulis ?: "") }
    var kategori by remember { mutableStateOf(initialData?.kategori ?: "") }
    var stok by remember { mutableStateOf(initialData?.stok?.toString() ?: "") }

    val isValid = kode.isNotBlank() && judul.isNotBlank() && stok.toIntOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialData != null) "Ubah Buku" else "Tambah Buku") },
        text = {
            Column {
                OutlinedTextField(
                    value = kode,
                    onValueChange = { kode = it },
                    label = { Text("Kode Buku") },
                    enabled = initialData == null, // kode tidak diubah saat edit
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = judul,
                    onValueChange = { judul = it },
                    label = { Text("Judul Buku") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = penulis,
                    onValueChange = { penulis = it },
                    label = { Text("Penulis") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = kategori,
                    onValueChange = { kategori = it },
                    label = { Text("Kategori") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = stok,
                    onValueChange = { if (it.all { c -> c.isDigit() }) stok = it },
                    label = { Text("Stok") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = isValid,
                onClick = {
                    onSave(
                        Buku(
                            kode = kode,
                            judul = judul,
                            penulis = penulis,
                            kategori = kategori,
                            stok = stok.toIntOrNull() ?: 0
                        )
                    )
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
