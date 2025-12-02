package org.example.batallanaval;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // 👉 Diagnóstico 1: Ver si Java encuentra el FXML
        System.out.println("FXML encontrado en: " +
                App.class.getResource("/org/example/batallanaval/styles/main.fxml")
        );

        // 👉 Diagnóstico 2: Ver dónde está el classpath root REAL
        System.out.println("Classpath root: " +
                App.class.getResource("/")
        );

        // 👉 Cargar FXML
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/org/example/batallanaval/styles/main.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());

        // 👉 Cargar estilos CSS
        scene.getStylesheets().add(
                App.class.getResource("/org/example/batallanaval/styles/styles.css")
                        .toExternalForm()
        );

        // 👉 Configurar ventana
        stage.setTitle("Batalla Naval - JavaFX");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
