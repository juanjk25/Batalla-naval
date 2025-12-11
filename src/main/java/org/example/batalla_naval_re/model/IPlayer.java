package org.example.batalla_naval_re.model;

import java.io.Serializable;

/**
 * Interfaz que define el contrato para un jugador en el juego de Batalla Naval.
 * <p>
 * Un jugador posee una identidad (nickname), un tablero de juego propio
 * y un registro de su desempeño (cantidad de barcos enemigos hundidos).
 * </p>
 */
public interface IPlayer extends Serializable {

    /**
     * Obtiene el nombre o apodo del jugador.
     *
     * @return El nickname del jugador.
     */
    String getNickname();

    /**
     * Obtiene el tablero de juego asociado a este jugador.
     *
     * @return El objeto {@link Board} donde están colocados los barcos del jugador.
     */
    Board getBoard();

    /**
     * Obtiene la cantidad de barcos enemigos que este jugador ha hundido.
     *
     * @return El número de barcos hundidos.
     */
    int getSunkCount();

    /**
     * Incrementa en uno el contador de barcos enemigos hundidos.
     * <p>
     * Se debe llamar cuando un disparo realizado por este jugador resulta en el hundimiento
     * de un barco contrario.
     * </p>
     */
    void incrementSunkCount();
}