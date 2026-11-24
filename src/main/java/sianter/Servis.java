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
        this.id = id;
        this.pelanggan = pelanggan;
        this.perangkat = perangkat;
        this.keluhan = keluhan;
        this.biayaJasa = biayaJasa;
        this.status = "Menunggu";
    }

    // Tiap jenis servis menghitung biayanya sendiri (polymorphism)
    public abstract double hitungBiaya();

    // "Hardware" atau "Software", dipakai untuk tampilan tabel
    public abstract String getJenis();

    public int getId() { return id; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public String getPerangkat() { return perangkat; }
    public String getKeluhan() { return keluhan; }
    public String getStatus() { return status; }
    public double getBiayaJasa() { return biayaJasa; }

    public void setStatus(String status) { this.status = status; }
}
