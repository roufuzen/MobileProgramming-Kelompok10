package com.example.perpustakaandigital.ui.pemustaka

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

@Composable
fun DetailAnggotaScreen(
    name: String,
    nik: String,
    phone: String,
    address: String,
    onFinishClick: () -> Unit
) {
    // Generate QR Code secara lokal menggunakan ZXing berdasarkan NIK Pemustaka
    val qrCodeBitmap = remember(nik) { generateQRCode(nik) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "KARTU ANGGOTA DIGITAL",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = "DISPUSIPDA PROVINSI",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Komponen Tampilan Biodata Pemustaka
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Nama Lengkap", fontSize = 12.sp, color = Color.Gray)
                    Text(name, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Nomor Induk Kependudukan (NIK)", fontSize = 12.sp, color = Color.Gray)
                    Text(nik, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Nomor Telepon", fontSize = 12.sp, color = Color.Gray)
                    Text(phone, fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Alamat", fontSize = 12.sp, color = Color.Gray)
                    Text(address, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Menampilkan QR Code hasil generate
            qrCodeBitmap?.let { bitmap ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.size(200.dp).padding(8.dp)
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "QR Code Anggota",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Pindai QR untuk Verifikasi Petugas", fontSize = 12.sp, color = Color.Gray)
        }

        Button(
            onClick = onFinishClick,
            modifier = Modifier.fillMaxWidth().height(50.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("SELESAI & KEMBALI", fontWeight = FontWeight.Bold)
        }
    }
}

// Fungsi pembantu untuk membuat QR Code bitmap
fun generateQRCode(text: String): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bmp
    } catch (e: Exception) {
        null
    }
}