package sianter;

// Satu catatan aksi untuk fitur undo: apa yang dilakukan dan ke servis yang mana.
public class Aksi {

    public static final String TAMBAH = "TAMBAH";
    public static final String PANGGIL = "PANGGIL";
    public static final String SELESAI = "SELESAI";

    private final String jenis;
    private final Servis servis;

    public Aksi(String jenis, Servis servis) {
        this.jenis = jenis;
        this.servis = servis;
    }

    public String getJenis() {
        return jenis;
    }

    public Servis getServis() {
        return servis;
    }
}
