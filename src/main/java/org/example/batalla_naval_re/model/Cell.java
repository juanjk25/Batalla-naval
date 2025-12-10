package org.example.batalla_naval_re.model;

import java.io.Serializable;

public class Cell implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean ship;
    private boolean hit;
    private boolean miss;
    private boolean sunkPart;
    private boolean visibleToPlayer;

    private Ship shipObject;
    private int row;
    private int col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        clear();
    }

    public Cell() {
        this(0, 0);
    }

    // =========================
    // GETTERS
    // =========================
    public boolean isShip() { return ship; }
    public boolean isHit() { return hit; }
    public boolean isMiss() { return miss; }
    public boolean isSunkPart() { return sunkPart; }
    public boolean isVisibleToPlayer() { return visibleToPlayer; }
    public boolean isTried() { return hit || miss; }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public Ship getShip() { return shipObject; }

    // =========================
    // SETTERS IMPORTANTES
    // =========================
    public void setShip(Ship ship) {
        this.shipObject = ship;
        this.ship = ship != null;
    }

    public void setHit(boolean hit) {
        if (this.hit) return;   // ✅ evita repetir
        this.hit = hit;
        this.miss = false;

        if (hit && shipObject != null) {
            shipObject.notifyHit(this); // ✅ notifica UNA vez
        }
    }

    public void setMiss(boolean miss) {
        if (this.miss) return;
        this.miss = miss;
        this.hit = false;
    }

    public void setSunkPart(boolean sunkPart) {
        this.sunkPart = sunkPart;
        if (sunkPart) this.hit = true;
    }

    public void setVisibleToPlayer(boolean visibleToPlayer) {
        this.visibleToPlayer = visibleToPlayer;
    }

    // =========================
    // DISPARO
    // =========================
    public ShotResult shoot() {
        if (isTried()) {
            return ShotResult.ALREADY_TRIED;
        }

        if (ship) {
            setHit(true);
            if (shipObject != null && shipObject.isSunk()) {
                return ShotResult.SUNK;
            }
            return ShotResult.HIT;
        } else {
            setMiss(true);
            return ShotResult.MISS;
        }
    }

    // =========================
    // RESET
    // =========================
    public void clear() {
        ship = false;
        hit = false;
        miss = false;
        sunkPart = false;
        visibleToPlayer = false;
        shipObject = null;
    }

    public enum ShotResult {
        HIT, MISS, SUNK, ALREADY_TRIED
    }

    @Override
    public String toString() {
        return "Cell[" + row + "," + col + "] → " +
                (ship ? "SHIP" : "WATER") +
                (hit ? " HIT" : miss ? " MISS" : "");
    }
}
