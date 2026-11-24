package sianter;

// Modul 6 (OOP II): inheritance dari Servis.
public class ServisHardware extends Servis {

    private double biayaSparepart;

    public ServisHardware(int id, Pelanggan pelanggan, String perangkat, String keluhan,
                          double biayaJasa, double biayaSparepart) {
        super(id, pelanggan, perangkat, keluhan, biayaJasa);
        this.biayaSparepart = biayaSparepart;
    }

    @Override
    public double hitungBiaya() {
        return biayaJasa + biayaSparepart;
    }

    @Override
    public String getJenis() {
        return "Hardware";
    }

    public double getBiayaSparepart() { return biayaSparepart; }
    public void setBiayaSparepart(double biayaSparepart) { this.biayaSparepart = biayaSparepart; }
}
