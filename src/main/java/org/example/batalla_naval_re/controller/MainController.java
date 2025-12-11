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

/**
 * Controlador para la pantalla de menú principal de la aplicación.
 * <p>
 * Gestiona la navegación inicial, permitiendo crear nuevas partidas,
 * continuar partidas guardadas o acceder a modos de prueba.
 * </p>
 */
public class MainController {

    /**
     * Campo de texto para ingresar el apodo del jugador.
     */
    @FXML private TextField nicknameField;

    /**
     * Maneja el evento de creación de una nueva partida.
     * <p>
     * Obtiene el nombre del jugador (o usa "Jugador" por defecto), inicializa
     * un nuevo estado de juego, lo guarda y transiciona a la pantalla de juego.
     * </p>
     *
     * @param event El evento disparado por el botón "Nuevo Juego".
     */
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

    /**
     * Maneja el evento de continuar una partida existente.
     * <p>
     * Intenta cargar el último estado guardado. Si no existe, crea una nueva partida.
     * Finalmente transiciona a la pantalla de juego.
     * </p>
     *
     * @param event El evento disparado por el botón "Continuar".
     */
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

    /**
     * Maneja el evento para mostrar el tablero de la máquina (Modo Verificador).
     * <p>
     * Carga o crea un estado de juego bajo el nombre "Verificador".
     * </p>
     *
     * @param event El evento disparado por el botón correspondiente.
     */
    @FXML
    protected void onShowMachineBoard(ActionEvent event) {
        try {
            GameState state = SaveManager.loadLastState();
            if(state == null) state = GameState.newGame("Verificador");
            openGameScene(event, state); // Quitamos el parámetro boolean
        } catch (Exception e) {
            showError("Error al mostrar tablero de la máquina", e);
        }
    }

    /**
     * Método auxiliar para cargar la escena del juego (game.fxml).
     * <p>
     * Carga el archivo FXML, obtiene el controlador {@link GameController},
     * inicializa el estado del juego en él y cambia la escena actual.
     * </p>
     *
     * @param event El evento que originó el cambio de escena (usado para obtener el Stage).
     * @param state El estado del juego a cargar.
     * @throws IOException Si no se encuentra el archivo FXML.
     */
    private void openGameScene(ActionEvent event, GameState state) throws IOException {
        // Buscar el archivo FXML en resources
        URL fxmlUrl = findResource("game.fxml");
        if (fxmlUrl == null) {
            throw new IOException("Archivo game.fxml no encontrado. Debe estar en src/main/resources/");
        }

        // Cargar la interfaz
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        AnchorPane root = loader.load();
        // Llamada CORRECTA a initState
        GameController controller = loader.getController();
        controller.initState(state);

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
     * Busca un recurso (FXML, CSS, imagen) en varias rutas comunes dentro del classpath.
     *
     * @param filename El nombre del archivo a buscar.
     * @return La URL del recurso si se encuentra, o {@code null} si no existe.
     */
    private URL findResource(String filename) {
        String[] possiblePaths = {
                "/" + filename,
                "/vista/" + filename,
                "/fxml/" + filename,
                "/view/" + filename,
                filename
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
     * Muestra una alerta de error al usuario.
     *
     * @param title Título del mensaje de error.
     * @param e     La excepción que causó el error.
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