package org.example.batalla_naval_re.persistence;

import org.example.batalla_naval_re.model.GameState;
import org.example.batalla_naval_re.persistence.GameFileException;
import org.example.batalla_naval_re.persistence.SaveManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SaveManagerTest {

    @BeforeEach
    void cleanBefore() throws GameFileException {
        SaveManager.deleteSave();
    }

    @Test
    void saveAndLoadGameState_success() throws GameFileException {
        GameState original = GameState.newGame("Tester");

        SaveManager.saveState(original);
        GameState loaded = SaveManager.loadLastState();

        assertNotNull(loaded);
        assertEquals(original.getPlayer().getName(), loaded.getPlayer().getName());
    }

    @Test
    void saveState_null_throwsException() {
        assertThrows(GameFileException.class, () -> {
            SaveManager.saveState(null);
        });
    }

    @Test
    void loadWithoutFile_returnsNull() throws GameFileException {
        GameState state = SaveManager.loadLastState();
        assertNull(state);
    }
}
