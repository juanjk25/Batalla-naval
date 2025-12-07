package org.example.batalla_naval_re.model;

import java.io.Serializable;

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean ship;
    private boolean hit;
    private boolean miss;
    private boolean sunkPart;
    private boolean visibleToPlayer;
    private Ship shipObject;  // ← NUEVO: Referencia al objeto Ship
    private int row;          // ← NUEVO: Fila de la celda
    private int col;          // ← NUEVO: Columna de la celda

    // Constructor modificado para recibir coordenadas
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.ship = false;
        this.hit = false;
        this.miss = false;
        this.sunkPart = false;
        this.visibleToPlayer = false;
        this.shipObject = null;
    }

    // Constructor vacío para serialización (mantener compatibilidad)
    public Cell() {
        this(0, 0); // Valores por defecto
    }

    // Getters y Setters básicos
    public boolean isShip() { return ship; }
    public boolean isHit() { return hit; }
    public boolean isMiss() { return miss; }
    public boolean isSunkPart() { return sunkPart; }
    public boolean isVisibleToPlayer() { return visibleToPlayer; }
    public boolean isTried() { return hit || miss; }

    public int getRow() { return row; }
    public int getCol() { return col; }

    // Getter y Setter para shipObject
    public Ship getShip() { return shipObject; }
    public void setShip(Ship ship) {
        this.shipObject = ship;
        this.ship = (ship != null);
    }

    // Setters con validación
    public void setHit(boolean hit) {
        this.hit = hit;
        if (hit) {
            this.miss = false; // No puede ser hit y miss a la vez
            // Notificar al barco que fue golpeado
            if (shipObject != null) {
                shipObject.notifyHit(this);
            }
        }
    }

    public void setMiss(boolean miss) {
        this.miss = miss;
        if (miss) {
            this.hit = false; // No puede ser miss y hit a la vez
        }
    }

    public void setSunkPart(boolean sunkPart) {
        this.sunkPart = sunkPart;
        if (sunkPart) {
            this.hit = true; // Si está hundido, también está golpeado
        }
    }

    public void setVisibleToPlayer(boolean visibleToPlayer) {
        this.visibleToPlayer = visibleToPlayer;
    }

    // Método para disparar en esta celda
    public ShotResult shoot() {
        if (isTried()) {
            return ShotResult.ALREADY_TRIED;
        }

        if (isShip()) {
            setHit(true);
            // Verificar si el barco fue hundido
            if (shipObject != null && shipObject.isSunk()) {
                setSunkPart(true);
                return ShotResult.SUNK;
            }
            return ShotResult.HIT;
        } else {
            setMiss(true);
            return ShotResult.MISS;
        }
    }

    // Método para limpiar la celda (reiniciar juego)
    public void clear() {
        this.ship = false;
        this.hit = false;
        this.miss = false;
        this.sunkPart = false;
        this.visibleToPlayer = false;
        this.shipObject = null;
    }

    // Método para obtener el estado de la celda como texto (debug)
    public String getStatus() {
        if (sunkPart) return "SUNK";
        if (hit) return "HIT";
        if (miss) return "MISS";
        if (ship) return "SHIP";
        return "WATER";
    }

    // Enum interno para resultados de disparo
    public enum ShotResult {
        HIT, MISS, SUNK, ALREADY_TRIED
    }

    // Sobreescribir toString para debug
    @Override
    public String toString() {
        return String.format("Cell[%d,%d] - Status: %s, Ship: %s",
                row, col, getStatus(),
                (shipObject != null ? shipObject.getType() : "None"));
    }

    // Método para serialización personalizada (opcional)
    private void writeObject(java.io.ObjectOutputStream out)
            throws java.io.IOException {
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Asegurar consistencia después de deserialización
        if (shipObject == null) {
            ship = false;
        }
    }
}