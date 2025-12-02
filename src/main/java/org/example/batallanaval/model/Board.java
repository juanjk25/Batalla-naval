package org.example.batallanaval.model;

import org.example.batallanaval.exceptions.PlacementException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int SIZE = 10;

    private final Cell[][] cells = new Cell[SIZE][SIZE];
    private final List<Ship> ships = new ArrayList<>();

    public Board() {
        for (int r=0;r<SIZE;r++) for (int c=0;c<SIZE;c++) cells[r][c] = new Cell();
    }

    public Cell getCell(int r, int c) {
        return cells[r][c];
    }

    public boolean canPlace(int r, int c, int length, boolean horizontal) {
        if (horizontal) {
            if (c + length > SIZE) return false;
            for (int i=0;i<length;i++) if (cells[r][c+i].isShip()) return false;
        } else {
            if (r + length > SIZE) return false;
            for (int i=0;i<length;i++) if (cells[r+i][c].isShip()) return false;
        }
        return true;
    }

    public void placeShip(Ship ship, int r, int c, boolean horizontal) throws PlacementException {
        int len = ship.getType().getSize();
        if(!canPlace(r,c,len,horizontal)) throw new PlacementException("No se puede colocar el barco aquí.");
        for (int i=0;i<len;i++) {
            int rr = horizontal ? r : r+i;
            int cc = horizontal ? c+i : c;
            cells[rr][cc].setShip(true);
            ship.addCoord(rr, cc);
        }
        ships.add(ship);
    }

    public boolean shoot(int r, int c) {
        Cell cell = cells[r][c];
        if (cell.isTried()) return false;
        if (cell.isShip()) {
            cell.setHit(true);
            return true;
        } else {
            cell.setMiss(true);
            return false;
        }
    }

    public boolean allShipsSunk() {
        for (Ship s : ships) {
            boolean sSunk = s.getCoords().stream().allMatch(p -> cells[p[0]][p[1]].isHit());
            if (!sSunk) return false;
        }
        return true;
    }

    public boolean checkAndMarkSunkAt(int r, int c) {
        for (Ship s : ships) {
            if (s.occupies(r,c)) {
                boolean sSunk = s.getCoords().stream().allMatch(p -> cells[p[0]][p[1]].isHit());
                if (sSunk) {
                    s.setSunk(true);
                    s.getCoords().forEach(p -> cells[p[0]][p[1]].setSunkPart(true));
                    return true;
                }
            }
        }
        return false;
    }

    public void randomPlaceAllShips() {
        // Place 1 carrier(4), 2 submarines(3), 3 destroyers(2), 4 frigates(1)
        Random rnd = new Random();
        int[] counts = {1,2,3,4};
        ShipType[] types = {ShipType.CARRIER, ShipType.SUBMARINE, ShipType.DESTROYER, ShipType.FRIGATE};
        for (int t=0;t<types.length;t++) {
            for (int k=0;k<counts[t];k++) {
                boolean placed=false;
                int tries=0;
                while (!placed && tries<2000) {
                    tries++;
                    int r = rnd.nextInt(SIZE);
                    int c = rnd.nextInt(SIZE);
                    boolean horiz = rnd.nextBoolean();
                    Ship ship = new Ship(types[t]);
                    if (canPlace(r,c, ship.getType().getSize(), horiz)) {
                        try { placeShip(ship, r, c, horiz); placed=true; } catch (PlacementException ignore) {}
                    }
                }
                if (!placed) throw new RuntimeException("No se pudo ubicar un barco aleatoriamente.");
            }
        }
    }

    public List<Ship> getShips() { return ships; }
}
