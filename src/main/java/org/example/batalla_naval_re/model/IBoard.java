package org.example.batalla_naval_re.model;

import org.example.batalla_naval_re.exceptions.PlacementException;

import java.io.Serializable;
import java.util.List;

/**
 * Interfaz que define las operaciones fundamentales de un tablero de Batalla Naval.
 * <p>
 * Proporciona un contrato para gestionar las celdas, verificar reglas de colocación,
 * realizar disparos y controlar el estado de los barcos en el tablero.
 * </p>
 */
public interface IBoard extends Serializable {

    /**
     * Obtiene la celda en una posición específica.
     *
     * @param r indice de la fila.
     * @param c indice de la columna.
     * @return El objeto {@link Cell} en esa coordenada.
     */
    Cell getCell(int r, int c);

    /**
     * Verifica si es posible colocar un barco de cierta longitud en una posición dada.
     * <p>
     * Comprueba límites del tablero y si las celdas necesarias están libres.
     * </p>
     *
     * @param r          Fila inicial.
     * @param c          Columna inicial.
     * @param length     Longitud del barco.
     * @param horizontal {@code true} si la orientación es horizontal.
     * @return {@code true} si la colocación es válida.
     */
    boolean canPlace(int r, int c, int length, boolean horizontal);

    /**
     * Coloca un barco en el tablero de manera definitiva.
     *
     * @param ship       El objeto {@link Ship} a colocar.
     * @param r          Fila de inicio.
     * @param c          Columna de inicio.
     * @param horizontal Orientación del barco.
     * @throws PlacementException Si la colocación viola alguna regla del juego.
     */
    void placeShip(Ship ship, int r, int c, boolean horizontal) throws PlacementException;

    /**
     * Realiza un disparo en la coordenada indicada.
     *
     * @param r Fila objetivo.
     * @param c Columna objetivo.
     * @return {@code true} si el disparo impactó en un barco (HIT), {@code false} si fue agua (MISS).
     */
    boolean shoot(int r, int c);

    /**
     * Verifica si todos los barcos del tablero han sido hundidos.
     * Esta es la condición principal de victoria/derrota.
     *
     * @return {@code true} si no quedan barcos a flote.
     */
    boolean allShipsSunk();

    /**
     * Verifica una celda y, si es parte de un barco, comprueba si ese barco se hundió con el último impacto.
     *
     * @param r Fila a verificar.
     * @param c Columna a verificar.
     * @return {@code true} si el barco en esa posición acaba de hundirse.
     */
    boolean checkAndMarkSunkAt(int r, int c);

    /**
     * Coloca automáticamente todos los barcos de la flota en posiciones aleatorias válidas.
     * Útil para inicializar el tablero de la máquina.
     */
    void randomPlaceAllShips();

    /**
     * Obtiene la lista de todos los barcos presentes en el tablero.
     *
     * @return Lista de objetos {@link Ship}.
     */
    List<Ship> getShips();
}