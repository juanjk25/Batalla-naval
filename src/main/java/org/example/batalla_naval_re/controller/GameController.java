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

    // ✅ Iconos
    private final Image hitImage =
            new Image(getClass().getResourceAsStream("/impacto.jpg"));
    private final Image sunkImage =
            new Image(getClass().getResourceAsStream("/undido.jpg"));

    // ------------------------------------------------------
    // INIT
    // ------------------------------------------------------
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
    private void paintCellState(StackPane pane, Cell cell) {
        if (cell.isSunkPart()) drawSunkIcon(pane);
        else if (cell.isHit()) drawHitIcon(pane);
        else if (cell.isMiss()) drawMiss(pane);
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
        Image img = new Image(getClass().getResourceAsStream(
                ship.getType().getImagePath()));
        ImageView iv = new ImageView(img);
        iv.setFitWidth(40);
        iv.setFitHeight(40);
        pane.getChildren().add(iv);
    }

    private void drawHitIcon(StackPane pane) {
        ImageView iv = new ImageView(hitImage);
        iv.setFitWidth(28);
        iv.setFitHeight(28);
        pane.getChildren().add(iv);
    }

    private void drawSunkIcon(StackPane pane) {
        ImageView iv = new ImageView(sunkImage);
        iv.setFitWidth(40);
        iv.setFitHeight(40);
        pane.getChildren().add(iv);
    }

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
    private void enableShipSelection() {
        for (Ship s : state.getPlayer().getBoard().getShips()) {
            if (s.getCells().isEmpty()) {
                selectedShip = s;
                return;
            }
        }
    }

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

    private void onPlayerShot(int row, int col) {
        Cell.ShotResult result = state.getMachineBoard().shoot(row, col);
        renderBoards();
        if (result == Cell.ShotResult.MISS) aiTurnThread();
    }

    // ------------------------------------------------------
    // THREADS
    // ------------------------------------------------------
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
                javafx.fxml.FXMLLoader.load(getClass()
                        .getResource("/main.fxml"))
        ));
    }
}
