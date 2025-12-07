package org.example.batalla_naval_re.controller;

import javafx.event.ActionEvent;

import java.io.IOException;

public interface IMainController {
    void onNewGame(ActionEvent event) throws IOException;
    void onContinue(ActionEvent event) throws IOException, ClassNotFoundException;
    void onShowMachineBoard(ActionEvent event) throws IOException, ClassNotFoundException;
}
