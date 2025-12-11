package org.example.batalla_naval_re.persistence;

import java.io.*;

public class StatsManager {

    private static final String STATS_FILE = "player_stats.txt";

    public static void saveStats(String nickname, int sunkShips) throws GameFileException {
        try (PrintWriter out = new PrintWriter(new FileWriter(STATS_FILE))) {
            out.println(nickname);
            out.println(sunkShips);
        } catch (IOException e) {
            throw new GameFileException("No se pudieron guardar las estadísticas", e);
        }
    }

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
