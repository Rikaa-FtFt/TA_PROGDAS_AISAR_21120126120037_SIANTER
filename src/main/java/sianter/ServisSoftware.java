package sianter;

// Modul 6 (OOP II): inheritance dari Servis.
public class ServisSoftware extends Servis {

    public static final double BIAYA_INSTAL_ULANG = 50000;

    private boolean instalUlang;

    public ServisSoftware(int id, Pelanggan pelanggan, String perangkat, String keluhan,
                          double biayaJasa, boolean instalUlang) {
        super(id, pelanggan, perangkat, keluhan, biayaJasa);
        // TODO: simpan instalUlang
    }

    @Override
    public double hitungBiaya() {
        // TODO: kalau instalUlang -> biayaJasa + BIAYA_INSTAL_ULANG,
        //       kalau tidak -> biayaJasa saja (Modul 2)
        return 0;
    }

    @Override
    public String getJenis() {
        return "Software";
    }

    // TODO: getter isInstalUlang()
}
