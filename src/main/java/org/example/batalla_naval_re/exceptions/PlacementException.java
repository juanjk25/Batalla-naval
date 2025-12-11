package org.example.batalla_naval_re.exceptions;

/**
 * Excepción personalizada que indica un error al intentar colocar un barco en el tablero.
 * <p>
 * Se lanza cuando una operación de colocación viola las reglas del juego, como intentar
 * colocar un barco fuera de los límites, sobreponerse a otro barco, o no respetar
 * el espacio mínimo entre barcos.
 * </p>
 */
public class PlacementException extends Exception {
    /**
     * Crea una nueva instancia de {@code PlacementException} con un mensaje detallado.
     *
     * @param message El mensaje que describe la causa específica del error de colocación.
     */
    public PlacementException(String message) { super(message); }
}