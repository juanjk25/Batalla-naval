package org.example.batallanaval.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.batallanaval.model.GameState;
import org.example.batallanaval.persistence.SaveManager;

import java.io.IOException;

public class MainController {

    @FXML private TextField nicknameField;

    @FXML
    protected void onNewGame(ActionEvent event) throws IOException {
        String nick = nicknameField.getText().isBlank() ? "Jugador" : nicknameField.getText().trim();
        GameState state = GameState.newGame(nick);
        SaveManager.saveState(state); // guarda inicial
        openGameScene(event, state);
    }

    @FXML
    protected void onContinue(ActionEvent event) throws IOException, ClassNotFoundException {
        GameState state = SaveManager.loadLastState();
        if(state == null) {
            // si no hay guardado, iniciar nuevo
            state = GameState.newGame("Jugador");
        }
        openGameScene(event, state);
    }

    @FXML
    protected void onShowMachineBoard(ActionEvent event) throws IOException, ClassNotFoundException {
        GameState state = SaveManager.loadLastState();
        if(state == null) state = GameState.newGame("Verificador");
        // pasar a pantalla de juego pero con bandera que muestre tablero enemigo completo
        openGameScene(event, state, true);
    }

    private void openGameScene(ActionEvent event, GameState state) throws IOException {
        openGameScene(event, state, false);
    }

    private void openGameScene(ActionEvent event, GameState state, boolean revealMachine) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/batallanaval/styles/game.fxml"));
        AnchorPane root = loader.load();
        GameController controller = loader.getController();
        controller.initState(state, revealMachine);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/batallanaval/styles/styles.css").toExternalForm());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
