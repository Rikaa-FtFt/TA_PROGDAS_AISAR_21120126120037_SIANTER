package sianter;

// Modul 7: Queue buatan sendiri berbasis array (circular), seperti di modul.
// Bedanya: kalau penuh, array-nya diperbesar, jadi tidak mentok di kapasitas tetap.
public class Antrean {

    private Servis[] data;
    private int depan;     // index elemen paling depan
    private int belakang;  // index elemen paling belakang
    private int jumlah;    // banyak elemen sekarang

    public Antrean(int kapasitasAwal) {
        data = new Servis[kapasitasAwal];
        depan = 0;
        belakang = -1;
        jumlah = 0;
    }

    public boolean isEmpty() {
        // TODO
        return false;
    }

    public int size() {
        return jumlah;
    }

    // Pelanggan baru masuk di belakang
    public void enqueue(Servis s) {
        // TODO: kalau penuh panggil perbesar() dulu, lalu geser belakang secara circular
    }

    // Pelanggan paling depan dipanggil (keluar dari antrean)
    public Servis dequeue() {
        // TODO: kalau kosong return null
        return null;
    }

    // Lihat yang paling depan tanpa mengeluarkan
    public Servis peek() {
        // TODO
        return null;
    }

    // Ambil elemen ke-i dihitung dari depan (0 = paling depan), untuk ditampilkan di tabel
    public Servis get(int i) {
        // TODO: index aslinya (depan + i) % data.length
        return null;
    }

    // Untuk undo "Panggil": servis dikembalikan ke posisi paling depan
    public void kembalikanKeDepan(Servis s) {
        // TODO: geser depan mundur satu secara circular
    }

    // Untuk undo "Tambah": buang elemen paling belakang
    public Servis hapusBelakang() {
        // TODO
        return null;
    }

    private void perbesar() {
        // TODO: buat array 2x lebih besar, salin elemen urut dari depan, reset depan = 0
    }
}
