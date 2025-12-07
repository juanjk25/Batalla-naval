package org.example.batalla_naval_re.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.batalla_naval_re.model.GameState;
import org.example.batalla_naval_re.persistence.SaveManager;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML private TextField nicknameField;

    @FXML
    protected void onNewGame(ActionEvent event) {
        try {
            String nick = nicknameField.getText().isBlank() ? "Jugador" : nicknameField.getText().trim();
            GameState state = GameState.newGame(nick);
            SaveManager.saveState(state);
            openGameScene(event, state);
        } catch (Exception e) {
            showError("Error al crear nueva partida", e);
        }
    }

    @FXML
    protected void onContinue(ActionEvent event) {
        try {
            GameState state = SaveManager.loadLastState();
            if(state == null) {
                state = GameState.newGame("Jugador");
            }
            openGameScene(event, state);
        } catch (Exception e) {
            showError("Error al continuar partida", e);
        }
    }

    @FXML
    protected void onShowMachineBoard(ActionEvent event) {
        try {
            GameState state = SaveManager.loadLastState();
            if(state == null) state = GameState.newGame("Verificador");
            openGameScene(event, state, true);
        } catch (Exception e) {
            showError("Error al mostrar tablero de la máquina", e);
        }
    }

    private void openGameScene(ActionEvent event, GameState state) throws IOException {
        openGameScene(event, state, false);
    }

    private void openGameScene(ActionEvent event, GameState state, boolean revealMachine) throws IOException {
        // Buscar el archivo FXML en resources
        URL fxmlUrl = findResource("game.fxml");
        if (fxmlUrl == null) {
            throw new IOException("Archivo game.fxml no encontrado. Debe estar en src/main/resources/");
        }

        // Cargar la interfaz
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        AnchorPane root = loader.load();
        GameController controller = loader.getController();
        controller.initState(state, revealMachine);

        // Crear escena
        Scene scene = new Scene(root);

        // Cargar CSS si existe
        URL cssUrl = findResource("styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        // Cambiar ventana
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.show();
    }

    /**
     * Método auxiliar para buscar recursos en el classpath
     */
    private URL findResource(String filename) {
        // Intentar diferentes ubicaciones comunes
        String[] possiblePaths = {
                "/" + filename,           // Raíz de resources
                "/vista/" + filename,     // En carpeta vista
                "/fxml/" + filename,      // En carpeta fxml
                "/view/" + filename,      // En carpeta view
                filename                  // Ruta relativa
        };

        for (String path : possiblePaths) {
            URL url = getClass().getResource(path);
            if (url != null) {
                System.out.println("Recurso encontrado: " + path + " -> " + url);
                return url;
            }
        }

        System.err.println("Recurso NO encontrado: " + filename);
        return null;
    }

    /**
     * Método para mostrar errores al usuario
     */
    private void showError(String title, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }
}