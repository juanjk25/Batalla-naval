package org.example.batalla_naval_re.controller;

import org.example.batalla_naval_re.model.GameState;

import java.io.IOException;

public interface IGameController {
    void initState(GameState state, boolean revealMachine);
    void onBack() throws IOException;
}
