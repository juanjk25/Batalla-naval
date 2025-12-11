
package org.example.batalla_naval_re.model;

/**
 * Enumeración que define los tipos de barcos disponibles en el juego.
 * <p>
 * Cada tipo de barco tiene propiedades fijas:
 * <ul>
 *     <li>Tamaño (número de celdas que ocupa).</li>
 *     <li>Nombre para mostrar en la interfaz.</li>
 *     <li>Ruta de la imagen asociada (para la vista gráfica).</li>
 * </ul>
 */
public enum ShipType {
    /** Portaaviones: El barco más grande, ocupa 4 celdas. */
    CARRIER(4, "Portaaviones", "/portaaviones.jpg"),

    /** Submarino: Barco mediano, ocupa 3 celdas. */
    SUBMARINE(3, "Submarino", "/submarino2.jpg"),

    /** Destructor: Barco pequeño, ocupa 2 celdas. */
    DESTROYER(2, "Destructor", "/destructor1.jpg"),

    /** Fragata: El barco más pequeño, ocupa 1 celda. */
    FRIGATE(1, "Fragata", "/fragata1.jpg");

    private final int size;
    private final String displayName;
    private final String imagePath;

    /**
     * Constructor para los tipos de barco.
     *
     * @param size        Tamaño del barco en celdas.
     * @param displayName Nombre legible del barco.
     * @param imagePath   Ruta del recurso de imagen.
     */
    ShipType(int size, String displayName, String imagePath) {
        this.size = size;
        this.displayName = displayName;
        this.imagePath = imagePath;
    }

    /**
     * Obtiene el tamaño del barco.
     *
     * @return Número de celdas que ocupa.
     */
    public int getSize() {
        return size;
    }

    /**
     * Obtiene el nombre para mostrar.
     *
     * @return El nombre del barco (ej. "Portaaviones").
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Obtiene la ruta de la imagen asociada.
     *
     * @return String con la ruta relativa del recurso.
     */
    public String getImagePath() {
        return imagePath;
    }

    // Método auxiliar por tamaño (opcional)
    /**
     * Busca un tipo de barco basándose en su tamaño.
     *
     * @param size El tamaño buscado.
     * @return El {@code ShipType} correspondiente.
     * @throws IllegalArgumentException Si no existe ningún barco con ese tamaño exacto.
     */
    public static ShipType fromSize(int size) {
        for (ShipType type : values()) {
            if (type.getSize() == size) {
                return type;
            }
        }
        throw new IllegalArgumentException("No ship type with size " + size);
    }
}