package org.example.batalla_naval_re.view.shapes;

import javafx.scene.paint.Color;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

/**
 * Clase abstracta base para las representaciones gráficas de los barcos.
 * <p>
 * Implementa la interfaz {@link IShipShape} y proporciona métodos de utilidad comunes
 * para determinar colores y posiciones relativas de las celdas dentro de un barco.
 * Las clases específicas de cada tipo de barco deben extender de esta para heredar
 * esta lógica básica.
 * </p>
 */
public abstract class ShipShape implements IShipShape {

    /**
     * Determina el color que debe tener una celda del barco basándose en su estado actual.
     *
     * @param cell La celda a evaluar.
     * @return {@code Color.web("#6A0000")} (Rojo oscuro) si es parte de un barco hundido,
     *         {@code Color.web("#E65C00")} (Naranja) si ha sido impactada,
     *         {@code Color.web("#404040")} (Gris oscuro) si está intacta.
     */
    protected Color getShipColor(Cell cell) {

        if (cell.isSunkPart()) {
            return Color.web("#6A0000"); // rojo hundido oscuro
        }

        if (cell.isHit()) {
            return Color.web("#E65C00"); // naranja tocado
        }

        return Color.web("#404040"); // gris metal barco sano
    }

    /**
     * Calcula el índice de una celda específica dentro de la lista de celdas que componen un barco.
     * <p>
     * Esto es útil para determinar qué parte del barco dibujar (proa, popa, cuerpo medio)
     * en una coordenada específica del tablero.
     * </p>
     *
     * @param ship El barco que contiene la celda.
     * @param cell La celda cuya posición se busca.
     * @return El índice (0 a size-1) de la celda en el barco, o -1 si no pertenece a él.
     */
    protected int getPositionInShip(Ship ship, Cell cell) {
        if (ship == null || cell == null) return -1;

        return ship.getCells().indexOf(
                ship.getCells().stream()
                        .filter(c -> c.getRow() == cell.getRow() && c.getCol() == cell.getCol())
                        .findFirst()
                        .orElse(null)
        );
    }

    /**
     * Proporciona un color para indicar visualmente si la colocación de un barco es válida.
     * <p>
     * util para implementar feedback visual durante la fase de posicionamiento (drag and drop).
     * </p>
     *
     * @param validPlacement {@code true} si la posición es válida.
     * @return {@code Color.LIGHTGREEN} para válido, {@code Color.RED} para inválido.
     */
    protected Color getPlacementOutline(boolean validPlacement) {
        return validPlacement ? Color.LIGHTGREEN : Color.RED;
    }
}