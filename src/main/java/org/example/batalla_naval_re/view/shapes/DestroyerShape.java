package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public class DestroyerShape implements IShipShape {

    @Override
    public Node createShape(Ship ship, Cell cell) {
        Group group = new Group();
        Color shipColor = getShipColor(cell);
        int position = ship.getCellPosition(cell);

        if (ship.isHorizontal()) {
            createHorizontalDestroyer(group, shipColor, position);
        } else {
            createVerticalDestroyer(group, shipColor, position);
        }

        return group;
    }

    private void createHorizontalDestroyer(Group group, Color color, int position) {
        Rectangle hull = new Rectangle(35, 10);
        hull.setFill(color);
        hull.setArcWidth(6);
        hull.setArcHeight(6);

        // Cañón (en celda frontal)
        if (position == 0) {
            Rectangle cannon = new Rectangle(6, 4);
            cannon.setFill(Color.BLACK);
            cannon.setTranslateX(-10);
            group.getChildren().add(cannon);
        }

        // Torre (en celda trasera)
        if (position == 1) {
            Rectangle tower = new Rectangle(8, 12);
            tower.setFill(color.darker());
            tower.setTranslateY(-10);
            group.getChildren().add(tower);
        }

        group.getChildren().add(hull);
    }

    private void createVerticalDestroyer(Group group, Color color, int position) {
        Rectangle hull = new Rectangle(10, 35);
        hull.setFill(color);
        hull.setArcWidth(6);
        hull.setArcHeight(6);

        if (position == 0) {
            Rectangle cannon = new Rectangle(4, 6);
            cannon.setFill(Color.BLACK);
            cannon.setTranslateY(-10);
            group.getChildren().add(cannon);
        }

        if (position == 1) {
            Rectangle tower = new Rectangle(12, 8);
            tower.setFill(color.darker());
            tower.setTranslateX(-10);
            group.getChildren().add(tower);
        }

        group.getChildren().add(hull);
    }

    private Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.NAVY;
    }
}