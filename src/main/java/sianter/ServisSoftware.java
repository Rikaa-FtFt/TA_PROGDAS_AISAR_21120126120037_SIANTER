package sianter;

// Modul 6 (OOP II): inheritance dari Servis.
public class ServisSoftware extends Servis {

    public static final double BIAYA_INSTAL_ULANG = 50000;

    private boolean instalUlang;

    public ServisSoftware(int id, Pelanggan pelanggan, String perangkat, String keluhan,
                          double biayaJasa, boolean instalUlang) {
        super(id, pelanggan, perangkat, keluhan, biayaJasa);
        this.instalUlang = instalUlang;
    }

    @Override
    public double hitungBiaya() {
        if (instalUlang) {
            return biayaJasa + BIAYA_INSTAL_ULANG;
        } else {
            return biayaJasa;
        }
    }

    @Override
    public String getJenis() {
        return "Software";
    }

    public boolean isInstalUlang() { return instalUlang; }
}
