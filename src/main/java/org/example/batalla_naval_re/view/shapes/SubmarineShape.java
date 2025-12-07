package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public class SubmarineShape implements IShipShape {

    @Override
    public Node createShape(Ship ship, Cell cell) {
        Group group = new Group();
        Color shipColor = getShipColor(cell);
        int position = ship.getCellPosition(cell);

        if (ship.isHorizontal()) {
            createHorizontalSubmarine(group, shipColor, position);
        } else {
            createVerticalSubmarine(group, shipColor, position);
        }

        return group;
    }

    private void createHorizontalSubmarine(Group group, Color color, int position) {
        // Cuerpo ovalado
        Ellipse hull = new Ellipse(15, 8);
        hull.setFill(color);

        // Torre
        Rectangle tower = new Rectangle(8, 10);
        tower.setFill(color.darker());
        tower.setTranslateY(-8);

        // Periscopio (solo en celda central)
        if (position == 1) {
            Circle periscope = new Circle(3, Color.RED);
            periscope.setTranslateY(-12);
            group.getChildren().add(periscope);
        }

        group.getChildren().addAll(hull, tower);
    }

    private void createVerticalSubmarine(Group group, Color color, int position) {
        Ellipse hull = new Ellipse(8, 15);
        hull.setFill(color);

        Rectangle tower = new Rectangle(10, 8);
        tower.setFill(color.darker());
        tower.setTranslateX(-8);

        if (position == 1) {
            Circle periscope = new Circle(3, Color.RED);
            periscope.setTranslateX(-12);
            group.getChildren().add(periscope);
        }

        group.getChildren().addAll(hull, tower);
    }

    private Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.DARKSLATEGRAY;
    }
}