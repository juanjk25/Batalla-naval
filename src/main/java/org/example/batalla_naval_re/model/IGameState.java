package org.example.batalla_naval_re.model;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Interfaz que define el contrato para el estado del juego.
 * <p>
 * Proporciona acceso a los componentes principales de una partida en curso,
 * como el jugador humano, el tablero de la máquina y el estado de finalización del juego.
 * </p>
 */
public interface IGameState extends Serializable {

    /**
     * Obtiene el jugador humano asociado a la partida.
     *
     * @return El objeto {@link Player}.
     */
    Player getPlayer();

    /**
     * Obtiene el tablero controlado por la máquina (IA).
     *
     * @return El objeto {@link Board} de la máquina.
     */
    Board getMachineBoard();

    /**
     * Verifica si la partida ha terminado.
     *
     * @return {@code true} si hay un ganador, {@code false} si la partida continúa.
     */
    boolean isGameOver();

    /**
     * Establece el estado de finalización del juego.
     *
     * @param gameOver {@code true} para finalizar la partida.
     */
    void setGameOver(boolean gameOver);

    /**
     * Obtiene el jugador máquina, si está modelado como un objeto {@link Player}.
     * <p>
     * Este método devuelve un {@link Optional} ya que en algunas implementaciones
     * la máquina puede ser representada solo por su tablero y no por un objeto Player completo.
     * </p>
     *
     * @return Un {@link Optional} que contiene al jugador máquina si existe.
     */
    Optional<Player> getMachine();

    /**
     * Obtiene el contador atómico de barcos hundidos de la máquina.
     * <p>
     * Permite llevar un registro thread-safe del progreso de la máquina contra el jugador.
     * </p>
     *
     * @return Un {@link AtomicInteger} con la cantidad de barcos hundidos.
     */
    AtomicInteger getMachineSunkCount();
}