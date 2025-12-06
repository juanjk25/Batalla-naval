package org.example.batallanaval.model;

import java.io.Serializable;

public interface IPlayer extends Serializable {
    String getNickname();
    Board getBoard();
    int getSunkCount();
    void incrementSunkCount();
}
