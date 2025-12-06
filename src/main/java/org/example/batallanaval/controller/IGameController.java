package org.example.batallanaval.controller;

import org.example.batallanaval.model.GameState;

import java.io.IOException;

public interface IGameController {
    void initState(GameState state, boolean revealMachine);
    void onBack() throws IOException;
}
