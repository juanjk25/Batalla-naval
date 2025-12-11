package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;
import org.example.batalla_naval_re.model.ShipType;

/**
 * Fábrica encargada de crear las representaciones visuales de los barcos.
 * <p>
 * Proporciona métodos para generar nodos gráficos básicos o para obtener
 * la estrategia de dibujo específica ({@link IShipShape}) según el tipo de barco.
 * Implementa el patrón Factory para desacoplar la creación de las formas de su uso.
 * </p>
 */
public class ShipShapeFactory {

    /**
     * Crea un nodo gráfico genérico (Rectángulo simple) para representar una celda de un barco.
     * <p>
     * Este método genera una representación visual simplificada basada en bloques de color.
     * Es útil como mecanismo de respaldo (fallback) o para depuración.
     * El color del rectángulo varía según el tipo de barco y su estado (intacto, tocado, hundido).
     * </p>
     *
     * @param ship El barco al que pertenece la celda.
     * @param cell La celda específica que se va a dibujar.
     * @return Un {@link Node} (Rectangle) configurado con el color y tamaño correspondientes.
     */
    public static Node createShipNode(Ship ship, Cell cell) {

        Rectangle rect = new Rectangle(38, 38); // tamaño interno de celda

        // Colores base por tipo de barco (solo estilo visual)
        Color base;
        switch (ship.getType()) {
            case CARRIER -> base = Color.DARKSLATEGRAY;
            case SUBMARINE -> base = Color.DARKCYAN;
            case DESTROYER -> base = Color.DARKMAGENTA;
            case FRIGATE -> base = Color.DARKOLIVEGREEN;
            default -> base = Color.GRAY;
        }

        // Cambia color según estado
        if (cell.isSunkPart()) {
            rect.setFill(Color.DARKRED);
        } else if (cell.isHit()) {
            rect.setFill(Color.ORANGE);
        } else {
            rect.setFill(base);
        }

        rect.setArcWidth(8);
        rect.setArcHeight(8);
        rect.setStroke(Color.BLACK);

        return rect;
    }

    /**
     * Método de fábrica para obtener la instancia de {@link IShipShape} adecuada para un tipo de barco.
     * <p>
     * Devuelve el objeto encargado de dibujar la forma compleja y detallada correspondiente
     * al {@link ShipType} proporcionado.
     * </p>
     *
     * @param type El tipo de barco ({@link ShipType}) para el cual se requiere el dibujante.
     * @return Una instancia de {@link IShipShape} específica (ej. CarrierShape, DestroyerShape), o null si no está implementada.
     */
    public static IShipShape createShape(ShipType type) {
        return null;
    }
}