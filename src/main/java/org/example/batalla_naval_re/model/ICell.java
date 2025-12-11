package org.example.batalla_naval_re.model;

import java.io.Serializable;

/**
 * Interfaz que define el contrato para una celda dentro del tablero de Batalla Naval.
 * <p>
 * Proporciona los métodos necesarios para consultar y modificar el estado de una casilla,
 * incluyendo si contiene un barco, si ha sido atacada, si el disparo fue acertado o fallido,
 * y su visibilidad para el jugador.
 * </p>
 */
public interface ICell extends Serializable {

    /**
     * Verifica si la celda contiene una parte de un barco.
     *
     * @return {@code true} si hay un barco en esta celda.
     */
    boolean isShip();

    /**
     * Define si la celda contiene un barco.
     *
     * @param ship {@code true} para colocar un barco, {@code false} para dejarla vacía.
     */
    void setShip(boolean ship);

    /**
     * Verifica si la celda ha recibido un impacto directo sobre un barco.
     *
     * @return {@code true} si la celda fue atacada y contenía un barco.
     */
    boolean isHit();

    /**
     * Marca la celda como impactada.
     *
     * @param hit {@code true} para establecer el estado de impacto.
     */
    void setHit(boolean hit);

    /**
     * Verifica si la celda ha recibido un disparo fallido (agua).
     *
     * @return {@code true} si la celda fue atacada pero estaba vacía.
     */
    boolean isMiss();

    /**
     * Marca la celda como disparo fallido (agua).
     *
     * @param miss {@code true} para establecer el estado de fallo.
     */
    void setMiss(boolean miss);

    /**
     * Verifica si la celda pertenece a un barco que ha sido completamente hundido.
     *
     * @return {@code true} si el barco en esta celda está hundido.
     */
    boolean isSunkPart();

    /**
     * Marca la celda como parte de un barco hundido.
     *
     * @param sunkPart {@code true} para indicar que el barco asociado se ha hundido.
     */
    void setSunkPart(boolean sunkPart);

    /**
     * Verifica si la celda ya ha sido atacada, independientemente del resultado (acierto o fallo).
     *
     * @return {@code true} si la celda ya ha sido jugada.
     */
    boolean isTried();

    /**
     * Verifica si el contenido de la celda es visible para el jugador.
     * Útil para modos de depuración o para revelar el tablero al final del juego.
     *
     * @return {@code true} si la celda es visible.
     */
    boolean isVisibleToPlayer();

    /**
     * Establece la visibilidad de la celda para el jugador.
     *
     * @param visibleToPlayer {@code true} para hacer visible el contenido de la celda.
     */
    void setVisibleToPlayer(boolean visibleToPlayer);
}