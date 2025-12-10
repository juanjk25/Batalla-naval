package org.example.batalla_naval_re.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.batalla_naval_re.ai.SimpleAI;
import org.example.batalla_naval_re.model.*;
import org.example.batalla_naval_re.persistence.SaveManager;

import java.io.IOException;

public class GameController {

    @FXML private GridPane playerGrid;
    @FXML private GridPane machineGrid;
    @FXML private Label lblNickname;
    @FXML private Label lblStatus;
    @FXML private Label lblSunkCount;
    @FXML private Button btnBack;

    private GameState state;
    private boolean revealMachineBoard = false;
    private SimpleAI ai;

    public void initState(GameState state, boolean revealMachine) {
        this.state = state;
        this.revealMachineBoard = revealMachine;
        this.ai = new SimpleAI(state.getMachineBoard());
        lblNickname.setText(state.getPlayer().getName());
        renderBoards();
        updateStatus();
    }

    private void renderBoards() {
        playerGrid.getChildren().clear();
        machineGrid.getChildren().clear();

        // Render del tablero del jugador
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                StackPane cellPane = createCellPane(state.getPlayer().getBoard(), r, c, true);
                playerGrid.add(cellPane, c, r);
                GridPane.setHalignment(cellPane, HPos.CENTER);
                GridPane.setValignment(cellPane, VPos.CENTER);
            }
        }

        // Render del tablero de la máquina
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                StackPane cellPane = createCellPane(state.getMachineBoard(), r, c, false);
                final int rr = r;
                final int cc = c;
                cellPane.setOnMouseClicked(ev -> {
                    if (ev.getButton() != MouseButton.PRIMARY) return;
                    onPlayerShot(rr, cc);
                });
                machineGrid.add(cellPane, c, r);
            }
        }

        // Renderizar imágenes de barcos del jugador
        renderShipImages(state.getPlayer().getBoard(), true);

        // Renderizar imágenes de barcos de la máquina si corresponde
        if (revealMachineBoard) {
            renderShipImages(state.getMachineBoard(), false);
        }
    }

    private StackPane createCellPane(Board board, int row, int col, boolean isPlayerBoard) {
        StackPane pane = new StackPane();
        pane.setPrefSize(40, 40);

        // Fondo del agua
        Rectangle background = new Rectangle(40, 40);
        background.setFill(Color.LIGHTBLUE);
        background.setStroke(Color.DARKBLUE);
        background.setStrokeWidth(1);
        pane.getChildren().add(background);

        Cell cell = board.getCell(row, col);

        // Render disparo fallado
        if (cell.isMiss()) {
            renderMissCell(pane);
        }

        return pane;
    }

    private void renderMissCell(StackPane pane) {
        // X para disparo al agua
        javafx.scene.shape.Line line1 = new javafx.scene.shape.Line(5, 5, 35, 35);
        line1.setStroke(Color.BLUE);
        line1.setStrokeWidth(2);

        javafx.scene.shape.Line line2 = new javafx.scene.shape.Line(35, 5, 5, 35);
        line2.setStroke(Color.BLUE);
        line2.setStrokeWidth(2);

        pane.getChildren().addAll(line1, line2);
    }

    private void renderShipImages(Board board, boolean isPlayerBoard) {
        for (Ship ship : board.getShips()) {
            Cell startCell = ship.getCells().get(0);

            Image img = new Image(getClass().getResourceAsStream(ship.getType().getImagePath()));
            ImageView shipView = new ImageView(img);

            // Ajustar tamaño según orientación
            if (ship.isHorizontal()) {
                shipView.setFitWidth(40 * ship.getType().getSize());
                shipView.setFitHeight(40);
            } else {
                shipView.setFitWidth(40);
                shipView.setFitHeight(40 * ship.getType().getSize());
            }
            shipView.setPreserveRatio(false);

            // Posicionar en la celda inicial
            GridPane.setColumnIndex(shipView, startCell.getCol());
            GridPane.setRowIndex(shipView, startCell.getRow());
            if (ship.isHorizontal()) {
                GridPane.setColumnSpan(shipView, ship.getType().getSize());
            } else {
                GridPane.setRowSpan(shipView, ship.getType().getSize());
            }

            if (isPlayerBoard) {
                playerGrid.getChildren().add(shipView);
            } else {
                machineGrid.getChildren().add(shipView);
            }
        }
    }

    private void onPlayerShot(int r, int c) {
        if (state.isGameOver()) return;

        Board machine = state.getMachineBoard();
        Cell target = machine.getCell(r, c);

        if (target.isTried()) {
            lblStatus.setText("Ya disparaste aquí.");
            return;
        }

        Cell.ShotResult result = machine.shoot(r, c);

        if (result == Cell.ShotResult.HIT || result == Cell.ShotResult.SUNK) {
            if (result == Cell.ShotResult.SUNK) {
                lblStatus.setText("¡Hundido!");
                state.getPlayer().incrementSunkCount();
            } else {
                lblStatus.setText("¡Tocado!");
            }

            if (machine.allShipsSunk()) {
                state.setGameOver(true);
                lblStatus.setText("¡Ganaste!");
            }
        } else {
            lblStatus.setText("Agua. Turno de la máquina...");
            saveAndAiTurn();
        }

        safeSave();
        renderBoards();
        updateStatus();
    }

    private void saveAndAiTurn() {
        safeSave();
        Platform.runLater(() -> {
            boolean aiContinues = true;

            while (aiContinues && !state.isGameOver()) {
                int[] shot = ai.nextShot();
                Cell.ShotResult result = state.getPlayer().getBoard().shoot(shot[0], shot[1]);
                boolean hit = (result == Cell.ShotResult.HIT || result == Cell.ShotResult.SUNK);
                ai.reportResult(shot, hit);

                if (hit) {
                    if (state.getPlayer().getBoard().allShipsSunk()) {
                        state.setGameOver(true);
                        lblStatus.setText("La máquina ganó.");
                        break;
                    }
                    lblStatus.setText("Máquina: ¡Tocado!");
                } else {
                    lblStatus.setText("Máquina: Agua.");
                    aiContinues = false;
                }

                safeSave();
            }

            renderBoards();
            updateStatus();
        });
    }

    private void updateStatus() {
        lblSunkCount.setText("Barcos hundidos: " + state.getPlayer().getSunkCount());
    }

    @FXML
    protected void onBack() throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(new javafx.scene.Scene(
                javafx.fxml.FXMLLoader.load(getClass().getResource("/main.fxml"))
        ));
    }

    private void safeSave() {
        try {
            SaveManager.saveState(state);
        } catch (IOException e) {
            System.err.println("Error guardando: " + e.getMessage());
        }
    }
}
