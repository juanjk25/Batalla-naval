package org.example.batalla_naval_re.view.renderer;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.example.batalla_naval_re.model.*;
import org.example.batalla_naval_re.view.shapes.IShipShape;
import org.example.batalla_naval_re.view.shapes.ShipShapeFactory;

public class BoardRenderer {

    public static StackPane renderCell(Board board, int row, int col,
                                       boolean isPlayerBoard, boolean revealOpponent) {
        StackPane pane = new StackPane();
        pane.setPrefSize(40, 40);

        // Fondo
        Rectangle background = new Rectangle(40, 40);
        background.setFill(Color.LIGHTBLUE);
        background.setStroke(Color.DARKBLUE);
        background.setStrokeWidth(1);
        pane.getChildren().add(background);

        Cell cell = board.getCell(row, col);

        if (cell.isShip() && shouldShowShip(cell, isPlayerBoard, revealOpponent)) {
            renderShip(pane, cell);
        }

        if (cell.isMiss()) {
            renderMiss(pane);
        }

        if (cell.isHit() && !cell.isShip()) {
            renderHitWater(pane);
        }

        return pane;
    }

    private static boolean shouldShowShip(Cell cell, boolean isPlayerBoard, boolean revealOpponent) {
        // Mostrar barco si:
        // 1. Es el tablero del jugador
        // 2. Está revelando el oponente
        // 3. Está golpeado o hundido
        return isPlayerBoard || revealOpponent || cell.isHit() || cell.isSunkPart();
    }

    private static void renderShip(StackPane pane, Cell cell) {
        Ship ship = cell.getShip();
        IShipShape shape = ShipShapeFactory.createShape(ship.getType());

        Node shipNode = shape.createShape(ship, cell);
        pane.getChildren().add(shipNode);

        // Añadir efectos según estado
        if (cell.isSunkPart()) {
            addSunkEffects(pane);
        } else if (cell.isHit()) {
            addHitEffects(pane);
        }
    }

    private static void renderMiss(StackPane pane) {
        Line line1 = new Line(10, 10, 30, 30);
        line1.setStroke(Color.BLUE);
        line1.setStrokeWidth(2);

        Line line2 = new Line(30, 10, 10, 30);
        line2.setStroke(Color.BLUE);
        line2.setStrokeWidth(2);

        pane.getChildren().addAll(line1, line2);
    }

    private static void renderHitWater(StackPane pane) {
        Circle hitMarker = new Circle(5);
        hitMarker.setFill(Color.RED);
        pane.getChildren().add(hitMarker);
    }

    private static void addHitEffects(StackPane pane) {
        Circle explosion = new Circle(8);
        explosion.setFill(Color.TRANSPARENT);
        explosion.setStroke(Color.ORANGE);
        explosion.setStrokeWidth(2);
        pane.getChildren().add(explosion);
    }

    private static void addSunkEffects(StackPane pane) {
        Circle ripple = new Circle(12);
        ripple.setFill(Color.TRANSPARENT);
        ripple.setStroke(Color.DARKRED);
        ripple.setStrokeWidth(2);
        pane.getChildren().add(ripple);
    }
}