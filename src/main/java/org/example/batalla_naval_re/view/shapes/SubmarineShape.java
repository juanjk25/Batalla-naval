package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public class SubmarineShape extends ShipShape implements IShipShape {

    @Override
    public Node createShape(Ship ship, Cell cell) {
        Group group = new Group();

        Color shipColor = getShipColor(cell);

        // 🚨 Cambio clave: ya no usamos ship.getCellPosition
        int position = getPositionInShip(ship, cell);

        if (ship.isHorizontal()) {
            createHorizontalSubmarine(group, shipColor, position);
        } else {
            createVerticalSubmarine(group, shipColor, position);
        }

        return group;
    }

    private void createHorizontalSubmarine(Group group, Color color, int position) {
        Ellipse hull = new Ellipse(18, 9);
        hull.setFill(color);

        Rectangle tower = new Rectangle(10, 12);
        tower.setFill(color.darker());
        tower.setTranslateY(-10);

        // Periscopio sólo en el centro
        if (position == 1) {
            Circle periscope = new Circle(4, Color.BLACK);
            periscope.setTranslateY(-18);
            group.getChildren().add(periscope);
        }

        group.getChildren().addAll(hull, tower);
    }

    private void createVerticalSubmarine(Group group, Color color, int position) {
        Ellipse hull = new Ellipse(9, 18);
        hull.setFill(color);

        Rectangle tower = new Rectangle(12, 10);
        tower.setFill(color.darker());
        tower.setTranslateX(-10);

        if (position == 1) {
            Circle periscope = new Circle(4, Color.BLACK);
            periscope.setTranslateX(-18);
            group.getChildren().add(periscope);
        }

        group.getChildren().addAll(hull, tower);
    }

    @Override
    protected Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.DARKSLATEGRAY;
    }
}
