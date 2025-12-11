package org.example.batalla_naval_re.model;

/**
 * Interfaz que define las características básicas de un tipo de barco.
 * <p>
 * Abstrae las propiedades visuales y dimensionales de los distintos barcos disponibles en el juego.
 * </p>
 */
public interface IShipType {
    /**
     * Obtiene el tamaño del barco en número de celdas.
     *
     * @return La longitud del barco.
     */
    int getSize();

    /**
     * Obtiene el nombre legible para mostrar al usuario.
     *
     * @return El nombre del tipo de barco (ej. "Portaaviones").
     */
    String getDisplay();
}