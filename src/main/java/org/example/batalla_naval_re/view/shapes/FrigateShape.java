package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public class FrigateShape implements IShipShape {

    @Override
    public Node createShape(Ship ship, Cell cell) {
        Group group = new Group();
        Color shipColor = getShipColor(cell);

        // Fragata pequeña (1 celda)
        Circle hull = new Circle(12);
        hull.setFill(shipColor);

        // Torre pequeña
        Rectangle tower = new Rectangle(6, 8);
        tower.setFill(shipColor.darker());
        tower.setTranslateY(-10);

        // Luz de señal (solo si está intacta)
        if (!cell.isHit() && !cell.isSunkPart()) {
            Circle light = new Circle(2, Color.LIME);
            light.setTranslateY(-14);
            group.getChildren().add(light);
        }

        group.getChildren().addAll(hull, tower);

        // Efecto de hundimiento (fragata se hunde inmediatamente al ser golpeada)
        if (cell.isSunkPart()) {
            Circle splash = new Circle(15);
            splash.setFill(Color.TRANSPARENT);
            splash.setStroke(Color.BLUE);
            splash.setStrokeWidth(1);
            group.getChildren().add(splash);
        }

        return group;
    }

    private Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.STEELBLUE;
    }
}