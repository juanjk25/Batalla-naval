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

    /** 🔥 Nuevo: Notificación correcta al barco cuando recibe impacto */
    public void notifyHit(Cell hitCell) {
        if (!cells.contains(hitCell)) return;

        hitCell.setHit(true);

        boolean allHit = cells.stream().allMatch(Cell::isHit);
        if (allHit) markAsSunk();
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

    public void setHorizontal(boolean horizontal) {
        this.isHorizontal = horizontal;
    }

    /** ✔ Lo que hacía mal antes: evitar superposición y tocar bordes */
    public boolean canPlaceAt(Board board, int startRow, int startCol, boolean horizontal) {
        int size = getSize();

        for (int i = 0; i < size; i++) {
            int row = horizontal ? startRow : startRow + i;
            int col = horizontal ? startCol + i : startCol;

            if (!board.isValidPosition(row, col)) return false;

            if (board.getCell(row, col).isShip()) return false;

            // Evita que queden pegados (regla oficial)
            if (!board.isCellFreeForShipPlacement(row, col)) return false;
        }
        return true;
    }

    public boolean placeAt(Board board, int startRow, int startCol, boolean horizontal) {
        if (!canPlaceAt(board, startRow, startCol, horizontal)) return false;

        this.isHorizontal = horizontal;
        cells.clear();

        for (int i = 0; i < getSize(); i++) {
            int row = horizontal ? startRow : startRow + i;
            int col = horizontal ? startCol + i : startCol;

            Cell cell = board.getCell(row, col);
            addCell(cell);
        }
        return true;
    }

    @Override
    public String toString() {
        return "%s [%d celdas]".formatted(getName(), getSize());
    }
}
