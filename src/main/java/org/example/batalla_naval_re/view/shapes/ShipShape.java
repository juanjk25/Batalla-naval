package org.example.batalla_naval_re.view.shapes;

import javafx.scene.paint.Color;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public abstract class ShipShape implements IShipShape {

    /**
     * Determina el color según el estado real de la celda.
     * - Hundido → rojo oscuro
     * - Tocado  → naranja
     * - Barco sano → gris
     */
    protected Color getShipColor(Cell cell) {

        if (cell.isSunkPart()) {
            return Color.web("#6A0000"); // rojo hundido oscuro
        }

        if (cell.isHit()) {
            return Color.web("#E65C00"); // naranja tocado
        }

        return Color.web("#404040"); // gris metal barco sano
    }

    /**
     * Devuelve la posición del Cell dentro del barco (0, 1, 2...).
     * Si la celda no pertenece al barco, retorna -1.
     */
    protected int getPositionInShip(Ship ship, Cell cell) {
        if (ship == null || cell == null) return -1;

        return ship.getCells().indexOf(
                ship.getCells().stream()
                        .filter(c -> c.getRow() == cell.getRow() && c.getCol() == cell.getCol())
                        .findFirst()
                        .orElse(null)
        );
    }

    /**
     * Determina si pintar borde amarillo cuando el jugador aún está colocando.
     * (más adelante si quieres highlight en arrastre)
     */
    protected Color getPlacementOutline(boolean validPlacement) {
        return validPlacement ? Color.LIGHTGREEN : Color.RED;
    }
}
