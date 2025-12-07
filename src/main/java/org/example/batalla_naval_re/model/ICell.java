package org.example.batalla_naval_re.model;

import java.io.Serializable;

public interface ICell extends Serializable {
    boolean isShip();
    void setShip(boolean ship);
    boolean isHit();
    void setHit(boolean hit);
    boolean isMiss();
    void setMiss(boolean miss);
    boolean isSunkPart();
    void setSunkPart(boolean sunkPart);
    boolean isTried();
    boolean isVisibleToPlayer();
    void setVisibleToPlayer(boolean visibleToPlayer);
}
