package org.example.batalla_naval_re.persistence;

/**
 * Excepción personalizada para errores relacionados con
 * guardar o cargar el estado del juego.
 *
 * Se lanza ante problemas de entrada/salida, archivos corruptos,
 * ausencia de archivos válidos o fallos de serialización.
 */
public final class GameFileException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Crea una excepción con un mensaje específico.
     *
     * @param message descripción del error
     */
    public GameFileException(String message) {
        super(message);
    }

    /**
     * Crea una excepción con un mensaje y una causa.
     *
     * @param message descripción del error
     * @param cause excepción original que produjo el fallo
     */
    public GameFileException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crea una excepción solo con la causa.
     *
     * @param cause excepción original
     */
    public GameFileException(Throwable cause) {
        super(cause);
    }
}
