package org.example.batalla_naval_re.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import org.example.batalla_naval_re.ai.SimpleAI;
import org.example.batalla_naval_re.model.*;
import org.example.batalla_naval_re.persistence.GameFileException;
import org.example.batalla_naval_re.persistence.SaveManager;

import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controlador principal de la vista del juego (game.fxml).
 * <p>
 * Gestiona la lógica de interacción entre el usuario y el modelo de datos durante la partida.
 * Se encarga de renderizar los tableros, gestionar la fase de colocación de barcos,
 * coordinar los turnos de disparo (Jugador vs IA) y manejar la persistencia del juego.
 * </p>
 */
public class GameController {

    @FXML private GridPane playerGrid;
    @FXML private GridPane machineGrid;

    @FXML private Label lblNickname;
    @FXML private Label lblStatus;
    @FXML private Label lblSunkCount;

    @FXML private Button btnBack;
    @FXML private Button btnSave;
    @FXML private Button btnLoad;

    /**
     * Estado actual del juego que contiene a los jugadores y tableros.
     */
    private GameState state;

    /**
     * Instancia de la Inteligencia Artificial simple para el oponente.
     */
    private SimpleAI ai;

    /**
     * Barco actualmente seleccionado para ser colocado en el tablero.
     */
    private Ship selectedShip;

    /**
     * Define la orientación de colocación del barco (true = horizontal, false = vertical).
     */
    private boolean horizontalPlacement = true;

    // ✅ Iconos
    private final Image hitImage =
            new Image(getClass().getResourceAsStream("/impacto.jpg"));
    private final Image sunkImage =
            new Image(getClass().getResourceAsStream("/undido.jpg"));

    // ------------------------------------------------------
    // INIT
    // ------------------------------------------------------

    /**
     * Inicializa el estado del juego recibido desde el controlador principal.
     * <p>
     * Configura la IA, actualiza las etiquetas de la interfaz, posiciona aleatoriamente
     * los barcos de la máquina y prepara el tablero del jugador para la fase de colocación.
     * </p>
     *
     * @param state El objeto {@link GameState} que contiene la información de la partida.
     */
    public void initState(GameState state) {
        this.state = state;
        this.ai = new SimpleAI(state.getMachineBoard());

        lblNickname.setText(state.getPlayer().getName());
        lblStatus.setText("Coloca tus barcos | Click derecho = rotar");
        lblSunkCount.setText("0");

        state.getMachineBoard().randomPlaceAllShips();
        state.getPlayer().getBoard().createShipsWithoutPlacement();

        machineGrid.setDisable(true);

        renderBoards();
        enableShipSelection();
    }

    // ------------------------------------------------------
    // RENDER
    // ------------------------------------------------------

    /**
     * Limpia y redibuja completamente ambos tableros (Jugador y Máquina).
     * <p>
     * Se llama cada vez que ocurre un cambio en el estado del juego (disparo, colocación, carga).
     * </p>
     */
    private void renderBoards() {
        playerGrid.getChildren().clear();
        machineGrid.getChildren().clear();

        drawHeaders(playerGrid);
        drawHeaders(machineGrid);

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                playerGrid.add(createPlayerCell(r, c), c + 1, r + 1);
                machineGrid.add(createMachineCell(r, c), c + 1, r + 1);
            }
        }
    }

    // ------------------------------------------------------
    // HEADERS A–J / 1–10
    // ------------------------------------------------------

    /**
     * Dibuja los encabezados de filas (1-10) y columnas (A-J) en el grid dado.
     *
     * @param grid El {@link GridPane} donde se añadirán las etiquetas.
     */
    private void drawHeaders(GridPane grid) {

        for (int c = 0; c < Board.SIZE; c++) {
            Label lbl = new Label(String.valueOf((char) ('A' + c)));
            lbl.setStyle("-fx-font-weight: bold;");
            grid.add(lbl, c + 1, 0);
        }

        for (int r = 0; r < Board.SIZE; r++) {
            Label lbl = new Label(String.valueOf(r + 1));
            lbl.setStyle("-fx-font-weight: bold;");
            grid.add(lbl, 0, r + 1);
        }
    }

    // ------------------------------------------------------
    // PLAYER CELL
    // ------------------------------------------------------

    /**
     * Crea la representación visual de una celda en el tablero del jugador.
     * <p>
     * Configura los eventos del mouse para la fase de colocación:
     * <ul>
     *     <li>Clic derecho: Rota la orientación del barco.</li>
     *     <li>Clic izquierdo: Intenta colocar el barco seleccionado.</li>
     * </ul>
     * </p>
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return Un {@link StackPane} que representa la celda gráfica.
     */
    private StackPane createPlayerCell(int row, int col) {
        Cell cell = state.getPlayer().getBoard().getCell(row, col);
        StackPane pane = baseCell();

        if (cell.isShip()) showShipIcon(pane, cell.getShip());
        paintCellState(pane, cell);

        pane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                horizontalPlacement = !horizontalPlacement;
            } else if (selectedShip != null) {
                attemptPlaceShip(row, col);
            }
        });

        return pane;
    }

    // ------------------------------------------------------
    // MACHINE CELL
    // ------------------------------------------------------

    /**
     * Crea la representación visual de una celda en el tablero de la máquina.
     * <p>
     * Configura el evento de clic izquierdo para realizar un disparo contra el oponente.
     * </p>
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return Un {@link StackPane} que representa la celda gráfica.
     */
    private StackPane createMachineCell(int row, int col) {
        Cell cell = state.getMachineBoard().getCell(row, col);
        StackPane pane = baseCell();

        pane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                onPlayerShot(row, col);
            }
        });

        paintCellState(pane, cell);
        return pane;
    }

    // ------------------------------------------------------
    // CELL PAINT
    // ------------------------------------------------------

    /**
     * Pinta el estado actual de la celda (Hundido, Impacto o Agua) sobre el panel.
     *
     * @param pane El panel gráfico de la celda.
     * @param cell El objeto lógico de la celda.
     */
    private void paintCellState(StackPane pane, Cell cell) {
        if (cell.isSunkPart()) drawSunkIcon(pane);
        else if (cell.isHit()) drawHitIcon(pane);
        else if (cell.isMiss()) drawMiss(pane);
    }

    /**
     * Crea el estilo base (fondo azul) para una celda del tablero.
     * @return Un nuevo StackPane configurado.
     */
    private StackPane baseCell() {
        StackPane pane = new StackPane();
        Rectangle r = new Rectangle(40, 40);
        r.setFill(Color.LIGHTBLUE);
        r.setStroke(Color.DARKBLUE);
        pane.getChildren().add(r);
        return pane;
    }

    // ------------------------------------------------------
    // ICONS
    // ------------------------------------------------------

    /**
     * Muestra el icono correspondiente al tipo de barco en la celda.
     */
    private void showShipIcon(StackPane pane, Ship ship) {
        Image img = new Image(getClass().getResourceAsStream(
                ship.getType().getImagePath()));
        ImageView iv = new ImageView(img);
        iv.setFitWidth(40);
        iv.setFitHeight(40);
        pane.getChildren().add(iv);
    }

    /**
     * Dibuja el icono de "Impacto" (fuego/explosión).
     */
    private void drawHitIcon(StackPane pane) {
        ImageView iv = new ImageView(hitImage);
        iv.setFitWidth(28);
        iv.setFitHeight(28);
        pane.getChildren().add(iv);
    }

    /**
     * Dibuja el icono de "Hundido" (calavera o similar).
     */
    private void drawSunkIcon(StackPane pane) {
        ImageView iv = new ImageView(sunkImage);
        iv.setFitWidth(40);
        iv.setFitHeight(40);
        pane.getChildren().add(iv);
    }

    /**
     * Dibuja una "X" para representar "Agua" (tiro fallido).
     */
    private void drawMiss(StackPane pane) {
        Line l1 = new Line(5, 5, 35, 35);
        Line l2 = new Line(35, 5, 5, 35);
        l1.setStroke(Color.BLUE);
        l2.setStroke(Color.BLUE);
        pane.getChildren().addAll(l1, l2);
    }

    // ------------------------------------------------------
    // GAME LOGIC
    // ------------------------------------------------------

    /**
     * Busca el siguiente barco no colocado en la lista del jugador y lo selecciona.
     * Si no hay más barcos para colocar, `selectedShip` será null.
     */
    private void enableShipSelection() {
        for (Ship s : state.getPlayer().getBoard().getShips()) {
            if (s.getCells().isEmpty()) {
                selectedShip = s;
                return;
            }
        }
    }

    /**
     * Intenta colocar el barco seleccionado actualmente en la posición indicada.
     * <p>
     * Si la colocación es exitosa, selecciona el siguiente barco.
     * Si todos los barcos están colocados, habilita el tablero de la máquina para comenzar el juego.
     * </p>
     *
     * @param row Fila de inicio.
     * @param col Columna de inicio.
     */
    private void attemptPlaceShip(int row, int col) {
        if (state.getPlayer().getBoard()
                .addShip(selectedShip, row, col, horizontalPlacement)) {

            selectedShip = null;
            enableShipSelection();

            if (state.getPlayer().getBoard().allShipsPlaced()) {
                machineGrid.setDisable(false);
            }
            renderBoards();
        }
    }

    /**
     * Maneja la lógica cuando el jugador hace clic en el tablero enemigo.
     * <p>
     * Realiza el disparo y actualiza la vista. Si el jugador falla (Agua),
     * inicia el turno de la IA en un hilo separado.
     * </p>
     *
     * @param row Fila objetivo.
     * @param col Columna objetivo.
     */
    private void onPlayerShot(int row, int col) {
        Cell.ShotResult result = state.getMachineBoard().shoot(row, col);
        renderBoards();
        if (result == Cell.ShotResult.MISS) aiTurnThread();
    }

    // ------------------------------------------------------
    // THREADS
    // ------------------------------------------------------

    /**
     * Ejecuta el turno de la IA en un hilo separado (Daemon) para simular tiempo de "pensamiento".
     * <p>
     * Espera 700ms, calcula el disparo de la IA y actualiza la UI mediante {@link Platform#runLater}.
     * </p>
     */
    private void aiTurnThread() {
        Thread t = new Thread(() -> {
            try { Thread.sleep(700); } catch (InterruptedException ignored) {}
            int[] pos = ai.nextShot();
            state.getPlayer().getBoard().shoot(pos[0], pos[1]);
            Platform.runLater(this::renderBoards);
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Inicia un hilo de autoguardado que persiste el estado del juego después de 5 segundos.
     */
    private void autoSaveThread() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000);
                SaveManager.saveState(state);
            } catch (Exception ignored) {}
        });
        t.setDaemon(true);
        t.start();
    }

    // ------------------------------------------------------
    // SAVE / LOAD
    // ------------------------------------------------------

    /**
     * Manejador del botón "Guardar". Guarda el estado actual y activa el autoguardado.
     * @throws GameFileException Si ocurre un error al escribir el archivo.
     */
    @FXML
    private void onSaveGame() throws GameFileException {
        SaveManager.saveState(state);
        autoSaveThread();
    }

    /**
     * Manejador del botón "Cargar". Restaura el último estado guardado y actualiza la vista.
     * @throws GameFileException Si ocurre un error al leer el archivo.
     */
    @FXML
    private void onLoadGame() throws GameFileException {
        GameState loaded = SaveManager.loadLastState();
        if (loaded != null) {
            state = loaded;
            renderBoards();
        }
    }

    // ------------------------------------------------------
    // BACK
    // ------------------------------------------------------

    /**
     * Manejador del botón "Volver". Regresa a la pantalla principal (main.fxml).
     * @throws IOException Si falla la carga del FXML del menú principal.
     */
    @FXML
    private void onBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(new Scene(
                javafx.fxml.FXMLLoader.load(getClass()
                        .getResource("/main.fxml"))
        ));
    }
}