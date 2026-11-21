package sianter;

import java.util.ArrayList;

// Simpan dan muat data ke file CSV supaya data tidak hilang waktu aplikasi ditutup.
// Modul 3 (perulangan) untuk baca/tulis tiap baris, Modul 4 (method).
public class Penyimpanan {

    public static final String FILE_ANTREAN = "data/antrean.csv";
    public static final String FILE_RIWAYAT = "data/riwayat.csv";

    public static void simpanAntrean(Antrean antrean) {
        // TODO: tulis tiap servis di antrean jadi satu baris CSV
    }

    public static Antrean muatAntrean() {
        // TODO: baca file baris per baris, bikin ServisHardware/ServisSoftware sesuai kolom jenis
        return new Antrean(10);
    }

    public static void simpanRiwayat(ArrayList<Servis> riwayat) {
        // TODO
    }

    public static ArrayList<Servis> muatRiwayat() {
        // TODO
        return new ArrayList<>();
    }
}
