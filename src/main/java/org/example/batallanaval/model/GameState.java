package org.example.batallanaval.model;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    private Player player;
    private Board machineBoard;
    private boolean gameOver = false;
    private transient AtomicInteger machineSunkCount = new AtomicInteger(0);

    public GameState(Player player, Board machineBoard) {
        this.player = player;
        this.machineBoard = machineBoard;
    }

    public static GameState newGame(String nickname) {
        Player p = new Player(nickname);
        Board machine = new Board();
        // Place player's ships randomly (could add placement UI later). For now: both random.
        p.getBoard().randomPlaceAllShips();
        machine.randomPlaceAllShips();
        return new GameState(p, machine);
    }

    public Player getPlayer() { return player; }
    public Board getMachineBoard() { return machineBoard; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public Optional<Player> getMachine() { return Optional.empty(); }
    public AtomicInteger getMachineSunkCount() {
        if (machineSunkCount==null) machineSunkCount = new AtomicInteger(0);
        return machineSunkCount;
    }
}
