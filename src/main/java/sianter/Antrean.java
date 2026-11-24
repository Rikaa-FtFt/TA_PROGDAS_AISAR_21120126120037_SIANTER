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
        return jumlah == 0;
    }

    public int size() {
        return jumlah;
    }

    // Pelanggan baru masuk di belakang
    public void enqueue(Servis s) {
        if (jumlah == data.length) {
            perbesar();
        }
        belakang = (belakang + 1) % data.length;
        data[belakang] = s;
        jumlah++;
    }

    // Pelanggan paling depan dipanggil (keluar dari antrean)
    public Servis dequeue() {
        if (isEmpty()) {
            return null;
        }
        Servis s = data[depan];
        data[depan] = null;
        depan = (depan + 1) % data.length;
        jumlah--;
        return s;
    }

    // Lihat yang paling depan tanpa mengeluarkan
    public Servis peek() {
        if (isEmpty()) {
            return null;
        }
        return data[depan];
    }

    // Ambil elemen ke-i dihitung dari depan (0 = paling depan), untuk ditampilkan di tabel
    public Servis get(int i) {
        if (i < 0 || i >= jumlah) {
            return null;
        }
        return data[(depan + i) % data.length];
    }

    // Untuk undo "Panggil": servis dikembalikan ke posisi paling depan
    public void kembalikanKeDepan(Servis s) {
        if (jumlah == data.length) {
            perbesar();
        }
        depan = (depan - 1 + data.length) % data.length;   // mundur 1, dari 0 balik ke ujung
        data[depan] = s;
        if (jumlah == 0) {
            belakang = depan;   // cuma 1 elemen: depan dan belakang di slot yang sama
        }
        jumlah++;
    }

    // Untuk undo "Tambah": buang elemen paling belakang
    public Servis hapusBelakang() {
        if (isEmpty()) {
            return null;
        }
        Servis s = data[belakang];
        data[belakang] = null;
        belakang = (belakang - 1 + data.length) % data.length;
        jumlah--;
        return s;
    }

    private void perbesar() {
        Servis[] baru = new Servis[data.length * 2];
        for (int i = 0; i < jumlah; i++) {
            baru[i] = get(i);
        }
        data = baru;
        depan = 0;
        belakang = jumlah - 1;
    }
}
