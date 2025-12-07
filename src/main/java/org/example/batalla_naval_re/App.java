package org.example.batalla_naval_re;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // 1. OBTENER URL del archivo FXML
        // IMPORTANTE: Usar "/" al inicio para buscar desde la raíz de resources
        URL fxmlUrl = App.class.getResource("/main.fxml");

        // Verificar que se encontró
        if (fxmlUrl == null) {
            System.err.println("ERROR: No se encontró /main.fxml en src/main/resources/");
            throw new RuntimeException("Archivo FXML no encontrado");
        }

        System.out.println("FXML encontrado en: " + fxmlUrl);

        // 2. CARGAR FXML usando la URL
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load());

        // 3. OPCIONAL: Cargar CSS si existe
        URL cssUrl = App.class.getResource("/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        // 4. CONFIGURAR VENTANA
        stage.setTitle("Batalla Naval - JavaFX");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}