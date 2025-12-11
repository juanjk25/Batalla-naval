package org.example.batalla_naval_re.controller;

import org.example.batalla_naval_re.model.GameState;

import java.io.IOException;

/**
 * Interfaz que define el contrato para los controladores de la vista de juego.
 * <p>
 * Proporciona los métodos esenciales para inicializar el estado de una partida
 * y manejar la navegación hacia atrás (salir de la partida).
 * </p>
 */
public interface IGameController {

    /**
     * Inicializa el controlador con el estado de juego proporcionado.
     *
     * @param state         El objeto {@link GameState} que contiene la información de la partida actual.
     * @param revealMachine {@code true} si se deben revelar los barcos de la máquina (modo debug o fin de partida),
     *                      {@code false} en caso contrario.
     */
    void initState(GameState state, boolean revealMachine);

    /**
     * Maneja la acción de regresar a la pantalla anterior o menú principal.
     *
     * @throws IOException Si ocurre un error al cargar la vista de destino (por ejemplo, el archivo FXML).
     */
    void onBack() throws IOException;
}