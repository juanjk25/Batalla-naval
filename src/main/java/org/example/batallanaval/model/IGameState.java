package org.example.batallanaval.model;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public interface IGameState extends Serializable {
    Player getPlayer();
    Board getMachineBoard();
    boolean isGameOver();
    void setGameOver(boolean gameOver);
    Optional<Player> getMachine();
    AtomicInteger getMachineSunkCount();
}
