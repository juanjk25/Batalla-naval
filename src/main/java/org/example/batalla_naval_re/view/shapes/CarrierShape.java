package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public class CarrierShape extends ShipShape implements IShipShape {

    @Override
    public Node createShape(Ship ship, Cell cell) {
        Group group = new Group();

        Color shipColor = getShipColor(cell);

        // 🚨 Cambio clave:
        int position = getPositionInShip(ship, cell);

        if (ship.isHorizontal()) {
            createHorizontalCarrier(group, shipColor, position);
        } else {
            createVerticalCarrier(group, shipColor, position);
        }

        return group;
    }

    private void createHorizontalCarrier(Group group, Color color, int position) {
        Rectangle hull = new Rectangle(35, 12);
        hull.setFill(color);
        hull.setArcWidth(8);
        hull.setArcHeight(8);

        // Cubierta central
        if (position == 1 || position == 2) {
            Rectangle deck = new Rectangle(25, 6);
            deck.setFill(color.brighter());
            deck.setTranslateY(-8);
            group.getChildren().add(deck);
        }

        // Torre comando
        if (position == 1) {
            Rectangle tower = new Rectangle(9, 15);
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

    @Override
    protected Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.DARKGRAY;
    }
}
