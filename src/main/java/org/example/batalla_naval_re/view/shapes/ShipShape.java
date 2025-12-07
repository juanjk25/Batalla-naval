package org.example.batalla_naval_re.view.shapes;

import javafx.scene.paint.Color;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public abstract class ShipShape implements IShipShape {

    protected Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) {
            return Color.DARKRED;
        }
        if (cell.isHit()) {
            return Color.ORANGERED;
        }
        return Color.DARKGRAY;
    }

    protected int getPositionInShip(Ship ship, Cell cell) {
        return ship.getCellPosition(cell);
    }
}