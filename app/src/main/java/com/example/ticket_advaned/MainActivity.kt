package com.example.ticket_advaned

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticket_advaned.ui.theme.GojekGreen
import com.example.ticket_advaned.ui.theme.GojekGreenDark
import com.example.ticket_advaned.ui.theme.GojekGreenLight
import com.example.ticket_advaned.ui.theme.StatusErrorBg
import com.example.ticket_advaned.ui.theme.StatusErrorText
import com.example.ticket_advaned.ui.theme.StatusIdleBg
import com.example.ticket_advaned.ui.theme.StatusIdleText
import com.example.ticket_advaned.ui.theme.StatusProcessingBg
import com.example.ticket_advaned.ui.theme.StatusProcessingText
import com.example.ticket_advaned.ui.theme.StatusSuccessBg
import com.example.ticket_advaned.ui.theme.StatusSuccessText
import com.example.ticket_advaned.ui.theme.TicketadvanedTheme
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicketadvanedTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TicketBookingParentScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * Status Pemesanan Tiket
 */
sealed class OrderStatus {
    object Idle : OrderStatus()
    object EmptyName : OrderStatus()
    object Processing : OrderStatus()
    object Success : OrderStatus()
}

/**
 * PARENT COMPOSABLE (Stateful)
 *
 * Mengelola State yang di-hoist dari Child:
 * 1. Harga Tiket (hargaTiket)
 * 2. Jumlah Tiket (jumlahTiket)
 * 3. Nama Pembeli Tiket (namaPembeli)
 *
 * Serta mengelola logika bisnis dan LaunchedEffect untuk status pemesanan.
 */
@Composable
fun TicketBookingParentScreen(
    modifier: Modifier = Modifier
) {
    // 1. State Harga Tiket (State dikelola Parent)
    var hargaTiket by remember { mutableIntStateOf(50_000) }

    // 2. State Jumlah Tiket (State dikelola Parent)
    var jumlahTiket by remember { mutableIntStateOf(1) }

    // 3. State Nama Pembeli Tiket (State dikelola Parent)
    var namaPembeli by remember { mutableStateOf("") }

    // State Status Pemesanan
    var orderStatus by remember { mutableStateOf<OrderStatus>(OrderStatus.Idle) }

    // State pemicu proses pemesanan
    var submitTrigger by remember { mutableLongStateOf(0L) }

    // LaunchedEffect untuk menangani alur validasi dan pemrosesan pemesanan (delay 5 detik)
    LaunchedEffect(submitTrigger) {
        if (submitTrigger > 0L) {
            if (namaPembeli.isBlank()) {
                // Status jika nama masih kosong
                orderStatus = OrderStatus.EmptyName
            } else {
                // Status memproses pesanan
                orderStatus = OrderStatus.Processing
                // Simulasi pemrosesan selama 5 detik
                delay(5.seconds)
                // Status setelah 5 detik: Tiket telah dipesan
                orderStatus = OrderStatus.Success
            }
        }
    }

    // Mengirimkan state dan callback ke Child Composable (State Hoisting)
    TicketBookingContent(
        hargaTiket = hargaTiket,
        jumlahTiket = jumlahTiket,
        namaPembeli = namaPembeli,
        orderStatus = orderStatus,
        onNamaChange = { newName ->
            namaPembeli = newName
            if (orderStatus is OrderStatus.EmptyName && newName.isNotBlank()) {
                orderStatus = OrderStatus.Idle
            }
        },
        onJumlahIncrement = {
            jumlahTiket++
            if (orderStatus is OrderStatus.Success) orderStatus = OrderStatus.Idle
        },
        onJumlahDecrement = {
            if (jumlahTiket > 1) {
                jumlahTiket--
                if (orderStatus is OrderStatus.Success) orderStatus = OrderStatus.Idle
            }
        },
        onPesanClick = {
            submitTrigger = System.currentTimeMillis()
        },
        modifier = modifier
    )
}

/**
 * CHILD COMPOSABLE (Stateless)
 *
 * Menerima state dari Parent dan memanggil callback ketika ada interaksi pengguna.
 */
@Composable
fun TicketBookingContent(
    hargaTiket: Int,
    jumlahTiket: Int,
    namaPembeli: String,
    orderStatus: OrderStatus,
    onNamaChange: (String) -> Unit,
    onJumlahIncrement: () -> Unit,
    onJumlahDecrement: () -> Unit,
    onPesanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalHarga = hargaTiket * jumlahTiket

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Top Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GojekGreen)
                .padding(vertical = 18.dp, horizontal = 20.dp)
        ) {
            Text(
                text = "Pemesanan Tiket",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Informasi Harga Tiket (State 1)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = GojekGreenLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Harga Tiket",
                            fontSize = 12.sp,
                            color = GojekGreenDark,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = formatRupiah(hargaTiket) + " / tiket",
                            fontSize = 16.sp,
                            color = GojekGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Total Harga",
                            fontSize = 12.sp,
                            color = GojekGreenDark,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = formatRupiah(totalHarga),
                            fontSize = 18.sp,
                            color = GojekGreenDark,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // Input Nama Pembeli Tiket (State 3)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Nama",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                OutlinedTextField(
                    value = namaPembeli,
                    onValueChange = onNamaChange,
                    placeholder = {
                        Text(
                            text = "Masukkan nama Anda",
                            color = Color(0xFF9E9E9E)
                        )
                    },
                    singleLine = true,
                    enabled = orderStatus !is OrderStatus.Processing,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GojekGreen,
                        focusedLabelColor = GojekGreen,
                        cursorColor = GojekGreen,
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Input Jumlah Tiket (State 2)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Jumlah Tiket",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tombol Decrement (-)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (jumlahTiket > 1 && orderStatus !is OrderStatus.Processing)
                                    Color(0xFFE8ECEF)
                                else Color(0xFFF0F0F0)
                            )
                            .clickable(
                                enabled = jumlahTiket > 1 && orderStatus !is OrderStatus.Processing,
                                onClick = onJumlahDecrement
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "-",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (jumlahTiket > 1) Color(0xFF212121) else Color(0xFFBDBDBD)
                        )
                    }

                    // Menampilkan Jumlah Tiket
                    Text(
                        text = "$jumlahTiket",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF212121),
                        modifier = Modifier.width(40.dp)
                    )

                    // Tombol Increment (+)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (orderStatus !is OrderStatus.Processing)
                                    Color(0xFFE8ECEF)
                                else Color(0xFFF0F0F0)
                            )
                            .clickable(
                                enabled = orderStatus !is OrderStatus.Processing,
                                onClick = onJumlahIncrement
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Tombol Pesan Tiket
            Button(
                onClick = onPesanClick,
                enabled = orderStatus !is OrderStatus.Processing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GojekGreen,
                    disabledContainerColor = Color(0xFFB0BEC5),
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = if (orderStatus is OrderStatus.Processing) "Memproses..." else "Pesan Tiket",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Status Card (Berubah sesuai state yang di-handle LaunchedEffect)
            StatusCard(orderStatus = orderStatus)
        }
    }
}

/**
 * Component Tampilan Status Pemesanan
 */
@Composable
fun StatusCard(
    orderStatus: OrderStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, contentColor) = when (orderStatus) {
        OrderStatus.Idle -> StatusIdleBg to StatusIdleText
        OrderStatus.EmptyName -> StatusErrorBg to StatusErrorText
        OrderStatus.Processing -> StatusProcessingBg to StatusProcessingText
        OrderStatus.Success -> StatusSuccessBg to StatusSuccessText
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            when (orderStatus) {
                OrderStatus.Idle -> {
                    Text(
                        text = "Status: Silakan pesan tiket",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                }

                OrderStatus.EmptyName -> {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Status: Nama Masih Kosong",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                }

                OrderStatus.Processing -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = GojekGreen,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Status: Memproses pesanan...",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = GojekGreenDark
                    )
                }

                OrderStatus.Success -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = GojekGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Status: Tiket telah dipesan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusSuccessText
                    )
                }
            }
        }
    }
}

/**
 * Format Angka ke Format Mata Uang Rupiah (Contoh: 50000 -> Rp 50.000)
 */
fun formatRupiah(amount: Int): String {
    return "Rp " + String.format(Locale.getDefault(), "%,d", amount).replace(',', '.')
}

@Preview(showBackground = true)
@Composable
fun TicketBookingPreview() {
    TicketadvanedTheme {
        TicketBookingParentScreen()
    }
}
