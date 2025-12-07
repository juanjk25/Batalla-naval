package org.example.batalla_naval_re.view.shapes;

import javafx.scene.Node;
import org.example.batalla_naval_re.model.Cell;
import org.example.batalla_naval_re.model.Ship;

public interface IShipShape {
    /**
     * Crea la figura 2D del barco
     * La implementación debe manejar los diferentes estados (intacto, golpeado, hundido)
     * basándose en el estado de la celda
     */
    Node createShape(Ship ship, Cell cell);
}