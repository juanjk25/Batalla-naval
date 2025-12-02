package org.example.batallanaval.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nickname;
    private Board board;
    private int sunkCount = 0;

    public Player(String nickname) {
        this.nickname = nickname;
        this.board = new Board();
    }

    public String getNickname() { return nickname; }
    public Board getBoard() { return board; }
    public int getSunkCount() { return sunkCount; }
    public void incrementSunkCount() { sunkCount++; }
}
