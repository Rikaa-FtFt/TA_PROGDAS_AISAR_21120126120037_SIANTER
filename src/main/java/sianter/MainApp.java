package sianter;

import java.util.ArrayList;
import java.util.Locale;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Modul 8 (GUI): tampilan SIANTER pakai JavaFX.
// Kiri : form untuk mendaftarkan servis baru.
// Kanan: tabel antrean, servis yang sedang dikerjakan, tombol Panggil/Selesai/Undo,
//        dan tabel riwayat servis yang sudah selesai.
public class MainApp extends Application {

    // ===== Data =====
    private Antrean antrean;                              // Modul 7: queue
    private ArrayList<Servis> riwayat;                    // servis yang sudah selesai
    private RiwayatAksi riwayatAksi = new RiwayatAksi(10); // Modul 7: stack untuk undo
    private Servis sedangDiproses = null;                 // satu teknisi, jadi cuma satu yang dikerjakan
    private int nextId = 1;                               // id untuk servis berikutnya

    // ===== Komponen form =====
    private TextField tfNama = new TextField();
    private TextField tfNoHp = new TextField();
    private TextField tfPerangkat = new TextField();
    private TextField tfKeluhan = new TextField();
    private ComboBox<String> cbJenis = new ComboBox<>();
    private TextField tfBiayaJasa = new TextField();
    private TextField tfSparepart = new TextField();
    private CheckBox chkInstalUlang = new CheckBox("Instal ulang (+Rp 50.000)");

    // ===== Komponen tampilan =====
    private TableView<Servis> tabelAntrean;
    private TableView<Servis> tabelRiwayat;
    private Label lblDiproses = new Label();
    private Label lblPesan = new Label();
    private ToggleButton tglGelap = new ToggleButton("Mode Gelap");
    private BorderPane root = new BorderPane();

    // Tema bawaan JavaFX (Modena) menghitung semua warna dari beberapa warna dasar ini.
    // Cukup ganti warna dasarnya, tombol/tabel/teks ikut berubah sendiri.
    private static final String TEMA_GELAP =
            "-fx-base: #3a3d41;"                          // warna tombol, header tabel
            + "-fx-background: #26282b;"                  // latar jendela
            + "-fx-control-inner-background: #2f3236;"    // isi text field dan tabel
            + "-fx-control-inner-background-alt: #34373b;" // baris tabel selang-seling
            + "-fx-accent: #3d7bd9;"                      // baris yang dipilih
            + "-fx-selection-bar-non-focused: #3b4654;"   // baris dipilih waktu tabel tidak aktif
            + "-fx-focus-color: #5b9bf0;"                 // garis fokus
            + "-fx-faint-focus-color: transparent;";

    // Warna teks contoh (prompt) diatur di tiap text field, jadi harus dipasang satu per satu
    private static final String PROMPT_GELAP = "-fx-prompt-text-fill: #80868f;";

    @Override
    public void start(Stage stage) {
        // Muat data lama dari file CSV (kalau ada)
        antrean = Penyimpanan.muatAntrean();
        riwayat = Penyimpanan.muatRiwayat();
        hitungNextId();

        tglGelap.setOnAction(e -> aturTema());

        // Baris bawah: pesan di kiri, tombol mode gelap di kanan
        Region pengisi = new Region();
        HBox.setHgrow(pengisi, Priority.ALWAYS);
        HBox barisBawah = new HBox(8, lblPesan, pengisi, tglGelap);
        barisBawah.setAlignment(Pos.CENTER_LEFT);

        root.setPadding(new Insets(12));
        root.setLeft(buatForm());
        root.setCenter(buatPanelKanan());
        root.setBottom(barisBawah);
        BorderPane.setMargin(barisBawah, new Insets(8, 0, 0, 0));

        refresh();
        pesan("Siap. Ada " + antrean.size() + " servis di antrean.");

        stage.setTitle("SIANTER - Sistem Antrean Servis Komputer");
        stage.setScene(new Scene(root, 1150, 650));
        stage.show();
    }

    // Dipanggil otomatis oleh JavaFX waktu jendela ditutup: simpan data ke CSV
    @Override
    public void stop() {
        Penyimpanan.simpanAntrean(antrean);
        Penyimpanan.simpanRiwayat(riwayat);
    }

    // ================= Bagian tampilan =================

    private VBox buatForm() {
        cbJenis.getItems().addAll("Hardware", "Software");
        cbJenis.setValue("Hardware");
        cbJenis.setOnAction(e -> aturFieldJenis());

        tfNama.setPromptText("Budi");
        tfNoHp.setPromptText("08123456789");
        tfPerangkat.setPromptText("Laptop Asus A409");
        tfKeluhan.setPromptText("Tidak mau menyala");
        tfBiayaJasa.setPromptText("100.000");
        tfSparepart.setPromptText("0");
        pasangFormatRibuan(tfBiayaJasa);
        pasangFormatRibuan(tfSparepart);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.addRow(0, new Label("Nama"), tfNama);
        grid.addRow(1, new Label("No HP"), tfNoHp);
        grid.addRow(2, new Label("Perangkat"), tfPerangkat);
        grid.addRow(3, new Label("Keluhan"), tfKeluhan);
        grid.addRow(4, new Label("Jenis"), cbJenis);
        grid.addRow(5, new Label("Biaya jasa"), tfBiayaJasa);
        grid.addRow(6, new Label("Sparepart"), tfSparepart);
        grid.add(chkInstalUlang, 1, 7);

        Button btnTambah = new Button("Tambah ke Antrean");
        btnTambah.setMaxWidth(Double.MAX_VALUE);
        btnTambah.setOnAction(e -> tambah());

        Label judul = new Label("Servis Baru");
        judul.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        aturFieldJenis();

        VBox form = new VBox(12, judul, grid, btnTambah);
        form.setPadding(new Insets(0, 16, 0, 0));
        form.setPrefWidth(300);
        return form;
    }

    private VBox buatPanelKanan() {
        tabelAntrean = buatTabel("Antrean kosong");
        tabelRiwayat = buatTabel("Belum ada servis yang selesai");

        Button btnPanggil = new Button("Panggil");
        Button btnSelesai = new Button("Selesai");
        Button btnUndo = new Button("Undo");
        btnPanggil.setOnAction(e -> panggil());
        btnSelesai.setOnAction(e -> selesai());
        btnUndo.setOnAction(e -> undo());

        lblDiproses.setStyle("-fx-font-size: 14px;");
        HBox barisTombol = new HBox(8, btnPanggil, btnSelesai, btnUndo);

        Label judulAntrean = new Label("Antrean (paling atas = dipanggil duluan)");
        Label judulDiproses = new Label("Sedang Diproses");
        Label judulRiwayat = new Label("Riwayat Selesai");
        judulAntrean.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        judulDiproses.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        judulRiwayat.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox.setVgrow(tabelAntrean, Priority.ALWAYS);
        VBox.setVgrow(tabelRiwayat, Priority.ALWAYS);

        return new VBox(8, judulAntrean, tabelAntrean,
                judulDiproses, lblDiproses, barisTombol,
                judulRiwayat, tabelRiwayat);
    }

    // Tabel antrean dan tabel riwayat kolomnya sama, jadi dibuat oleh satu method.
    // c.getValue() = objek Servis di baris itu, lalu diambil teks yang mau ditampilkan.
    private TableView<Servis> buatTabel(String teksKosong) {
        TableView<Servis> tabel = new TableView<>();
        tabel.setPlaceholder(new Label(teksKosong));
        tabel.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Servis, String> kId = new TableColumn<>("ID");
        kId.setCellValueFactory(c -> new SimpleStringProperty("#" + c.getValue().getId()));
        kId.setMaxWidth(50);

        TableColumn<Servis, String> kNama = new TableColumn<>("Nama");
        kNama.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPelanggan().getNama()));

        TableColumn<Servis, String> kNoHp = new TableColumn<>("No HP");
        kNoHp.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPelanggan().getNoHp()));

        TableColumn<Servis, String> kPerangkat = new TableColumn<>("Perangkat");
        kPerangkat.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPerangkat()));

        TableColumn<Servis, String> kKeluhan = new TableColumn<>("Keluhan");
        kKeluhan.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKeluhan()));

        TableColumn<Servis, String> kJenis = new TableColumn<>("Jenis");
        kJenis.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getJenis()));

        TableColumn<Servis, String> kBiaya = new TableColumn<>("Total Biaya");
        kBiaya.setCellValueFactory(c -> new SimpleStringProperty(rupiah(c.getValue().hitungBiaya())));

        TableColumn<Servis, String> kStatus = new TableColumn<>("Status");
        kStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));

        tabel.getColumns().add(kId);
        tabel.getColumns().add(kNama);
        tabel.getColumns().add(kNoHp);
        tabel.getColumns().add(kPerangkat);
        tabel.getColumns().add(kKeluhan);
        tabel.getColumns().add(kJenis);
        tabel.getColumns().add(kBiaya);
        tabel.getColumns().add(kStatus);
        return tabel;
    }

    // Hardware pakai biaya sparepart, Software pakai pilihan instal ulang
    private void aturFieldJenis() {
        boolean hardware = cbJenis.getValue().equals("Hardware");
        tfSparepart.setDisable(!hardware);
        chkInstalUlang.setDisable(hardware);
    }

    // Isi ulang kedua tabel dan label "Sedang Diproses" dari data terbaru
    private void refresh() {
        tabelAntrean.getItems().clear();
        for (int i = 0; i < antrean.size(); i++) {
            tabelAntrean.getItems().add(antrean.get(i));
        }

        tabelRiwayat.getItems().clear();
        for (int i = 0; i < riwayat.size(); i++) {
            tabelRiwayat.getItems().add(riwayat.get(i));
        }

        if (sedangDiproses == null) {
            lblDiproses.setText("(kosong) - tekan Panggil untuk mengambil antrean paling depan");
        } else {
            lblDiproses.setText("#" + sedangDiproses.getId() + "  "
                    + sedangDiproses.getPelanggan().getNama() + " - "
                    + sedangDiproses.getPerangkat() + " ("
                    + sedangDiproses.getJenis() + ", "
                    + rupiah(sedangDiproses.hitungBiaya()) + ")");
        }
    }

    // ================= Logika tombol =================

    private void tambah() {
        String nama = tfNama.getText().trim();
        String noHp = tfNoHp.getText().trim();
        String perangkat = tfPerangkat.getText().trim();
        String keluhan = tfKeluhan.getText().trim();

        if (nama.isEmpty() || noHp.isEmpty() || perangkat.isEmpty() || keluhan.isEmpty()) {
            pesan("Nama, No HP, perangkat, dan keluhan wajib diisi.");
            return;
        }

        // Field biaya cuma bisa diisi angka (lihat pasangFormatRibuan), jadi tinggal cek kosong
        if (tfBiayaJasa.getText().isEmpty()) {
            pesan("Biaya jasa wajib diisi.");
            return;
        }

        double biayaJasa = ambilAngka(tfBiayaJasa);
        double biayaSparepart = 0;
        if (cbJenis.getValue().equals("Hardware") && !tfSparepart.getText().isEmpty()) {
            biayaSparepart = ambilAngka(tfSparepart);
        }

        Pelanggan p = new Pelanggan(nama, noHp);
        Servis s;
        if (cbJenis.getValue().equals("Hardware")) {
            s = new ServisHardware(nextId, p, perangkat, keluhan, biayaJasa, biayaSparepart);
        } else {
            s = new ServisSoftware(nextId, p, perangkat, keluhan, biayaJasa, chkInstalUlang.isSelected());
        }
        nextId++;

        antrean.enqueue(s);
        riwayatAksi.push(new Aksi(Aksi.TAMBAH, s));

        kosongkanForm();
        refresh();
        pesan("Servis #" + s.getId() + " (" + nama + ") masuk antrean.");
    }

    private void panggil() {
        if (sedangDiproses != null) {
            pesan("Selesaikan dulu servis #" + sedangDiproses.getId() + " sebelum memanggil yang baru.");
            return;
        }
        if (antrean.isEmpty()) {
            pesan("Antrean kosong, tidak ada yang bisa dipanggil.");
            return;
        }

        sedangDiproses = antrean.dequeue();
        sedangDiproses.setStatus("Diproses");
        riwayatAksi.push(new Aksi(Aksi.PANGGIL, sedangDiproses));

        refresh();
        pesan("Servis #" + sedangDiproses.getId() + " dipanggil dan mulai dikerjakan.");
    }

    private void selesai() {
        if (sedangDiproses == null) {
            pesan("Belum ada servis yang sedang dikerjakan.");
            return;
        }

        Servis s = sedangDiproses;
        s.setStatus("Selesai");
        riwayat.add(s);
        riwayatAksi.push(new Aksi(Aksi.SELESAI, s));
        sedangDiproses = null;

        refresh();
        pesan("Servis #" + s.getId() + " selesai. Total biaya " + rupiah(s.hitungBiaya()) + ".");
    }

    // Ambil aksi terakhir dari stack, lalu jalankan kebalikannya
    private void undo() {
        if (riwayatAksi.isEmpty()) {
            pesan("Tidak ada aksi yang bisa di-undo.");
            return;
        }

        Aksi a = riwayatAksi.pop();
        Servis s = a.getServis();

        if (a.getJenis().equals(Aksi.TAMBAH)) {
            // Servis yang terakhir ditambah pasti ada di paling belakang antrean
            antrean.hapusBelakang();
            nextId--;
            pesan("Undo tambah: servis #" + s.getId() + " dihapus dari antrean.");
        } else if (a.getJenis().equals(Aksi.PANGGIL)) {
            // Servis balik ke posisi paling depan antrean
            sedangDiproses = null;
            s.setStatus("Menunggu");
            antrean.kembalikanKeDepan(s);
            pesan("Undo panggil: servis #" + s.getId() + " kembali ke depan antrean.");
        } else if (a.getJenis().equals(Aksi.SELESAI)) {
            // Servis keluar dari riwayat dan dikerjakan lagi
            riwayat.remove(riwayat.size() - 1);
            s.setStatus("Diproses");
            sedangDiproses = s;
            pesan("Undo selesai: servis #" + s.getId() + " kembali diproses.");
        }

        refresh();
    }

    // ================= Method bantu =================

    // Cari id terbesar dari data yang dimuat, supaya id baru tidak dobel
    private void hitungNextId() {
        int max = 0;
        for (int i = 0; i < antrean.size(); i++) {
            if (antrean.get(i).getId() > max) {
                max = antrean.get(i).getId();
            }
        }
        for (int i = 0; i < riwayat.size(); i++) {
            if (riwayat.get(i).getId() > max) {
                max = riwayat.get(i).getId();
            }
        }
        nextId = max + 1;
    }

    private void kosongkanForm() {
        tfNama.clear();
        tfNoHp.clear();
        tfPerangkat.clear();
        tfKeluhan.clear();
        tfBiayaJasa.clear();
        tfSparepart.clear();
        chkInstalUlang.setSelected(false);
        tfNama.requestFocus();
    }

    // Tombol Mode Gelap ditekan: pasang atau lepas warna tema gelap
    private void aturTema() {
        TextField[] semuaField = {tfNama, tfNoHp, tfPerangkat, tfKeluhan, tfBiayaJasa, tfSparepart};

        if (tglGelap.isSelected()) {
            root.setStyle(TEMA_GELAP);
            for (int i = 0; i < semuaField.length; i++) {
                semuaField[i].setStyle(PROMPT_GELAP);
            }
        } else {
            root.setStyle("");
            for (int i = 0; i < semuaField.length; i++) {
                semuaField[i].setStyle("");
            }
        }
    }

    private void pesan(String teks) {
        lblPesan.setText(teks);
    }

    // 900000 -> "900.000". Locale Indonesia supaya pemisahnya titik di laptop mana pun.
    private String ribuan(long angka) {
        return String.format(Locale.forLanguageTag("id-ID"), "%,d", angka);
    }

    private String rupiah(double angka) {
        return "Rp " + ribuan(Math.round(angka));
    }

    // "900.000" -> 900000.0 (titiknya dibuang dulu baru diubah jadi angka)
    private double ambilAngka(TextField tf) {
        return Double.parseDouble(tf.getText().replace(".", ""));
    }

    // Setiap isi field berubah, ambil digitnya saja lalu tulis ulang dengan titik ribuan.
    // Huruf dan tanda minus otomatis terbuang, jadi biaya pasti angka dan tidak negatif.
    private void pasangFormatRibuan(TextField tf) {
        tf.setTextFormatter(new TextFormatter<String>(perubahan -> {
            if (!perubahan.isContentChange()) {
                return perubahan;   // cuma geser kursor / blok teks, biarkan
            }
            String digit = perubahan.getControlNewText().replaceAll("[^0-9]", "");
            if (digit.length() > 12) {
                return null;   // tolak ketikan, maksimal 999.999.999.999
            }
            String rapi = "";
            if (!digit.isEmpty()) {
                rapi = ribuan(Long.parseLong(digit));
            }
            perubahan.setRange(0, perubahan.getControlText().length());
            perubahan.setText(rapi);
            perubahan.setCaretPosition(rapi.length());
            perubahan.setAnchor(rapi.length());
            return perubahan;
        }));
    }
}
