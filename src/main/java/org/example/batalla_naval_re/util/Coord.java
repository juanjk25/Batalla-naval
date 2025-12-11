package org.example.batalla_naval_re.util;

/**
 * Clase utilitaria inmutable que representa un par de coordenadas (fila, columna).
 * <p>
 * Se utiliza para simplificar el paso de posiciones dentro del tablero de juego
 * como un solo objeto.
 * </p>
 */
public class Coord {
    /**
     * El índice de la fila.
     */
    public final int row;

    /**
     * El índice de la columna.
     */
    public final int col;

    /**
     * Crea una nueva instancia de coordenada.
     *
     * @param row El índice de la fila.
     * @param col El índice de la columna.
     */
    public Coord(int row, int col) { this.row = row; this.col = col; }
}