package org.example.batallanaval.model;

public enum ShipType {
    CARRIER(4, "Portaaviones"),
    SUBMARINE(3, "Submarino"),
    DESTROYER(2, "Destructor"),
    FRIGATE(1, "Fragata");

    private final int size;
    private final String display;

    ShipType(int size, String display) { this.size = size; this.display = display; }
    public int getSize() { return size; }
    public String getDisplay() { return display; }
}
