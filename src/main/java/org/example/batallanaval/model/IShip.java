package org.example.batallanaval.model;

import java.io.Serializable;
import java.util.List;

public interface IShip extends Serializable {
    ShipType getType();
    List<int[]> getCoords();
    void addCoord(int r, int c);
    boolean occupies(int r, int c);
    boolean isSunk();
    void setSunk(boolean sunk);
}
