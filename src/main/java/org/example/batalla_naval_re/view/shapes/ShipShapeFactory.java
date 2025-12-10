package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;
import org.example.batalla_naval_re.model.ShipType;

/**
 * Fábrica de figuras para representar cada parte de un barco en el tablero.
 */
public class ShipShapeFactory {

    /**
     * Devuelve una figura 2D para representar UNA parte del barco (una celda).
     * El GameController coloca 1 figura por celda ocupada.
     */
    public static Node createShipNode(Ship ship, Cell cell) {

        Rectangle rect = new Rectangle(38, 38); // tamaño interno de celda

        // Colores base por tipo de barco (solo estilo visual)
        Color base;
        switch (ship.getType()) {
            case CARRIER -> base = Color.DARKSLATEGRAY;
            case SUBMARINE -> base = Color.DARKCYAN;
            case DESTROYER -> base = Color.DARKMAGENTA;
            case FRIGATE -> base = Color.DARKOLIVEGREEN;
            default -> base = Color.GRAY;
        }

        // Cambia color según estado
        if (cell.isSunkPart()) {
            rect.setFill(Color.DARKRED);
        } else if (cell.isHit()) {
            rect.setFill(Color.ORANGE);
        } else {
            rect.setFill(base);
        }

        rect.setArcWidth(8);
        rect.setArcHeight(8);
        rect.setStroke(Color.BLACK);

        return rect;
    }

    public static IShipShape createShape(ShipType type) {
        return null;
    }
}
