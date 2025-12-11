package org.example.batalla_naval_re.controller;

import javafx.event.ActionEvent;

import java.io.IOException;

/**
 * Interfaz que define el contrato para el controlador de la ventana principal (Menú).
 * <p>
 * Gestiona las acciones principales del menú de inicio, como comenzar un nuevo juego
 * o cargar una partida existente.
 * </p>
 */
public interface IMainController {

    /**
     * Maneja el evento de iniciar una nueva partida.
     * <p>
     * Solicita al usuario un nombre (nickname), crea un nuevo estado de juego
     * y transiciona a la vista del tablero de juego.
     * </p>
     *
     * @param event El evento de acción disparado por el botón (New Game).
     * @throws IOException Si ocurre un error al cargar la vista del juego (game.fxml).
     */
    void onNewGame(ActionEvent event) throws IOException;

    /**
     * Maneja el evento de continuar una partida guardada.
     * <p>
     * Intenta cargar el estado del juego desde un archivo de persistencia.
     * Si tiene éxito, restaura la partida y transiciona a la vista del juego.
     * </p>
     *
     * @param event El evento de acción disparado por el botón (Continue).
     * @throws IOException            Si ocurre un error de entrada/salida o al cargar la vista.
     * @throws ClassNotFoundException Si ocurre un error al deserializar el objeto del juego guardado.
     */
    void onContinue(ActionEvent event) throws IOException, ClassNotFoundException;

    /**
     * Maneja el evento para iniciar/continuar una partida revelando el tablero de la máquina.
     * <p>
     * Esta funcionalidad suele utilizarse con fines de depuración o demostración,
     * permitiendo ver la ubicación de los barcos enemigos desde el inicio.
     * </p>
     *
     * @param event El evento de acción disparado por el botón correspondiente.
     * @throws IOException            Si ocurre un error al cargar la vista.
     * @throws ClassNotFoundException Si ocurre un error al deserializar un juego guardado (si aplica).
     */
    void onShowMachineBoard(ActionEvent event) throws IOException, ClassNotFoundException;
}