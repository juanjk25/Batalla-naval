package org.example.batallanaval.model;

import org.example.batallanaval.exceptions.PlacementException;

import java.io.Serializable;
import java.util.List;

public interface IBoard extends Serializable {
    Cell getCell(int r, int c);
    boolean canPlace(int r, int c, int length, boolean horizontal);
    void placeShip(Ship ship, int r, int c, boolean horizontal) throws PlacementException;
    boolean shoot(int r, int c);
    boolean allShipsSunk();
    boolean checkAndMarkSunkAt(int r, int c);
    void randomPlaceAllShips();
    List<Ship> getShips();
}
