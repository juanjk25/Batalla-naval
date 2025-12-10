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

    public void addCell(Cell cell) {
        if (!cells.contains(cell)) {
            cells.add(cell);
            cell.setShip(this);
        }
    }

    // ✅ El barco recibe impacto SOLO desde Cell
    public void notifyHit(Cell hitCell) {
        if (!cells.contains(hitCell) || hitCell.isHit()) return;

        hitCell.setHit(true);

        if (cells.stream().allMatch(Cell::isHit)) {
            markAsSunk();
        }
    }

    private void markAsSunk() {
        this.isSunk = true;
        for (Cell c : cells) {
            c.setSunkPart(true);
        }
    }

    public boolean isSunk() {
        return isSunk;
    }

    public ShipType getType() {
        return type;
    }

    public int getSize() {
        return type.getSize();
    }

    public String getName() {
        return type.getDisplayName();
    }

    public List<Cell> getCells() {
        return new ArrayList<>(cells);
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.isHorizontal = horizontal;
    }

    public boolean placeAt(Board board, int startRow, int startCol, boolean horizontal) {
        if (!canPlaceAt(board, startRow, startCol, horizontal)) return false;

        this.isHorizontal = horizontal;
        cells.clear();

        for (int i = 0; i < getSize(); i++) {
            int row = horizontal ? startRow : startRow + i;
            int col = horizontal ? startCol + i : startCol;

            addCell(board.getCell(row, col));
        }
        return true;
    }

    public boolean canPlaceAt(Board board, int startRow, int startCol, boolean horizontal) {
        for (int i = 0; i < getSize(); i++) {
            int row = horizontal ? startRow : startRow + i;
            int col = horizontal ? startCol + i : startCol;

            if (!board.isValidPosition(row, col)) return false;
            if (board.getCell(row, col).isShip()) return false;
            if (!board.isCellFreeForShipPlacement(row, col)) return false;
        }
        return true;
    }
}
