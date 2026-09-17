# Tugas Praktikum Pengembangan Aplikasi Mobile (PAM)

**Muhammad Syauqi Imaduddin | 245150400111020 | PAM - SI F**

## Deskripsi Tugas
Aplikasi ini merupakan implementasi tugas praktikum mata kuliah Pengembangan Aplikasi Mobile (PAM) yang membahas tentang **State Hoisting, Async/Side Effect dengan LaunchedEffect, serta Event Handling & Recomposition pada Jetpack Compose**. 

Aplikasi "Pemesanan Tiket" ini dirancang dengan arsitektur *deklaratif* menggunakan pola **State Hoisting**, di mana seluruh State utama dikelola oleh Parent Composable (`TicketBookingParentScreen`) dan diteruskan ke Child Composable (`TicketBookingContent`). Selain itu, proses pemesanan tiket menggunakan **`LaunchedEffect`** untuk menangani efek samping pemrosesan pesanan secara *asynchronous* (termasuk validasi nama kosong dan delay 5 detik).

## Lokasi File Penting
- **Main Code (State Hoisting & LaunchedEffect)**: `app/src/main/java/com/example/ticket_advaned/MainActivity.kt`
- **Color Theme (Palet Warna Gojek)**: `app/src/main/java/com/example/ticket_advaned/ui/theme/Color.kt`
- **App Theme Configuration**: `app/src/main/java/com/example/ticket_advaned/ui/theme/Theme.kt`
- **Modul Praktikum (Materi & Modul yang diberikan)**: app/Muhammad Syauqi Imaduddin_245150400111020_MODUL4
- **Screenshot Hasil Praktikum (SS-an Aplikasi)**: app

<img width="377" height="821" alt="modul-4-1" src="https://github.com/user-attachments/assets/02964ff4-d5ea-4892-b90f-a8a4da76fc15" />
<img width="395" height="826" alt="modul-4-2" src="https://github.com/user-attachments/assets/f8ebdb13-5ad2-4145-9932-59157d064c9b" />
<img width="386" height="827" alt="modul-4-3" src="https://github.com/user-attachments/assets/8245819c-53c4-4420-bba0-da7310033aa9" />
<img width="386" height="832" alt="modul-4-4" src="https://github.com/user-attachments/assets/4a22d44a-eb02-4a68-bac7-c5d4ca1090d9" />


