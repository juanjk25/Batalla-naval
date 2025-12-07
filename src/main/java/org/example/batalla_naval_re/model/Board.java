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

        // Inicializar celdas
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell getCell(int row, int col) {
        if (isValidPosition(row, col)) {
            return grid[row][col];
        }
        throw new IndexOutOfBoundsException(
                String.format("Posición (%d,%d) fuera del tablero %dx%d",
                        row, col, SIZE, SIZE)
        );
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    // Disparar en una celda
    public Cell.ShotResult shoot(int row, int col) {
        Cell cell = getCell(row, col);
        Cell.ShotResult result = cell.shoot();

        // Verificar si se hundió un barco
        if (result == Cell.ShotResult.HIT) {
            Ship ship = cell.getShip();
            if (ship != null && ship.isSunk()) {
                result = Cell.ShotResult.SUNK;
            }
        }

        return result;
    }

    // Añadir barco al tablero
    public boolean addShip(Ship ship, int row, int col, boolean horizontal) {
        return ship.placeAt(this, row, col, horizontal);
    }

    // Verificar si todos los barcos están hundidos
    public boolean allShipsSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    // Obtener lista de barcos
    public List<Ship> getShips() {
        return new ArrayList<>(ships);
    }

    // Obtener barco en una posición específica
    public Ship getShipAt(int row, int col) {
        Cell cell = getCell(row, col);
        return cell.getShip();
    }

    // Limpiar tablero
    public void clear() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j].clear();
            }
        }
        ships.clear();
    }

    // MÉTODO QUE FALTA: Colocar todos los barcos aleatoriamente
    public boolean randomPlaceAllShips() {
        clear(); // Limpiar tablero primero

        // Lista de barcos a colocar según el enunciado
        List<Ship> shipsToPlace = createShipsList();
        Collections.shuffle(shipsToPlace, random);

        // Intentar colocar cada barco
        for (Ship ship : shipsToPlace) {
            if (!placeShipRandomly(ship)) {
                // Si no se puede colocar un barco, limpiar y retornar false
                clear();
                return false;
            }
            ships.add(ship);
        }

        return true;
    }

    // Crear lista de barcos según el enunciado
    private List<Ship> createShipsList() {
        List<Ship> shipsList = new ArrayList<>();

        // 1 portaaviones (4 casillas)
        shipsList.add(new Ship(ShipType.CARRIER));

        // 2 submarinos (3 casillas cada uno)
        shipsList.add(new Ship(ShipType.SUBMARINE));
        shipsList.add(new Ship(ShipType.SUBMARINE));

        // 3 destructores (2 casillas cada uno)
        shipsList.add(new Ship(ShipType.DESTROYER));
        shipsList.add(new Ship(ShipType.DESTROYER));
        shipsList.add(new Ship(ShipType.DESTROYER));

        // 4 fragatas (1 casilla cada una)
        shipsList.add(new Ship(ShipType.FRIGATE));
        shipsList.add(new Ship(ShipType.FRIGATE));
        shipsList.add(new Ship(ShipType.FRIGATE));
        shipsList.add(new Ship(ShipType.FRIGATE));

        return shipsList;
    }

    // Colocar un barco aleatoriamente
    private boolean placeShipRandomly(Ship ship) {
        int maxAttempts = 100; // Límite de intentos para evitar bucle infinito

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            // Generar posición y orientación aleatoria
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            boolean horizontal = random.nextBoolean();

            // Verificar si el barco cabe
            if (canPlaceShip(ship, row, col, horizontal)) {
                // Colocar el barco
                return ship.placeAt(this, row, col, horizontal);
            }
        }

        return false; // No se pudo colocar después de maxAttempts
    }

    // Verificar si un barco puede colocarse en una posición
    private boolean canPlaceShip(Ship ship, int startRow, int startCol, boolean horizontal) {
        int size = ship.getSize();

        for (int i = 0; i < size; i++) {
            int row = horizontal ? startRow : startRow + i;
            int col = horizontal ? startCol + i : startCol;

            // Verificar límites
            if (!isValidPosition(row, col)) {
                return false;
            }

            // Verificar que no haya otro barco
            if (grid[row][col].isShip()) {
                return false;
            }

            // Verificar casillas adyacentes (opcional, para que no se toquen)
            if (!isCellFreeForShip(row, col)) {
                return false;
            }
        }

        return true;
    }

    // Verificar que una celda y sus adyacentes estén libres
    private boolean isCellFreeForShip(int row, int col) {
        // Verificar la celda y todas las adyacentes
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (isValidPosition(r, c) && grid[r][c].isShip()) {
                    return false;
                }
            }
        }
        return true;
    }

    // Contar barcos hundidos
    public int getSunkShipsCount() {
        return (int) ships.stream().filter(Ship::isSunk).count();
    }

    // Contar barcos restantes
    public int getRemainingShipsCount() {
        return ships.size() - getSunkShipsCount();
    }

    // Método para debug - imprimir tablero
    public void printBoard(boolean showShips) {
        System.out.println("  " + "0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < SIZE; j++) {
                Cell cell = grid[i][j];
                char symbol = getCellSymbol(cell, showShips);
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
    }

    private char getCellSymbol(Cell cell, boolean showShips) {
        if (cell.isSunkPart()) return 'X';
        if (cell.isHit()) return '•';
        if (cell.isMiss()) return '~';
        if (showShips && cell.isShip()) return 'S';
        return '.';
    }

    // Método para obtener el estado del tablero como texto
    public String getBoardState(boolean showShips) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ").append("0 1 2 3 4 5 6 7 8 9\n");
        for (int i = 0; i < SIZE; i++) {
            sb.append(i).append(" ");
            for (int j = 0; j < SIZE; j++) {
                Cell cell = grid[i][j];
                char symbol = getCellSymbol(cell, showShips);
                sb.append(symbol).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Verifica y marca si un barco está hundido en una posición
     */
    public boolean checkAndMarkSunkAt(int row, int col) {
        Cell cell = getCell(row, col);
        Ship ship = cell.getShip();

        if (ship != null) {
            boolean sunk = ship.isSunk();
            if (sunk) {
                // Marcar todas las celdas del barco como hundidas
                ship.getCells().forEach(c -> c.setSunkPart(true));
            }
            return sunk;
        }

        return false;
    }

    /**
     * Método alternativo para disparar que retorna boolean (para compatibilidad)
     */
    public boolean shootBoolean(int row, int col) {
        Cell.ShotResult result = shoot(row, col);
        return result == Cell.ShotResult.HIT || result == Cell.ShotResult.SUNK;
    }
}