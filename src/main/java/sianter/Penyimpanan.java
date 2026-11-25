package sianter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

// Simpan dan muat data ke file CSV supaya data tidak hilang waktu aplikasi ditutup.
// Modul 3 (perulangan) untuk baca/tulis tiap baris, Modul 4 (method).
//
// Satu baris = satu servis, kolomnya dipisah titik koma (;):
// id;jenis;nama;noHp;perangkat;keluhan;biayaJasa;tambahan;status
// "tambahan" = biaya sparepart untuk Hardware, true/false instal ulang untuk Software.
public class Penyimpanan {

    public static final String FILE_ANTREAN = "data/antrean.csv";
    public static final String FILE_RIWAYAT = "data/riwayat.csv";

    private static final String HEADER = "id;jenis;nama;noHp;perangkat;keluhan;biayaJasa;tambahan;status";
    private static final int JUMLAH_KOLOM = 9;

    // Servis yang sedang diproses ikut disimpan di baris pertama (statusnya "Diproses"),
    // jadi tidak hilang kalau aplikasi ditutup di tengah pengerjaan
    public static void simpanAntrean(Antrean antrean, Servis sedangDiproses) {
        ArrayList<Servis> daftar = new ArrayList<>();
        if (sedangDiproses != null) {
            daftar.add(sedangDiproses);
        }
        for (int i = 0; i < antrean.size(); i++) {
            daftar.add(antrean.get(i));
        }
        tulisFile(FILE_ANTREAN, daftar);
    }

    public static Antrean muatAntrean() {
        Antrean antrean = new Antrean(10);
        ArrayList<Servis> daftar = bacaFile(FILE_ANTREAN);
        for (int i = 0; i < daftar.size(); i++) {
            antrean.enqueue(daftar.get(i));
        }
        return antrean;
    }

    public static void simpanRiwayat(ArrayList<Servis> riwayat) {
        tulisFile(FILE_RIWAYAT, riwayat);
    }

    public static ArrayList<Servis> muatRiwayat() {
        return bacaFile(FILE_RIWAYAT);
    }

    // ================= Baca / tulis file =================

    private static void tulisFile(String namaFile, ArrayList<Servis> daftar) {
        File file = new File(namaFile);
        file.getParentFile().mkdirs();   // bikin folder data/ kalau belum ada

        // try-with-resources: file otomatis ditutup walaupun terjadi error
        try (PrintWriter out = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            out.println(HEADER);
            for (int i = 0; i < daftar.size(); i++) {
                out.println(keBaris(daftar.get(i)));
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan " + namaFile + ": " + e.getMessage());
        }
    }

    private static ArrayList<Servis> bacaFile(String namaFile) {
        ArrayList<Servis> daftar = new ArrayList<>();
        File file = new File(namaFile);
        if (!file.exists()) {
            return daftar;   // pertama kali dijalankan, file belum ada
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String baris = in.readLine();   // baris pertama = header, dilewati
            while ((baris = in.readLine()) != null) {
                Servis s = dariBaris(baris);
                if (s != null) {
                    daftar.add(s);
                }
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca " + namaFile + ": " + e.getMessage());
        }
        return daftar;
    }

    // ================= Servis <-> baris CSV =================

    private static String keBaris(Servis s) {
        String tambahan;
        if (s instanceof ServisHardware) {
            ServisHardware h = (ServisHardware) s;
            tambahan = String.valueOf((long) h.getBiayaSparepart());
        } else {
            ServisSoftware sw = (ServisSoftware) s;
            tambahan = String.valueOf(sw.isInstalUlang());
        }

        return s.getId() + ";"
                + s.getJenis() + ";"
                + bersihkan(s.getPelanggan().getNama()) + ";"
                + bersihkan(s.getPelanggan().getNoHp()) + ";"
                + bersihkan(s.getPerangkat()) + ";"
                + bersihkan(s.getKeluhan()) + ";"
                + (long) s.getBiayaJasa() + ";"
                + tambahan + ";"
                + s.getStatus();
    }

    // Kebalikan keBaris. Baris yang rusak (kolom kurang, angka tidak valid) dilewati: return null.
    private static Servis dariBaris(String baris) {
        String[] kolom = baris.split(";", -1);
        if (kolom.length != JUMLAH_KOLOM) {
            return null;
        }

        try {
            int id = Integer.parseInt(kolom[0]);
            Pelanggan p = new Pelanggan(kolom[2], kolom[3]);
            double biayaJasa = Double.parseDouble(kolom[6]);

            Servis s;
            if (kolom[1].equals("Hardware")) {
                double biayaSparepart = Double.parseDouble(kolom[7]);
                s = new ServisHardware(id, p, kolom[4], kolom[5], biayaJasa, biayaSparepart);
            } else if (kolom[1].equals("Software")) {
                boolean instalUlang = Boolean.parseBoolean(kolom[7]);
                s = new ServisSoftware(id, p, kolom[4], kolom[5], biayaJasa, instalUlang);
            } else {
                return null;
            }

            s.setStatus(kolom[8]);
            return s;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Titik koma di teks diganti koma, supaya kolom CSV tidak bergeser
    private static String bersihkan(String teks) {
        return teks.replace(";", ",");
    }
}
