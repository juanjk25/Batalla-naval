package org.example.batalla_naval_re.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Board implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int SIZE = 10;

    private final Cell[][] grid;
    private final List<Ship> ships;
    private final Random random;

    public Board() {
        grid = new Cell[SIZE][SIZE];
        ships = new ArrayList<>();
        random = new Random();
        initializeGrid();
    }

    private void initializeGrid() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = new Cell(r, c);
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (isValidPosition(row, col)) return grid[row][col];
        throw new IndexOutOfBoundsException("Posición fuera del tablero");
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    // -----------------------------------
    //      DISPAROS
    // -----------------------------------
    public Cell.ShotResult shoot(int row, int col) {
        Cell cell = getCell(row, col);
        Cell.ShotResult result = cell.shoot();

        if (result == Cell.ShotResult.HIT) {
            Ship ship = cell.getShip();
            if (ship != null && ship.isSunk()) result = Cell.ShotResult.SUNK;
        }
        return result;
    }

    // -----------------------------------
    //      COLOCACIÓN MANUAL
    // -----------------------------------
    public boolean addShip(Ship ship, int row, int col, boolean horizontal) {
        if (!canPlaceShip(ship, row, col, horizontal)) return false;

        ship.placeAt(this, row, col, horizontal);

        if (!ships.contains(ship)) ships.add(ship);

        return true;
    }

    // 🛑 Sin tocar bordes ni diagonales
    public boolean isCellFreeForShipPlacement(int row, int col) {
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (isValidPosition(r, c) && grid[r][c].isShip()) return false;
            }
        }
        return true;
    }

    public boolean canPlaceShip(Ship ship, int startRow, int startCol, boolean horizontal) {
        int size = ship.getSize();

        for (int i = 0; i < size; i++) {
            int row = horizontal ? startRow : startRow + i;
            int col = horizontal ? startCol + i : startCol;

            if (!isValidPosition(row, col) || grid[row][col].isShip() || !isCellFreeForShipPlacement(row, col)) {
                return false;
            }
        }
        return true;
    }

    // -----------------------------------
    //      PROGRESO
    // -----------------------------------
    public boolean allShipsPlaced() {
        return ships.size() == 10 && ships.stream().allMatch(s -> s.getCells().size() == s.getSize());
    }

    public boolean allShipsSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    public List<Ship> getShips() {
        return new ArrayList<>(ships);
    }

    public void clear() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c].clear();
            }
        }
        ships.clear();
    }

    // -----------------------------------
    //      PARA LA MÁQUINA
    // -----------------------------------
    public void createShipsWithoutPlacement() {
        ships.clear();
        ships.add(new Ship(ShipType.CARRIER));
        ships.add(new Ship(ShipType.SUBMARINE));
        ships.add(new Ship(ShipType.SUBMARINE));
        ships.add(new Ship(ShipType.DESTROYER));
        ships.add(new Ship(ShipType.DESTROYER));
        ships.add(new Ship(ShipType.DESTROYER));
        ships.add(new Ship(ShipType.FRIGATE));
        ships.add(new Ship(ShipType.FRIGATE));
        ships.add(new Ship(ShipType.FRIGATE));
        ships.add(new Ship(ShipType.FRIGATE));
    }

    public boolean randomPlaceAllShips() {
        clear();
        List<Ship> shipsToPlace = createShipsForAI();
        Collections.shuffle(shipsToPlace, random);

        for (Ship ship : shipsToPlace) {
            if (!placeShipRandomly(ship)) {
                clear();
                return false;
            }
            ships.add(ship);
        }
        return true;
    }

    private List<Ship> createShipsForAI() {
        List<Ship> list = new ArrayList<>();
        list.add(new Ship(ShipType.CARRIER));
        list.add(new Ship(ShipType.SUBMARINE));
        list.add(new Ship(ShipType.SUBMARINE));
        list.add(new Ship(ShipType.DESTROYER));
        list.add(new Ship(ShipType.DESTROYER));
        list.add(new Ship(ShipType.DESTROYER));
        list.add(new Ship(ShipType.FRIGATE));
        list.add(new Ship(ShipType.FRIGATE));
        list.add(new Ship(ShipType.FRIGATE));
        list.add(new Ship(ShipType.FRIGATE));
        return list;
    }

    private boolean placeShipRandomly(Ship ship) {
        int maxAttempts = 100;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {

            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            boolean horizontal = random.nextBoolean();

            if (canPlaceShip(ship, row, col, horizontal)) {
                ship.placeAt(this, row, col, horizontal);
                return true;
            }
        }
        return false;
    }
}
