package org.example.batalla_naval_re.model;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Representa el estado completo de una partida de Batalla Naval.
 * <p>
 * Esta clase actúa como el objeto principal de transferencia y persistencia,
 * conteniendo la información del jugador, el tablero de la máquina y el estado
 * actual del juego (si ha terminado o no). Es serializable para facilitar
 * el guardado y carga de partidas.
 * </p>
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * El jugador humano, que contiene su propio tablero y estadísticas.
     */
    private Player player;

    /**
     * El tablero controlado por la máquina (IA).
     */
    private Board machineBoard;

    /**
     * Indica si la partida ha finalizado (alguien ha ganado).
     */
    private boolean gameOver = false;

    /**
     * Contador atómico para rastrear el número de barcos hundidos de la máquina.
     * Marcado como transient porque {@link AtomicInteger} no es Serializable por defecto,
     * o se prefiere reiniciar/recalcular al cargar.
     */
    private transient AtomicInteger machineSunkCount = new AtomicInteger(0);

    /**
     * Constructor para inicializar un estado de juego con un jugador y un tablero de máquina.
     *
     * @param player       El objeto {@link Player} que representa al usuario.
     * @param machineBoard El objeto {@link Board} que representa a la IA.
     */
    public GameState(Player player, Board machineBoard) {
        this.player = player;
        this.machineBoard = machineBoard;
    }

    /**
     * Crea una nueva instancia de juego configurada desde cero.
     * <p>
     * Pasos que realiza:
     * <ul>
     *     <li>Crea un jugador con el apodo proporcionado.</li>
     *     <li>Inicializa la lista de barcos del jugador (sin colocarlos en el tablero) para la fase de preparación manual.</li>
     *     <li>Crea el tablero de la máquina y coloca sus barcos de forma aleatoria automáticamente.</li>
     * </ul>
     *
     *
     * @param nickname El nombre o apodo del jugador humano.
     * @return Una nueva instancia de {@code GameState} lista para comenzar la fase de colocación.
     */
    public static GameState newGame(String nickname) {
        Player p = new Player(nickname);
        // Crear barcos vacíos para que el jugador los coloque manualmente
        p.getBoard().createShipsWithoutPlacement();

        Board machine = new Board();
        // Colocar automáticamente los barcos de la máquina
        machine.randomPlaceAllShips();

        return new GameState(p, machine);
    }

    // Getters y setters

    /**
     * Obtiene el jugador humano de la partida.
     * @return Objeto {@link Player}.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Obtiene el tablero de la máquina.
     * @return Objeto {@link Board}.
     */
    public Board getMachineBoard() {
        return machineBoard;
    }

    /**
     * Verifica si el juego ha terminado.
     * @return {@code true} si hay un ganador, {@code false} si la partida sigue en curso.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Establece el estado de fin de juego.
     * @param gameOver {@code true} para finalizar la partida.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Método auxiliar para obtener la representación de la máquina como jugador (si fuera necesario).
     * Actualmente retorna {@code Optional.empty()}.
     *
     * @return Un {@link Optional} vacío.
     */
    public Optional<Player> getMachine() {
        return Optional.empty();
    }

    /**
     * Obtiene el contador de barcos hundidos de la máquina.
     * <p>
     * Si el contador es nulo (por ejemplo, después de la deserialización), se reinicializa a 0.
     * </p>
     *
     * @return El {@link AtomicInteger} que lleva la cuenta.
     */
    public AtomicInteger getMachineSunkCount() {
        if (machineSunkCount == null) machineSunkCount = new AtomicInteger(0);
        return machineSunkCount;
    }
}