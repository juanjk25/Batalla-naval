package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

/**
 * Clase responsable de dibujar la representación gráfica de un Portaaviones (Carrier).
 * <p>
 * Extiende de {@link ShipShape} e implementa la lógica específica para renderizar
 * las partes visuales de un portaaviones, adaptándose a su orientación (horizontal o vertical)
 * y a su estado (normal, dañado, hundido).
 * </p>
 */
public class CarrierShape extends ShipShape implements IShipShape {

    /**
     * Crea el nodo gráfico que representa una parte específica del portaaviones en una celda dada.
     * <p>
     * Calcula la posición relativa de la celda dentro del barco para determinar qué segmento
     * dibujar (proa, popa, cuerpo central con torre, etc.).
     * </p>
     *
     * @param ship El objeto {@link Ship} (Portaaviones) al que pertenece la forma.
     * @param cell La celda específica del tablero donde se está dibujando esta parte.
     * @return Un {@link Node} (un {@link Group}) que contiene las formas geométricas del segmento del barco.
     */
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

    /**
     * Dibuja los componentes de un portaaviones orientado horizontalmente.
     * <p>
     * Añade detalles específicos como la cubierta de vuelo o la torre de control
     * dependiendo de la posición del segmento (índice).
     * </p>
     *
     * @param group    El grupo gráfico al que se añadirán las formas.
     * @param color    El color base a aplicar (depende del estado de daño).
     * @param position La posición (índice 0-3) de este segmento dentro del barco.
     */
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

    /**
     * Dibuja los componentes de un portaaviones orientado verticalmente.
     * <p>
     * Similar a la versión horizontal, pero rota las dimensiones y desplazamientos
     * de los elementos gráficos.
     * </p>
     *
     * @param group    El grupo gráfico al que se añadirán las formas.
     * @param color    El color base a aplicar.
     * @param position La posición (índice 0-3) de este segmento dentro del barco.
     */
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

    /**
     * Determina el color del barco en función del estado de la celda.
     *
     * @param cell La celda que se está renderizando.
     * @return {@code Color.DARKRED} si es parte de un barco hundido,
     *         {@code Color.ORANGERED} si ha sido impactada,
     *         {@code Color.DARKGRAY} si está intacta.
     */
    @Override
    protected Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.DARKGRAY;
    }
}