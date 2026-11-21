package sianter;

// Modul 6 (OOP II): abstract class. Tidak bisa dibuat langsung,
// harus lewat ServisHardware atau ServisSoftware.
public abstract class Servis {

    protected int id;
    protected Pelanggan pelanggan;
    protected String perangkat;   // contoh: "Laptop Asus A409"
    protected String keluhan;     // contoh: "Tidak mau menyala"
    protected String status;      // "Menunggu", "Diproses", atau "Selesai"
    protected double biayaJasa;

    public Servis(int id, Pelanggan pelanggan, String perangkat, String keluhan, double biayaJasa) {
        // TODO: isi semua atribut dari parameter, status awal = "Menunggu"
    }

    // Tiap jenis servis menghitung biayanya sendiri (polymorphism)
    public abstract double hitungBiaya();

    // "Hardware" atau "Software", dipakai untuk tampilan tabel
    public abstract String getJenis();

    // TODO: getter untuk id, pelanggan, perangkat, keluhan, status, biayaJasa
    // TODO: setter untuk status
}
