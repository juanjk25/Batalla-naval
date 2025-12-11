package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Node;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

/**
 * Interfaz para definir la estrategia de dibujo de los barcos en la interfaz gráfica.
 * <p>
 * Permite implementar diferentes formas y estilos visuales para cada tipo de barco,
 * delegando la creación de los nodos gráficos (JavaFX) a clases especializadas.
 * </p>
 */
public interface IShipShape {

    /**
     * Crea la representación gráfica (Nodo 2D) de una parte del barco.
     * <p>
     * La implementación debe determinar qué dibujar basándose en:
     * <ul>
     *     <li>El tipo de barco y su orientación.</li>
     *     <li>La posición relativa de la celda dentro del barco (proa, popa, cuerpo).</li>
     *     <li>El estado de la celda (intacta, tocada, hundida).</li>
     * </ul>
     *
     *
     * @param ship El objeto lógico del barco que se está dibujando.
     * @param cell La celda específica del tablero que corresponde a esta parte del barco.
     * @return Un {@link Node} de JavaFX que se añadirá al tablero visual.
     */
    Node createShape(Ship ship, Cell cell);
}