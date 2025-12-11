package org.example.batalla_naval_re.model;

import java.io.Serializable;
import java.util.List;

/**
 * Interfaz que define el comportamiento y propiedades básicas de un barco.
 * <p>
 * Proporciona métodos para acceder al tipo de barco, gestionar sus coordenadas
 * en el tablero y consultar o modificar su estado de hundimiento.
 * </p>
 */
public interface IShip extends Serializable {

    /**
     * Obtiene el tipo de barco, que determina su tamaño y apariencia.
     *
     * @return El {@link ShipType} del barco.
     */
    ShipType getType();

    /**
     * Obtiene la lista de coordenadas que ocupa este barco en el tablero.
     *
     * @return Una lista de arrays de enteros, donde cada array representa una posición {@code [fila, columna]}.
     */
    List<int[]> getCoords();

    /**
     * Añade una coordenada a la lista de posiciones ocupadas por el barco.
     *
     * @param r Fila de la coordenada.
     * @param c Columna de la coordenada.
     */
    void addCoord(int r, int c);

    /**
     * Verifica si el barco ocupa la coordenada especificada.
     *
     * @param r Fila a verificar.
     * @param c Columna a verificar.
     * @return {@code true} si el barco está posicionado en {@code (r, c)}, {@code false} en caso contrario.
     */
    boolean occupies(int r, int c);

    /**
     * Verifica si el barco ha sido hundido.
     *
     * @return {@code true} si el barco está hundido.
     */
    boolean isSunk();

    /**
     * Establece el estado de hundimiento del barco.
     *
     * @param sunk {@code true} para marcar el barco como hundido.
     */
    void setSunk(boolean sunk);
}