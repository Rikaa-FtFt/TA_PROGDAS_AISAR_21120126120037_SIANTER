# SIANTER

**Sistem Antrean dan Riwayat Servis Komputer Berbasis JavaFX**
Tugas Akhir Praktikum Pemrograman Dasar 2026, Teknik Komputer Universitas Diponegoro.

Aisar Aulady Albilad (21120126120037)

## Deskripsi

SIANTER membantu tempat servis komputer mengatur antrean pelanggan. Admin mendaftarkan pelanggan beserta perangkat dan keluhannya, teknisi memanggil antrean secara berurutan, lalu servis yang selesai masuk ke riwayat lengkap dengan biayanya. Kesalahan input bisa dibatalkan dengan tombol Undo, dan data tetap tersimpan walaupun aplikasi ditutup.

## Penerapan Modul

| Modul | Penerapan |
|---|---|
| 1. Variabel, Tipe Data, Array | data pelanggan dan biaya, array pada queue dan stack |
| 2. Pengkondisian | validasi input, status servis, perhitungan biaya |
| 3. Perulangan | menampilkan antrean, membaca dan menulis file |
| 4. Function & Method | method pada tiap class |
| 5. OOP I | class `Pelanggan`, `Servis` |
| 6. OOP II | abstract class `Servis`, turunan `ServisHardware` dan `ServisSoftware` |
| 7. Stack & Queue | `Antrean` (queue), `RiwayatAksi` (stack untuk undo) |
| 8. GUI | antarmuka JavaFX (`MainApp`) |

## Cara Menjalankan

Butuh JDK 21 dan Maven (atau IntelliJ IDEA).

```
mvn javafx:run
```
