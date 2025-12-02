package org.example.batallanaval;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/org/example/batallanaval/fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(App.class.getResource("/org/example/batallanaval/styles/styles.css").toExternalForm());
        stage.setTitle("Batalla Naval - JavaFX");
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/org/example/batallanaval/images/icon.png"))); // opcional
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
