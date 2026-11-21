package sianter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// Modul 8 (GUI). Sementara cuma buka jendela kosong untuk tes setup.
// Form, tabel antrean, dan tombol-tombolnya diisi nanti.
public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("SIANTER - setup jalan");
        StackPane root = new StackPane(label);

        stage.setTitle("SIANTER");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }
}
