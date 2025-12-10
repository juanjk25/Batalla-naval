package org.example.batalla_naval_re.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.example.batalla_naval_re.ai.SimpleAI;
import org.example.batalla_naval_re.model.*;

import java.io.IOException;

public class GameController {

    @FXML private GridPane playerGrid;
    @FXML private GridPane machineGrid;
    @FXML private Label lblNickname;
    @FXML private Label lblStatus;
    @FXML private Label lblSunkCount;
    @FXML private Button btnBack;

    private GameState state;
    private SimpleAI ai;

    private Ship selectedShip;
    private boolean horizontalPlacement = true;

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
        enableBoardInteraction();
    }

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

    private StackPane createPlayerCell(int row, int col) {
        Cell cell = state.getPlayer().getBoard().getCell(row, col);
        StackPane pane = createBaseCell();

        if (cell.isShip()) showShipIcon(pane, cell.getShip());

        if (cell.isHit()) drawHit(pane);
        if (cell.isSunkPart()) drawSunk(pane);
        if (cell.isMiss()) drawMiss(pane);

        pane.setOnMouseClicked(ev -> {
            if (ev.getButton() == MouseButton.SECONDARY) {
                horizontalPlacement = !horizontalPlacement;
                lblStatus.setText(horizontalPlacement ? "Horizontal" : "Vertical");
            }
        });

        pane.setOnMouseClicked(ev -> {
            if (selectedShip == null) return;
            attemptPlaceShip(row, col);
        });

        return pane;
    }

    private StackPane createMachineCell(int row, int col) {
        Cell cell = state.getMachineBoard().getCell(row, col);
        StackPane pane = createBaseCell();

        pane.setOnMouseClicked(ev -> {
            if (ev.getButton() != MouseButton.PRIMARY) return;
            onPlayerShot(row, col);
        });

        // No mostramos iconos de los barcos aquí
        // Solo el disparo
        if (cell.isHit()) drawHit(pane);
        if (cell.isSunkPart()) drawSunk(pane);
        if (cell.isMiss()) drawMiss(pane);

        return pane;
    }

    private StackPane createBaseCell() {
        StackPane pane = new StackPane();
        pane.setPrefSize(40, 40);

        Rectangle rect = new Rectangle(40, 40);
        rect.setFill(Color.LIGHTBLUE);
        rect.setStroke(Color.DARKBLUE);

        pane.getChildren().add(rect);
        return pane;
    }

    // ----------------------------------------
    // ICONOS RESTAURADOS
    // ----------------------------------------
    private void showShipIcon(StackPane pane, Ship ship) {
        String path = ship.getType().getImagePath(); // ya lo tienes en tu enum
        Image img = new Image(getClass().getResourceAsStream(path));
        ImageView iv = new ImageView(img);
        iv.setFitWidth(40);
        iv.setFitHeight(40);

        pane.getChildren().add(iv);
    }

    // ----------------------------------------
    // DISPAROS VISUALES
    // ----------------------------------------
    private void drawHit(StackPane pane) {
        Circle c = new Circle(10, Color.RED);
        c.setOpacity(0.85);
        pane.getChildren().add(c);
    }

    private void drawMiss(StackPane pane) {
        Line l1 = new Line(5, 5, 35, 35);
        Line l2 = new Line(35, 5, 5, 35);
        l1.setStroke(Color.BLUE); l2.setStroke(Color.BLUE);
        l1.setStrokeWidth(2); l2.setStrokeWidth(2);
        pane.getChildren().addAll(l1, l2);
    }

    private void drawSunk(StackPane pane) {
        Rectangle sunk = new Rectangle(40, 40, Color.DARKRED);
        sunk.setOpacity(0.7);
        pane.getChildren().add(sunk);
    }

    // ------------------------------------------------------
    // COLOCACIÓN
    // ------------------------------------------------------
    private void enableBoardInteraction() {
        for (Ship ship : state.getPlayer().getBoard().getShips()) {
            if (ship.getCells().isEmpty()) {
                selectedShip = ship;
                lblStatus.setText("Seleccionado: " + ship.getName());
                break;
            }
        }
    }

    private void attemptPlaceShip(int row, int col) {
        if (selectedShip == null) return;

        boolean can = state.getPlayer().getBoard().addShip(selectedShip, row, col, horizontalPlacement);

        if (!can) {
            lblStatus.setText("No se puede colocar ahí.");
            return;
        }

        lblStatus.setText(selectedShip.getName() + " colocado.");
        selectedShip = null;

        if (state.getPlayer().getBoard().allShipsPlaced()) {
            lblStatus.setText("Listo. Dispara al tablero enemigo.");
            machineGrid.setDisable(false);
        } else {
            enableBoardInteraction();
        }

        renderBoards();
    }

    // ------------------------------------------------------
    // DISPAROS AL ENEMIGO
    // ------------------------------------------------------
    private void onPlayerShot(int r, int c) {

        if (!state.getPlayer().getBoard().allShipsPlaced()) {
            lblStatus.setText("Primero coloca todos tus barcos.");
            return;
        }

        Cell.ShotResult result = state.getMachineBoard().shoot(r, c);

        switch (result) {
            case HIT -> lblStatus.setText("¡Tocado!");
            case SUNK -> lblStatus.setText("¡Hundido!");
            case MISS -> {
                lblStatus.setText("Agua...");
                aiTurn();
            }
            case ALREADY_TRIED -> {
                lblStatus.setText("Ya disparaste ahí.");
                return;
            }
        }

        renderBoards();
        checkEndGame();
    }

    private void aiTurn() {
        Platform.runLater(() -> {
            boolean again;
            do {
                int[] shot = ai.nextShot();
                Cell.ShotResult result = state.getPlayer().getBoard().shoot(shot[0], shot[1]);

                again = (result == Cell.ShotResult.HIT);

                if (state.getPlayer().getBoard().allShipsSunk()) {
                    lblStatus.setText("La máquina ganó.");
                    break;
                }
            } while (again);

            renderBoards();
        });
    }

    private void checkEndGame() {
        if (state.getMachineBoard().allShipsSunk()) {
            lblStatus.setText("¡Has ganado!");
        }
    }

    @FXML
    protected void onBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setFullScreen(false);
        stage.setScene(new Scene(
                javafx.fxml.FXMLLoader.load(getClass().getResource("/main.fxml"))
        ));
    }
}
