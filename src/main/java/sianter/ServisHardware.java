package sianter;

// Modul 6 (OOP II): inheritance dari Servis.
public class ServisHardware extends Servis {

    private double biayaSparepart;

    public ServisHardware(int id, Pelanggan pelanggan, String perangkat, String keluhan,
                          double biayaJasa, double biayaSparepart) {
        super(id, pelanggan, perangkat, keluhan, biayaJasa);
        // TODO: simpan biayaSparepart
    }

    @Override
    public double hitungBiaya() {
        // TODO: biaya total = biayaJasa + biayaSparepart
        return 0;
    }

    @Override
    public String getJenis() {
        return "Hardware";
    }

    // TODO: getter dan setter biayaSparepart (diisi teknisi waktu servis selesai)
}
