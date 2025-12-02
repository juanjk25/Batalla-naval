package org.example.batallanaval.model;

import java.io.Serializable;

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean ship;
    private boolean hit;
    private boolean miss;
    private boolean sunkPart;
    private boolean visibleToPlayer; // for revealing machine pieces (debugging/professor)

    public Cell() {}

    public boolean isShip() { return ship; }
    public void setShip(boolean ship) { this.ship = ship; }
    public boolean isHit() { return hit; }
    public void setHit(boolean hit) { this.hit = hit; }
    public boolean isMiss() { return miss; }
    public void setMiss(boolean miss) { this.miss = miss; }
    public boolean isSunkPart() { return sunkPart; }
    public void setSunkPart(boolean sunkPart) { this.sunkPart = sunkPart; }
    public boolean isTried() { return hit || miss; }
    public boolean isVisibleToPlayer() { return visibleToPlayer; }
    public void setVisibleToPlayer(boolean visibleToPlayer) { this.visibleToPlayer = visibleToPlayer; }
}
