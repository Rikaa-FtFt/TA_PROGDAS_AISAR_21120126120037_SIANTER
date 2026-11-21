package sianter;

// Modul 7: Stack buatan sendiri berbasis array, untuk tombol Undo.
// Aksi terakhir ada di paling atas (LIFO), jadi yang di-undo selalu yang terbaru.
public class RiwayatAksi {

    private Aksi[] data;
    private int top;

    public RiwayatAksi(int kapasitasAwal) {
        data = new Aksi[kapasitasAwal];
        top = -1;
    }

    public boolean isEmpty() {
        // TODO
        return false;
    }

    public void push(Aksi a) {
        // TODO: kalau penuh, perbesar array dulu
    }

    public Aksi pop() {
        // TODO: kalau kosong return null
        return null;
    }

    public Aksi peek() {
        // TODO
        return null;
    }
}
