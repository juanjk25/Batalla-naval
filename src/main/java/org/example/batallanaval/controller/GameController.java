package org.example.batallanaval.controller;

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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.batallanaval.ai.SimpleAI;
import org.example.batallanaval.model.*;
import org.example.batallanaval.persistence.SaveManager;

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
        lblNickname.setText(state.getPlayer().getNickname());
        renderBoards();
        updateStatus();
    }

    private void renderBoards() {
        playerGrid.getChildren().clear();
        machineGrid.getChildren().clear();
        // player (readonly)
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                StackPane cellPane = createCellPane(state.getPlayer().getBoard(), r, c, true);
                playerGrid.add(cellPane, c, r);
                GridPane.setHalignment(cellPane, HPos.CENTER);
                GridPane.setValignment(cellPane, VPos.CENTER);
            }
        }
        // machine (clickable for shots)
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
    }

    private StackPane createCellPane(Board board, int row, int col, boolean isPlayerBoard) {
        StackPane pane = new StackPane();
        pane.setPrefSize(36,36);
        Rectangle rect = new Rectangle(36,36);
        rect.getStyleClass().add("cell-rect");
        pane.getChildren().add(rect);
        Cell cell = board.getCell(row, col);
        // Render based on state
        if (cell.isShip() && (isPlayerBoard || revealMachineBoard || cell.isVisibleToPlayer())) {
            Rectangle shipRect = new Rectangle(30,30);
            shipRect.getStyleClass().add(cell.isSunkPart() ? "ship-sunk" : (cell.isHit() ? "ship-hit" : "ship"));
            pane.getChildren().add(shipRect);
        } else {
            // show X for miss
            if (cell.isMiss()) {
                Label x = new Label("X");
                x.getStyleClass().add("miss-label");
                pane.getChildren().add(x);
            }
        }
        return pane;
    }

    private void onPlayerShot(int r, int c) {
        if (state.isGameOver()) return;
        Board machine = state.getMachineBoard();
        Cell target = machine.getCell(r, c);
        if (target.isTried()) {
            lblStatus.setText("Ya disparaste aquí.");
            return;
        }
        boolean hit = machine.shoot(r, c);
        if (hit) {
            lblStatus.setText("¡Tocado!");
            if (machine.checkAndMarkSunkAt(r,c)) {
                lblStatus.setText("¡Hundido!");
                state.getPlayer().incrementSunkCount();
            }
            // player continues unless won
            if (machine.allShipsSunk()) {
                state.setGameOver(true);
                lblStatus.setText("¡Ganaste!");
            }
        } else {
            lblStatus.setText("Agua. Turno de la máquina...");
            // machine turn (simple AI) — run on Platform.runLater to allow UI update
            saveAndAiTurn();
        }
        safeSave();
        renderBoards();
        updateStatus();
    }

    private void saveAndAiTurn() {
        safeSave();
        Platform.runLater(() -> {
            // small delay could be added with Timeline; keep simple
            // AI shoots until misses or game over as described: AI selects random; but per spec AI shoots randomly and passes turn on miss.
            boolean aiContinues = true;
            while (aiContinues && !state.isGameOver()) {
                int[] shot = ai.nextShot();
                boolean hit = state.getPlayer().getBoard().shoot(shot[0], shot[1]);
                ai.reportResult(shot, hit);
                if (hit) {
                    state.getMachineSunkCount().incrementAndGet(); // optional counter
                    state.getMachine().ifPresent(m -> {});
                    if (state.getPlayer().getBoard().allShipsSunk()) {
                        state.setGameOver(true);
                        lblStatus.setText("La máquina ganó.");
                        break;
                    }
                    // AI continues after hit (spec says AI can continue)
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
        // volver a main
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/org/example/batallanaval/styles/main.fxml"));
        javafx.scene.layout.AnchorPane root = loader.load();
        Stage stage = (Stage) btnBack.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/org/example/batallanaval/styles/styles.css").toExternalForm());
        stage.setScene(scene);
    }

    private void safeSave() {
        try { SaveManager.saveState(state); } catch (IOException e) { System.err.println("Error guardando: " + e.getMessage()); }
    }
}
