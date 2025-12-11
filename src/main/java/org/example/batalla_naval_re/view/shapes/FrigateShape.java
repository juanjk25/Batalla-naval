package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

/**
 * Clase responsable de dibujar la representación gráfica de una Fragata.
 * <p>
 * La fragata es el barco más pequeño (tamaño 1). Esta clase define su apariencia visual,
 * que consiste en un casco circular y una pequeña torre, además de efectos visuales
 * según su estado.
 * </p>
 */
public class FrigateShape implements IShipShape {

    /**
     * Crea el nodo gráfico que representa la fragata en la celda dada.
     * <p>
     * Al ser un barco de una sola celda, la lógica de dibujo es directa.
     * Incluye detalles como una luz de señal (si está intacta) o un efecto de salpicadura
     * si ha sido hundida.
     * </p>
     *
     * @param ship El objeto {@link Ship} (Fragata).
     * @param cell La celda donde se encuentra el barco.
     * @return Un {@link Node} (un {@link Group}) con la forma visual de la fragata.
     */
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

    /**
     * Determina el color base de la fragata según el estado de la celda.
     *
     * @param cell La celda a evaluar.
     * @return El color correspondiente al estado (Hundido, Tocado o Intacto).
     */
    private Color getShipColor(Cell cell) {
        if (cell.isSunkPart()) return Color.DARKRED;
        if (cell.isHit()) return Color.ORANGERED;
        return Color.STEELBLUE;
    }
}