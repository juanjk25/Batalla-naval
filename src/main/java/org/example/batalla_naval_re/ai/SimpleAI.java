package org.example.batalla_naval_re.ai;

import org.example.batalla_naval_re.model.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Representa una Inteligencia Artificial básica para el juego de Batalla Naval.
 * <p>
 * Esta IA utiliza una estrategia de disparos aleatorios, manteniendo un registro
 * de las coordenadas disponibles para evitar disparar dos veces al mismo lugar.
 * </p>
 */
public class SimpleAI {
    /**
     * El tablero del oponente contra el cual la IA está jugando.
     */
    private final Board opponentBoard; // AI shoots against this board

    /**
     * Lista de todas las coordenadas posibles (fila, columna) a las que la IA puede disparar.
     * Se inicializa con todas las celdas del tablero y se reducen a medida que se realizan disparos.
     */
    private final List<int[]> possibleShots = new ArrayList<>();

    private final Random rnd = new Random();

    /**
     * Constructor que inicializa la IA.
     * Prepara la lista de disparos posibles llenándola con todas las coordenadas del tablero.
     *
     * @param opponentBoard El tablero del jugador oponente que será atacado por esta IA.
     */
    public SimpleAI(Board opponentBoard) {
        this.opponentBoard = opponentBoard;
        for (int r=0;r<Board.SIZE;r++) for (int c=0;c<Board.SIZE;c++) possibleShots.add(new int[]{r,c});
    }

    /**
     * Determina las coordenadas del siguiente disparo de la IA.
     * <p>
     * Selecciona aleatoriamente una coordenada de la lista de disparos posibles.
     * Verifica si la celda ya ha sido intentada en el tablero; si es así, busca recursivamente
     * otra coordenada válida.
     * </p>
     *
     * @return Un array de enteros de tamaño 2, donde el índice 0 es la fila y el índice 1 es la columna.
     *         Retorna {0,0} si no quedan disparos posibles (caso borde).
     */
    public int[] nextShot() {
        if (possibleShots.isEmpty()) return new int[]{0,0};
        int idx = rnd.nextInt(possibleShots.size());
        int[] shot = possibleShots.remove(idx);
        // ensure not tried
        Cell cell = opponentBoard.getCell(shot[0], shot[1]);
        if (cell.isTried()) return nextShot();
        return shot;
    }

    /**
     * Recibe el resultado del último disparo realizado.
     * <p>
     * Este método permite a la IA aprender o ajustar su estrategia basándose en si acertó o falló.
     * Actualmente, esta implementación básica no utiliza esta información para sesgar los futuros disparos.
     * </p>
     *
     * @param shot Las coordenadas del disparo realizado {fila, columna}.
     * @param hit  {@code true} si el disparo impactó un barco, {@code false} si fue agua.
     */
    public void reportResult(int[] shot, boolean hit) {
        // optional: could bias further shots
    }
}
