package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

/**
 * Clase responsable de dibujar la representación gráfica de un Submarino.
 * <p>
 * El submarino es un barco de tamaño medio (3 celdas). Esta clase define su aspecto visual,
 * caracterizado por un casco elíptico y una torre con periscopio en la sección central.
 * </p>
 */
public class SubmarineShape extends ShipShape implements IShipShape {

    /**
     * Crea el nodo gráfico para una celda específica ocupada por el submarino.
     * <p>
     * Determina la orientación del barco y la posición relativa de la celda
     * para dibujar los elementos correctos (por ejemplo, el periscopio solo en el centro).
     * </p>
     *
     * @param ship El objeto {@link Ship} (Submarino).
     * @param cell La celda específica que se está renderizando.
     * @return Un {@link Node} (Group) con la forma visual de esa parte del submarino.
     */
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

    /**
     * Dibuja los componentes de un submarino orientado horizontalmente.
     * <p>
     * Si la posición es la central (índice 1), añade el detalle del periscopio
     * sobre la torre.
     * </p>
     *
     * @param group    Grupo gráfico donde añadir las formas.
     * @param color    Color base del barco.
     * @param position indice de la parte dentro del barco (0, 1, 2).
     */
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

    /**
     * Dibuja los componentes de un submarino orientado verticalmente.
     * <p>
     * Similar a la versión horizontal, pero con las dimensiones rotadas y
     * ajustes en las traslaciones para alinear la torre y el periscopio.
     * </p>
     *
     * @param group    Grupo gráfico donde añadir las formas.
     * @param color    Color base del barco.
     * @param position indice de la parte dentro del barco (0, 1, 2).
     */
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

    /**
     * Determina el color del submarino según su estado.
     *
     * @param cell La celda actual.
     * @return {@code Color.DARKRED} si está hundido, {@code Color.ORANGERED} si está tocado,
     *         o {@code Color.DARKSLATEGRAY} (gris pizarra oscuro) si está intacto.
     */
    @Override
    protected Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.DARKSLATEGRAY;
    }
}