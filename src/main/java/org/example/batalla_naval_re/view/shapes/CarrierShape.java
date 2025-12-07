package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public class CarrierShape implements IShipShape {

    @Override
    public Node createShape(Ship ship, Cell cell) {
        Group group = new Group();

        // Determinar color según estado
        Color shipColor = getShipColor(cell);
        int position = ship.getCellPosition(cell);

        if (ship.isHorizontal()) {
            // Carrier horizontal (4 celdas)
            createHorizontalCarrier(group, shipColor, position);
        } else {
            // Carrier vertical
            createVerticalCarrier(group, shipColor, position);
        }

        addEffects(group, cell);
        return group;
    }

    private void createHorizontalCarrier(Group group, Color color, int position) {
        // Casco principal
        Rectangle hull = new Rectangle(35, 12);
        hull.setFill(color);
        hull.setArcWidth(8);
        hull.setArcHeight(8);

        // Cubierta de vuelo (en posiciones centrales)
        if (position == 1 || position == 2) {
            Rectangle deck = new Rectangle(25, 6);
            deck.setFill(color.brighter());
            deck.setTranslateY(-8);
            group.getChildren().add(deck);
        }

        // Torre de control (en posición media)
        if (position == 1) {
            Rectangle tower = new Rectangle(8, 15);
            tower.setFill(color.darker());
            tower.setTranslateY(-12);
            group.getChildren().add(tower);
        }

        group.getChildren().add(hull);
    }

    private void createVerticalCarrier(Group group, Color color, int position) {
        Rectangle hull = new Rectangle(12, 35);
        hull.setFill(color);
        hull.setArcWidth(8);
        hull.setArcHeight(8);

        if (position == 1 || position == 2) {
            Rectangle deck = new Rectangle(6, 25);
            deck.setFill(color.brighter());
            deck.setTranslateX(-8);
            group.getChildren().add(deck);
        }

        if (position == 1) {
            Rectangle tower = new Rectangle(15, 8);
            tower.setFill(color.darker());
            tower.setTranslateX(-12);
            group.getChildren().add(tower);
        }

        group.getChildren().add(hull);
    }

    private Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.DARKGRAY;
    }

    private void addEffects(Group group, Cell cell) {
        // Puedes añadir efectos aquí si lo necesitas
    }
}