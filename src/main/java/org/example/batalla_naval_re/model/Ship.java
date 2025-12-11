package org.example.batalla_naval_re.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un barco dentro del juego de Batalla Naval.
 * <p>
 * Esta clase gestiona el estado individual de un barco, incluyendo su tipo,
 * las celdas que ocupa en el tablero, su orientación y si ha sido hundido.
 * </p>
 */
public class Ship implements Serializable {

    private static final long serialVersionUID = 1L;

    private final ShipType type;
    private final List<Cell> cells;
    private boolean isHorizontal;
    private boolean isSunk;

    /**
     * Constructor que crea un nuevo barco de un tipo específico.
     *
     * @param type El tipo de barco (ej. Portaaviones, Submarino) que define su tamaño y apariencia.
     */
    public Ship(ShipType type) {
        this.type = type;
        this.cells = new ArrayList<>();
        this.isHorizontal = true;
        this.isSunk = false;
    }

    /**
     * Asocia una celda del tablero a este barco.
     * <p>
     * Establece la relación bidireccional: añade la celda a la lista del barco
     * y asigna este barco a la celda.
     * </p>
     *
     * @param cell La celda que formará parte de este barco.
     */
    public void addCell(Cell cell) {
        if (!cells.contains(cell)) {
            cells.add(cell);
            cell.setShip(this);
        }
    }

    // ✅ El barco recibe impacto SOLO desde Cell
    /**
     * Notifica al barco que una de sus celdas ha sido impactada.
     * <p>
     * Verifica si la celda pertenece al barco y si no ha sido impactada previamente.
     * Si todas las celdas del barco han sido impactadas, marca el barco como hundido.
     * </p>
     *
     * @param hitCell La celda que recibió el disparo.
     */
    public void notifyHit(Cell hitCell) {
        if (!cells.contains(hitCell) || hitCell.isHit()) return;

        hitCell.setHit(true);

        if (cells.stream().allMatch(Cell::isHit)) {
            markAsSunk();
        }
    }

    /**
     * Marca el barco internamente como hundido y actualiza el estado de todas sus celdas.
     */
    private void markAsSunk() {
        this.isSunk = true;
        for (Cell c : cells) {
            c.setSunkPart(true);
        }
    }

    /**
     * Verifica si el barco ha sido completamente hundido.
     *
     * @return {@code true} si todas sus partes han sido impactadas, {@code false} en caso contrario.
     */
    public boolean isSunk() {
        return isSunk;
    }

    /**
     * Obtiene el tipo de barco.
     *
     * @return El enum {@link ShipType} correspondiente.
     */
    public ShipType getType() {
        return type;
    }

    /**
     * Obtiene el tamaño (longitud en celdas) del barco.
     *
     * @return Un entero representando el tamaño.
     */
    public int getSize() {
        return type.getSize();
    }

    /**
     * Obtiene el nombre legible del barco.
     *
     * @return El nombre del barco (ej. "Portaaviones").
     */
    public String getName() {
        return type.getDisplayName();
    }

    /**
     * Obtiene una lista de las celdas que ocupa este barco.
     *
     * @return Una nueva lista conteniendo las celdas asociadas.
     */
    public List<Cell> getCells() {
        return new ArrayList<>(cells);
    }

    /**
     * Indica la orientación actual del barco.
     *
     * @return {@code true} si es horizontal, {@code false} si es vertical.
     */
    public boolean isHorizontal() {
        return isHorizontal;
    }

    /**
     * Establece la orientación del barco.
     *
     * @param horizontal {@code true} para horizontal, {@code false} para vertical.
     */
    public void setHorizontal(boolean horizontal) {
        this.isHorizontal = horizontal;
    }

    /**
     * Intenta colocar el barco en un tablero en una posición específica.
     * <p>
     * Primero verifica si la posición es válida usando {@link #canPlaceAt}.
     * Si es válida, asigna las celdas correspondientes al barco.
     * </p>
     *
     * @param board      El tablero donde se colocará el barco.
     * @param startRow   Fila inicial de colocación.
     * @param startCol   Columna inicial de colocación.
     * @param horizontal Orientación del barco.
     * @return {@code true} si el barco fue colocado exitosamente, {@code false} si no fue posible.
     */
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

    /**
     * Verifica si es posible colocar el barco en las coordenadas indicadas.
     * <p>
     * Comprueba límites del tablero, si las celdas ya están ocupadas y reglas de adyacencia
     * (espacio libre alrededor).
     * </p>
     *
     * @param board      El tablero a verificar.
     * @param startRow   Fila inicial.
     * @param startCol   Columna inicial.
     * @param horizontal Orientación propuesta.
     * @return {@code true} si la posición es válida y libre, {@code false} en caso contrario.
     */
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