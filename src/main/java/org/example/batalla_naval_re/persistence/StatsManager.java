package org.example.batalla_naval_re.persistence;

import java.io.*;

/**
 * Gestor de persistencia para las estadísticas del jugador.
 * <p>
 * Se encarga de guardar y recuperar información básica del jugador (apodo y
 * conteo total de barcos hundidos) en un archivo de texto plano.
 * </p>
 */
public class StatsManager {

    /**
     * Nombre del archivo donde se guardan las estadísticas.
     */
    private static final String STATS_FILE = "player_stats.txt";

    /**
     * Guarda las estadísticas actuales del jugador en el archivo.
     * <p>
     * Sobrescribe el archivo existente con los nuevos datos.
     * </p>
     *
     * @param nickname  El nombre o apodo del jugador.
     * @param sunkShips La cantidad total de barcos que ha hundido.
     * @throws GameFileException Si ocurre un error de escritura al intentar guardar el archivo.
     */
    public static void saveStats(String nickname, int sunkShips) throws GameFileException {
        try (PrintWriter out = new PrintWriter(new FileWriter(STATS_FILE))) {
            out.println(nickname);
            out.println(sunkShips);
        } catch (IOException e) {
            throw new GameFileException("No se pudieron guardar las estadísticas", e);
        }
    }

    /**
     * Carga las estadísticas almacenadas desde el archivo.
     * <p>
     * Si el archivo no existe, retorna valores por defecto ("Jugador", "0").
     * </p>
     *
     * @return Un arreglo de Strings de tamaño 2:
     *         <ul>
     *             <li>indice 0: Nombre del jugador.</li>
     *             <li>indice 1: Cantidad de barcos hundidos (como String).</li>
     *         </ul>
     * @throws GameFileException Si ocurre un error de lectura al procesar el archivo.
     */
    public static String[] loadStats() throws GameFileException {
        File f = new File(STATS_FILE);
        if (!f.exists()) {
            return new String[]{"Jugador", "0"};
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String name = reader.readLine();
            String sunk = reader.readLine();
            return new String[]{name, sunk};

        } catch (IOException e) {
            throw new GameFileException("Error al cargar las estadísticas", e);
        }
    }
}