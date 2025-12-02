package org.example.batallanaval.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ship implements Serializable {
    private static final long serialVersionUID = 1L;
    private ShipType type;
    private List<int[]> coords = new ArrayList<>(); // list of {row,col}
    private boolean sunk = false;

    public Ship(ShipType type) { this.type = type; }

    public ShipType getType() { return type; }
    public List<int[]> getCoords() { return coords; }
    public void addCoord(int r, int c) { coords.add(new int[]{r,c}); }
    public boolean occupies(int r, int c) {
        return coords.stream().anyMatch(p -> p[0]==r && p[1]==c);
    }
    public boolean isSunk() { return sunk; }
    public void setSunk(boolean sunk) { this.sunk = sunk; }
}
