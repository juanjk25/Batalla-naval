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
import javafx.scene.shape.*;
import javafx.stage.Stage;

import org.example.batalla_naval_re.ai.SimpleAI;
import org.example.batalla_naval_re.model.*;
import org.example.batalla_naval_re.persistence.GameFileException;
import org.example.batalla_naval_re.persistence.SaveManager;

import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameController {

    @FXML private GridPane playerGrid;
    @FXML private GridPane machineGrid;
    @FXML private Label lblNickname;
    @FXML private Label lblStatus;
    @FXML private Label lblSunkCount;
    @FXML private Button btnBack;
    @FXML private Button btnSave;
    @FXML private Button btnLoad;

    private GameState state;
    private SimpleAI ai;

    private Ship selectedShip;
    private boolean horizontalPlacement = true;

    // ------------------------------------------------------
    // INIT
    // ------------------------------------------------------
    public void initState(GameState state) {
        this.state = state;
        this.ai = new SimpleAI(state.getMachineBoard());

        lblNickname.setText(state.getPlayer().getName());
        lblStatus.setText("Arrastra y coloca tus barcos. Click derecho = rotar.");
        lblSunkCount.setText("0");

        machineGrid.setDisable(true);

        state.getMachineBoard().randomPlaceAllShips();
        state.getPlayer().getBoard().createShipsWithoutPlacement();

        renderBoards();
        enableShipSelection();
    }

    // ------------------------------------------------------
    // RENDER
    // ------------------------------------------------------
    private void renderBoards() {
        playerGrid.getChildren().clear();
        machineGrid.getChildren().clear();

        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                playerGrid.add(createPlayerCell(r, c), c, r);
                machineGrid.add(createMachineCell(r, c), c, r);
            }
        }
    }

    // ------------------------------------------------------
    // PLAYER CELL
    // ------------------------------------------------------
    private StackPane createPlayerCell(int row, int col) {
        Cell cell = state.getPlayer().getBoard().getCell(row, col);
        StackPane pane = baseCell();

        if (cell.isShip()) showShipIcon(pane, cell.getShip());
        if (cell.isHit()) drawHit(pane);
        if (cell.isSunkPart()) drawSunk(pane);
        if (cell.isMiss()) drawMiss(pane);

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
    private StackPane createMachineCell(int row, int col) {
        Cell cell = state.getMachineBoard().getCell(row, col);
        StackPane pane = baseCell();

        pane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                onPlayerShot(row, col);
            }
        });

        if (cell.isHit()) drawHit(pane);
        if (cell.isSunkPart()) drawSunk(pane);
        if (cell.isMiss()) drawMiss(pane);

        return pane;
    }

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
    private void showShipIcon(StackPane pane, Ship ship) {
        Image img = new Image(getClass().getResourceAsStream(ship.getType().getImagePath()));
        ImageView iv = new ImageView(img);
        iv.setFitWidth(40);
        iv.setFitHeight(40);
        pane.getChildren().add(iv);
    }

    private void drawHit(StackPane pane) {
        pane.getChildren().add(new Circle(10, Color.RED));
    }

    private void drawMiss(StackPane pane) {
        Line l1 = new Line(5, 5, 35, 35);
        Line l2 = new Line(35, 5, 5, 35);
        l1.setStroke(Color.BLUE);
        l2.setStroke(Color.BLUE);
        pane.getChildren().addAll(l1, l2);
    }

    private void drawSunk(StackPane pane) {
        Rectangle r = new Rectangle(40, 40, Color.DARKRED);
        r.setOpacity(0.7);
        pane.getChildren().add(r);
    }

    // ------------------------------------------------------
    // SHIP SELECTION
    // ------------------------------------------------------
    private void enableShipSelection() {
        for (Ship s : state.getPlayer().getBoard().getShips()) {
            if (s.getCells().isEmpty()) {
                selectedShip = s;
                return;
            }
        }
    }

    private void attemptPlaceShip(int row, int col) {
        if (state.getPlayer().getBoard().addShip(selectedShip, row, col, horizontalPlacement)) {
            selectedShip = null;
            enableShipSelection();

            if (state.getPlayer().getBoard().allShipsPlaced()) {
                machineGrid.setDisable(false);
            }

            renderBoards();
        }
    }

    // ------------------------------------------------------
    // PLAYER SHOT
    // ------------------------------------------------------
    private void onPlayerShot(int row, int col) {
        Cell.ShotResult result = state.getMachineBoard().shoot(row, col);
        renderBoards();

        if (result == Cell.ShotResult.MISS) {
            aiTurnThread();
        }
    }

    // ======================================================
    // 🧵 HILO 1 → IA
    // ======================================================
    private void aiTurnThread() {
        Thread aiThread = new Thread(() -> {
            try {
                Thread.sleep(800); // Simula pensar
            } catch (InterruptedException ignored) {}

            int[] pos = ai.nextShot();
            state.getPlayer().getBoard().shoot(pos[0], pos[1]);

            Platform.runLater(this::renderBoards);
        });

        aiThread.setDaemon(true);
        aiThread.start();
    }

    // ======================================================
    // 🧵 HILO 2 → AUTOSAVE
    // ======================================================
    private void autoSaveThread() {
        Thread saveThread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                SaveManager.saveState(state);
            } catch (Exception ignored) {}
        });

        saveThread.setDaemon(true);
        saveThread.start();
    }

    // ------------------------------------------------------
    // SAVE / LOAD
    // ------------------------------------------------------
    @FXML
    private void onSaveGame() throws GameFileException {
        SaveManager.saveState(state);
        autoSaveThread();
    }

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
    @FXML
    private void onBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(new Scene(
                javafx.fxml.FXMLLoader.load(getClass().getResource("/main.fxml"))
        ));
    }
}
