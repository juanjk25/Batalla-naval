package org.example.batalla_naval_re.persistence;

import org.example.batalla_naval_re.model.GameState;

import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "batalla_save.dat";
    public static void saveState(GameState state) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(state);
        }
    }

    public static GameState loadLastState() throws IOException, ClassNotFoundException {
        File f = new File(SAVE_FILE);
        if (!f.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (GameState) ois.readObject();
        }
    }
}
