package org.example.batalla_naval_re.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ship implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ShipType type;
    private final List<Cell> cells;
    private boolean isHorizontal;
    private boolean isSunk;

    public Ship(ShipType type) {
        this.type = type;
        this.cells = new ArrayList<>();
        this.isHorizontal = true;
        this.isSunk = false;
    }

    // Añadir celda al barco
    public void addCell(Cell cell) {
        if (!cells.contains(cell)) {
            cells.add(cell);
            cell.setShip(this);
        }
    }

    // MÉTODO QUE FALTA: Notificar que una celda fue golpeada
    public void notifyHit(Cell hitCell) {
        // Marcar esta celda específica como golpeada
        if (cells.contains(hitCell)) {
            hitCell.setHit(true);

            // Verificar si todas las celdas del barco están golpeadas
            boolean allCellsHit = cells.stream().allMatch(Cell::isHit);
            if (allCellsHit) {
                markAsSunk();
            }
        }
    }

    // Marcar barco como hundido
    private void markAsSunk() {
        this.isSunk = true;
        // Marcar todas las celdas como hundidas
        cells.forEach(cell -> cell.setSunkPart(true));
    }

    // Verificar si el barco está hundido
    public boolean isSunk() {
        // Si ya está marcado como hundido, retornar true
        if (isSunk) return true;

        // Si no, verificar si todas las celdas están golpeadas
        isSunk = cells.stream().allMatch(Cell::isHit);
        if (isSunk) {
            markAsSunk();
        }
        return isSunk;
    }

    // Getters
    public ShipType getType() {
        return type;
    }

    // Para compatibilidad si usas getShape() en otros lugares
    public ShipType getShape() {
        return type;
    }

    public List<Cell> getCells() {
        return new ArrayList<>(cells);
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public int getSize() {
        return type.getSize();
    }

    public String getName() {
        return type.getDisplayName();
    }

    // Setters
    public void setHorizontal(boolean horizontal) {
        this.isHorizontal = horizontal;
    }

    public void setSunk(boolean sunk) {
        this.isSunk = sunk;
        if (sunk) {
            cells.forEach(cell -> cell.setSunkPart(true));
        }
    }

    // Verificar si contiene una celda específica
    public boolean containsCell(int row, int col) {
        return cells.stream()
                .anyMatch(cell -> cell.getRow() == row && cell.getCol() == col);
    }

    // Obtener posición de una celda en el barco (0-index)
    public int getCellPosition(Cell cell) {
        for (int i = 0; i < cells.size(); i++) {
            if (cells.get(i) == cell) {
                return i;
            }
        }
        return -1;
    }

    // Verificar si el barco puede colocarse en una posición
    public boolean canPlaceAt(Board board, int startRow, int startCol, boolean horizontal) {
        int size = getSize();

        for (int i = 0; i < size; i++) {
            int row = horizontal ? startRow : startRow + i;
            int col = horizontal ? startCol + i : startCol;

            // Verificar límites del tablero
            if (!board.isValidPosition(row, col)) {
                return false;
            }

            // Verificar que no haya otro barco
            if (board.getCell(row, col).isShip()) {
                return false;
            }
        }
        return true;
    }

    // Colocar el barco en el tablero
    public boolean placeAt(Board board, int startRow, int startCol, boolean horizontal) {
        if (!canPlaceAt(board, startRow, startCol, horizontal)) {
            return false;
        }

        this.isHorizontal = horizontal;
        cells.clear();

        int size = getSize();
        for (int i = 0; i < size; i++) {
            int row = horizontal ? startRow : startRow + i;
            int col = horizontal ? startCol + i : startCol;

            Cell cell = board.getCell(row, col);
            addCell(cell);
        }

        return true;
    }

    // Método auxiliar para obtener la orientación como texto
    public String getOrientation() {
        return isHorizontal ? "Horizontal" : "Vertical";
    }

    @Override
    public String toString() {
        return String.format("%s (Size: %d, Horizontal: %s, Sunk: %s, Cells: %d)",
                getName(), getSize(), isHorizontal, isSunk, cells.size());
    }
}