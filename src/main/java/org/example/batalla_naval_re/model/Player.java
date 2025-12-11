package org.example.batalla_naval_re.model;

import java.io.Serializable;

/**
 * Representa a un jugador dentro del juego de Batalla Naval.
 * <p>
 * Cada jugador tiene un nombre, un tablero propio donde se colocan sus barcos,
 * y un contador de los barcos enemigos que ha logrado hundir.
 * </p>
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * El nombre o apodo del jugador.
     */
    private final String name;

    /**
     * El tablero de juego perteneciente al jugador.
     */
    private final Board board;

    /**
     * Contador de barcos enemigos hundidos por este jugador.
     */
    private int sunkCount;

    /**
     * Constructor que inicializa un nuevo jugador.
     * <p>
     * Crea un nuevo tablero vacío y establece el contador de hundidos a cero.
     * </p>
     *
     * @param name El nombre del jugador.
     */
    public Player(String name) {
        this.name = name;
        this.board = new Board();
        this.sunkCount = 0;
    }

    // Getters

    /**
     * Obtiene el nombre del jugador.
     *
     * @return El nombre del jugador.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el tablero del jugador.
     *
     * @return El objeto {@link Board} del jugador.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Obtiene la cantidad de barcos enemigos que el jugador ha hundido.
     *
     * @return El número de barcos hundidos.
     */
    public int getSunkCount() {
        return sunkCount;
    }

    /**
     * Incrementa en 1 el contador de barcos hundidos.
     * <p>
     * Se debe llamar cada vez que el jugador hunde exitosamente un barco oponente.
     * </p>
     */
    public void incrementSunkCount() {
        sunkCount++;
    }

    /**
     * Establece manualmente el contador de barcos hundidos.
     * Útil para cargar partidas guardadas o reiniciar estadísticas.
     *
     * @param sunkCount El nuevo valor del contador.
     */
    public void setSunkCount(int sunkCount) {
        this.sunkCount = sunkCount;
    }

    /**
     * Devuelve una representación en cadena del jugador.
     *
     * @return Una cadena con el formato "Player[name=..., sunkCount=...]".
     */
    @Override
    public String toString() {
        return String.format("Player[name=%s, sunkCount=%d]", name, sunkCount);
    }
}