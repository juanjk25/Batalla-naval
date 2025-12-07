package org.example.batalla_naval_re.view.shapes;

import org.example.batalla_naval_re.model.ShipType;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public class ShipShapeFactory {

    public static IShipShape createShape(ShipType shipType) {
        if (shipType == null) {
            throw new IllegalArgumentException("ShipType cannot be null");
        }

        switch (shipType) {
            case CARRIER:
                return new CarrierShape();
            case SUBMARINE:
                return new SubmarineShape();
            case DESTROYER:
                return new DestroyerShape();
            case FRIGATE:
                return new FrigateShape();
            default:
                // Figura por defecto (rectángulo simple) como fallback
                return createDefaultShape();
        }
    }

    private static IShipShape createDefaultShape() {
        return new IShipShape() {
            @Override
            public javafx.scene.Node createShape(Ship ship, Cell cell) {
                javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(30, 30);
                if (cell.isSunkPart()) {
                    rect.setFill(javafx.scene.paint.Color.DARKRED);
                } else if (cell.isHit()) {
                    rect.setFill(javafx.scene.paint.Color.ORANGE);
                } else {
                    rect.setFill(javafx.scene.paint.Color.GRAY);
                }
                return rect;
            }
        };
    }
}