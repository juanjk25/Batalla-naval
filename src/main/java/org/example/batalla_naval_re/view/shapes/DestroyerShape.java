package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

/**
 * Clase responsable de dibujar la representación gráfica de un Destructor.
 * <p>
 * Un destructor ocupa 2 celdas. Esta clase gestiona la apariencia visual de cada parte
 * del barco (proa y popa/torre) dependiendo de su orientación (horizontal o vertical).
 * </p>
 */
public class DestroyerShape extends ShipShape implements IShipShape {

    /**
     * Crea el nodo gráfico para una celda específica ocupada por el destructor.
     * <p>
     * Calcula la posición relativa de la celda dentro del barco (0 o 1) para determinar
     * qué parte dibujar (cañones o torre).
     * </p>
     *
     * @param ship El objeto {@link Ship} (Destructor).
     * @param cell La celda específica que se está renderizando.
     * @return Un {@link Node} (Group) con la forma visual de esa parte del barco.
     */
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

    /**
     * Dibuja los componentes de un destructor orientado horizontalmente.
     *
     * @param group    Grupo gráfico donde añadir las formas.
     * @param color    Color base del barco.
     * @param position indice de la parte (0: Proa con cañón, 1: Popa con torre).
     */
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

    /**
     * Dibuja los componentes de un destructor orientado verticalmente.
     *
     * @param group    Grupo gráfico donde añadir las formas.
     * @param color    Color base del barco.
     * @param position indice de la parte (0: Proa con cañón, 1: Popa con torre).
     */
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

    /**
     * Determina el color del barco según su estado.
     *
     * @param cell La celda actual.
     * @return Color.DARKRED (Hundido), Color.ORANGERED (Tocado) o Azul Navy (Intacto).
     */
    @Override
    protected Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.web("#002147");  // Navy mejorado
    }
}