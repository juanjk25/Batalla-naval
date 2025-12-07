package org.example.batalla_naval_re.model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final Board board;
    private int sunkCount;

    public Player(String name) {
        this.name = name;
        this.board = new Board();
        this.sunkCount = 0;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    public int getSunkCount() {
        return sunkCount;
    }

    public void incrementSunkCount() {
        sunkCount++;
    }

    public void setSunkCount(int sunkCount) {
        this.sunkCount = sunkCount;
    }

    @Override
    public String toString() {
        return String.format("Player[name=%s, sunkCount=%d]", name, sunkCount);
    }
}