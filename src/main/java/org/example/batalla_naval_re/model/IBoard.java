package org.example.batalla_naval_re.model;

import org.example.batalla_naval_re.exceptions.PlacementException;

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
