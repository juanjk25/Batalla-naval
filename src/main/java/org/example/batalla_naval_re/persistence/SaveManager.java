package org.example.batalla_naval_re.persistence;

import org.example.batalla_naval_re.model.GameState;

import java.io.*;

/**
 * Gestiona el guardado y carga del estado del juego.
 * Usa archivos serializados (.dat) y excepciones personalizadas para robustez.
 */
public class SaveManager {

    private static final String SAVE_FILE = "batalla_save.dat";

    /**
     * Guarda el estado actual del juego en un archivo binario.
     *
     * @param state estado del juego a guardar
     * @throws GameFileException si ocurre un error al escribir el archivo
     */
    public static void saveState(GameState state) throws GameFileException {
        if (state == null) {
            throw new GameFileException("No se puede guardar un estado nulo.");
        }

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {

            oos.writeObject(state);

        } catch (IOException e) {
            throw new GameFileException("Error al guardar la partida.", e);
        }
    }

    /**
     * Alias necesario para GameController.
     * Llama a loadLastState().
     */
    public static GameState loadState() throws GameFileException {
        return loadLastState();
    }


    /**
     * Carga el último estado guardado del juego.
     *
     * @return el GameState cargado o null si no existe guardado previo
     * @throws GameFileException si ocurre un error de lectura o archivo corrupto
     */
    public static GameState loadLastState() throws GameFileException {
        File file = new File(SAVE_FILE);

        if (!file.exists()) {
            return null; // no hay partida previa
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {

            Object obj = ois.readObject();

            if (!(obj instanceof GameState state)) {
                throw new GameFileException("El archivo de partida está corrupto.");
            }

            return state;

        } catch (IOException | ClassNotFoundException e) {
            throw new GameFileException("Error al cargar la partida.", e);
        }
    }

    /**
     * Elimina la partida guardada.
     *
     * @throws GameFileException si ocurre un error al borrar el archivo
     */
    public static void deleteSave() throws GameFileException {
        File file = new File(SAVE_FILE);
        if (file.exists() && !file.delete()) {
            throw new GameFileException("No se pudo eliminar el archivo de guardado.");
        }
    }
}
