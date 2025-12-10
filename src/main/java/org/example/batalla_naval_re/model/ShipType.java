package org.example.batalla_naval_re.model;

public enum ShipType {
    CARRIER(4, "Portaaviones", "/portaaviones.jpg"),
    SUBMARINE(3, "Submarino", "/submarino2.jpg"),
    DESTROYER(2, "Destructor", "/destructor1.jpg"),
    FRIGATE(1, "Fragata", "/fragata1.jpg");

    private final int size;
    private final String displayName;
    private final String imagePath;

    ShipType(int size, String displayName, String imagePath) {
        this.size = size;
        this.displayName = displayName;
        this.imagePath = imagePath;
    }

    public int getSize() {
        return size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImagePath() {
        return imagePath;
    }

    // Método auxiliar por tamaño (opcional)
    public static ShipType fromSize(int size) {
        for (ShipType type : values()) {
            if (type.getSize() == size) {
                return type;
            }
        }
        throw new IllegalArgumentException("No ship type with size " + size);
    }
}
