package org.example.batalla_naval_re.model;

public enum ShipType {
    CARRIER(4, "Portaaviones"),
    SUBMARINE(3, "Submarino"),
    DESTROYER(2, "Destructor"),
    FRIGATE(1, "Fragata");

    private final int size;
    private final String displayName;

    ShipType(int size, String displayName) {
        this.size = size;
        this.displayName = displayName;
    }

    public int getSize() {
        return size;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Método auxiliar para encontrar tipo por tamaño
    public static ShipType fromSize(int size) {
        for (ShipType type : values()) {
            if (type.getSize() == size) {
                return type;
            }
        }
        throw new IllegalArgumentException("No ship type with size " + size);
    }
}