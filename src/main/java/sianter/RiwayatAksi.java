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
        return top == -1;
    }

    public void push(Aksi a) {
        if (top == data.length - 1) {
            Aksi[] baru = new Aksi[data.length * 2];
            for (int i = 0; i <= top; i++) {
                baru[i] = data[i];
            }
            data = baru;
        }
        top++;
        data[top] = a;
    }

    public Aksi pop() {
        if (isEmpty()) {
            return null;
        }
        Aksi a = data[top];
        data[top] = null;
        top--;
        return a;
    }

    public Aksi peek() {
        if (isEmpty()) {
            return null;
        }
        return data[top];
    }
}
