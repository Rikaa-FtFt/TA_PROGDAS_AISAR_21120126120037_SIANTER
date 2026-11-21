package sianter;

import javafx.application.Application;

// Titik masuk program. Dipisah dari MainApp supaya tombol Run di IntelliJ
// tidak kena error "JavaFX runtime components are missing".
public class Launcher {
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
