# Tugas Praktikum Pengembangan Aplikasi Mobile (PAM)

**Muhammad Syauqi Imaduddin | 245150400111020 | PAM - SI F**

## Deskripsi Tugas
Aplikasi ini merupakan implementasi tugas praktikum mata kuliah Pengembangan Aplikasi Mobile (PAM) yang membahas tentang **State Hoisting, Async/Side Effect dengan LaunchedEffect, serta Event Handling & Recomposition pada Jetpack Compose**. 

Aplikasi "Pemesanan Tiket" ini dirancang dengan arsitektur *deklaratif* menggunakan pola **State Hoisting**, di mana seluruh State utama dikelola oleh Parent Composable (`TicketBookingParentScreen`) dan diteruskan ke Child Composable (`TicketBookingContent`). Selain itu, proses pemesanan tiket menggunakan **`LaunchedEffect`** untuk menangani efek samping pemrosesan pesanan secara *asynchronous* (termasuk validasi nama kosong dan delay 5 detik).

## Lokasi File Penting
- **Main Code (State Hoisting & LaunchedEffect)**: `app/src/main/java/com/example/ticket_advaned/MainActivity.kt`
- **Color Theme (Palet Warna Gojek)**: `app/src/main/java/com/example/ticket_advaned/ui/theme/Color.kt`
- **App Theme Configuration**: `app/src/main/java/com/example/ticket_advaned/ui/theme/Theme.kt`

## Fitur UI / UX & Konsep Teknis
- **Tema Gojek Modern:** Menggunakan palet warna hijau khas Gojek (`#00AA13`) pada Header, Tombol utama, Border input, dan Status Card.
- **State Hoisting (Parent-Managed State):**
  1. **Harga Tiket:** Dikelola di Parent Composable dan terhitung secara otomatis sesuai jumlah tiket.
  2. **Jumlah Tiket:** Dikelola di Parent Composable melalui event callback increment (`+`) dan decrement (`-`).
  3. **Nama Pembeli Tiket:** Dikelola di Parent Composable dengan `OutlinedTextField`.
- **Side Effect Handling via `LaunchedEffect`:**
  - **Status 1 (Nama Masih Kosong):** Jika pengguna menekan tombol "Pesan Tiket" saat nama belum diisi, `LaunchedEffect` menampilkan `Status : Nama Masih Kosong` dengan latar merah dan ikon peringatan.
  - **Status 2 (Memproses Pesanan):** Jika nama terisi, status berubah menjadi `Status : Memproses pesanan...` (dengan *loading spinner* dan tombol diproteksi).
  - **Status 3 (Tiket Telah Dipesan):** `LaunchedEffect` menjalankan penundaan selama 5 detik (`delay(5.seconds)`), lalu memperbarui status secara otomatis menjadi `Status : Tiket telah dipesan` dengan latar hijau dan ikon centang.
- **Dynamic Calculation & Formatting:** Perhitungan *Total Harga* dikalkulasikan secara real-time (`hargaTiket * jumlahTiket`) dan diformat ke dalam mata uang Rupiah (`Rp 50.000`).
