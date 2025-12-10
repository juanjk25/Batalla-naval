package org.example.batalla_naval_re.model;

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

    /**
     * Crea un nuevo juego.
     * - Los barcos del jugador NO se colocan automáticamente.
     * - La máquina sí coloca sus barcos automáticamente.
     */
    public static GameState newGame(String nickname) {
        Player p = new Player(nickname);
        // Crear barcos vacíos para que el jugador los coloque manualmente
        p.getBoard().createShipsWithoutPlacement();

        Board machine = new Board();
        // Colocar automáticamente los barcos de la máquina
        machine.randomPlaceAllShips();

        return new GameState(p, machine);
    }

    // Getters y setters
    public Player getPlayer() {
        return player;
    }

    public Board getMachineBoard() {
        return machineBoard;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Optional<Player> getMachine() {
        return Optional.empty();
    }

    public AtomicInteger getMachineSunkCount() {
        if (machineSunkCount == null) machineSunkCount = new AtomicInteger(0);
        return machineSunkCount;
    }
}
