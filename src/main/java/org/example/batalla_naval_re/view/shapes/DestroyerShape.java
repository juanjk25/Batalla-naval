package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public class DestroyerShape extends ShipShape implements IShipShape {

    @Override
    public Node createShape(Ship ship, Cell cell) {
        Group group = new Group();
        Color shipColor = getShipColor(cell);

        // Ahora posición se obtiene correctamente desde ShipShape
        int position = getPositionInShip(ship, cell);

        if (ship.isHorizontal()) {
            createHorizontalDestroyer(group, shipColor, position);
        } else {
            createVerticalDestroyer(group, shipColor, position);
        }

        return group;
    }

    private void createHorizontalDestroyer(Group group, Color color, int position) {
        Rectangle hull = new Rectangle(40, 15);
        hull.setFill(color);
        hull.setArcWidth(8);
        hull.setArcHeight(8);

        // PROA (celda frontal)
        if (position == 0) {
            Rectangle cannon = new Rectangle(10, 6);
            cannon.setFill(Color.BLACK);
            cannon.setTranslateX(-10);
            cannon.setTranslateY(4);
            group.getChildren().add(cannon);
        }

        // TORRE/PUENTE (celda trasera)
        if (position == 1) {
            Rectangle tower = new Rectangle(14, 18);
            tower.setFill(color.darker());
            tower.setTranslateY(-4);
            tower.setTranslateX(10);
            group.getChildren().add(tower);
        }

        group.getChildren().add(hull);
    }

    private void createVerticalDestroyer(Group group, Color color, int position) {
        Rectangle hull = new Rectangle(15, 40);
        hull.setFill(color);
        hull.setArcWidth(8);
        hull.setArcHeight(8);

        // PROA
        if (position == 0) {
            Rectangle cannon = new Rectangle(6, 10);
            cannon.setFill(Color.BLACK);
            cannon.setTranslateY(-10);
            cannon.setTranslateX(4);
            group.getChildren().add(cannon);
        }

        // TORRE
        if (position == 1) {
            Rectangle tower = new Rectangle(18, 14);
            tower.setFill(color.darker());
            tower.setTranslateX(-2);
            tower.setTranslateY(10);
            group.getChildren().add(tower);
        }

        group.getChildren().add(hull);
    }

    @Override
    protected Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.web("#002147");  // Navy mejorado
    }
}
