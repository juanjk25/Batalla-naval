package org.example.batalla_naval_re.view.renderer;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.example.batalla_naval_re.model.*;
import org.example.batalla_naval_re.view.shapes.IShipShape;
import org.example.batalla_naval_re.view.shapes.ShipShapeFactory;

/**
 * Clase utilitaria encargada de renderizar gráficamente las celdas del tablero.
 * <p>
 * Se ocupa de crear la representación visual de cada casilla, incluyendo el fondo,
 * los barcos (si son visibles), y los marcadores de disparos (impactos o agua).
 * </p>
 */
public class BoardRenderer {

    /**
     * Crea un panel gráfico que representa una celda del tablero en el estado actual.
     *
     * @param board          El tablero lógico al que pertenece la celda.
     * @param row            La fila de la celda.
     * @param col            La columna de la celda.
     * @param isPlayerBoard  {@code true} si es el tablero del jugador (muestra barcos propios),
     *                       {@code false} si es el del oponente (oculta barcos no descubiertos).
     * @param revealOpponent {@code true} para forzar la visualización de barcos enemigos (modo debug/fin de juego).
     * @return Un {@link StackPane} configurado con los elementos visuales de la celda.
     */
    public static StackPane renderCell(Board board, int row, int col,
                                       boolean isPlayerBoard, boolean revealOpponent) {
        StackPane pane = new StackPane();
        pane.setPrefSize(40, 40);

        // Fondo
        Rectangle background = new Rectangle(40, 40);
        background.setFill(Color.LIGHTBLUE);
        background.setStroke(Color.DARKBLUE);
        background.setStrokeWidth(1);
        pane.getChildren().add(background);

        Cell cell = board.getCell(row, col);

        if (cell.isShip() && shouldShowShip(cell, isPlayerBoard, revealOpponent)) {
            renderShip(pane, cell);
        }

        if (cell.isMiss()) {
            renderMiss(pane);
        }

        if (cell.isHit() && !cell.isShip()) {
            renderHitWater(pane);
        }

        return pane;
    }

    /**
     * Determina si se debe mostrar el barco contenido en una celda.
     * <p>
     * Reglas de visibilidad:
     * 1. Siempre visible si es el tablero del propio jugador.
     * 2. Visible si está activo el modo de revelación (revealOpponent).
     * 3. Visible si la celda ha sido impactada o es parte de un barco hundido.
     * </p>
     *
     * @param cell           La celda a evaluar.
     * @param isPlayerBoard  Si pertenece al jugador humano.
     * @param revealOpponent Si se deben revelar los secretos.
     * @return {@code true} si el barco debe dibujarse.
     */
    private static boolean shouldShowShip(Cell cell, boolean isPlayerBoard, boolean revealOpponent) {
        // Mostrar barco si:
        // 1. Es el tablero del jugador
        // 2. Está revelando el oponente
        // 3. Está golpeado o hundido
        return isPlayerBoard || revealOpponent || cell.isHit() || cell.isSunkPart();
    }

    /**
     * Dibuja el barco en el panel, delegando la creación de la forma a la fábrica correspondiente.
     * También aplica efectos visuales adicionales si el barco está dañado o hundido.
     *
     * @param pane El panel contenedor.
     * @param cell La celda lógica que contiene el barco.
     */
    private static void renderShip(StackPane pane, Cell cell) {
        Ship ship = cell.getShip();
        IShipShape shape = ShipShapeFactory.createShape(ship.getType());

        Node shipNode = shape.createShape(ship, cell);
        pane.getChildren().add(shipNode);

        // Añadir efectos según estado
        if (cell.isSunkPart()) {
            addSunkEffects(pane);
        } else if (cell.isHit()) {
            addHitEffects(pane);
        }
    }

    /**
     * Dibuja una 'X' azul para indicar un disparo fallido (Agua).
     *
     * @param pane El panel donde dibujar.
     */
    private static void renderMiss(StackPane pane) {
        Line line1 = new Line(10, 10, 30, 30);
        line1.setStroke(Color.BLUE);
        line1.setStrokeWidth(2);

        Line line2 = new Line(30, 10, 10, 30);
        line2.setStroke(Color.BLUE);
        line2.setStrokeWidth(2);

        pane.getChildren().addAll(line1, line2);
    }

    /**
     * Dibuja un marcador rojo pequeño para indicar impacto en el agua (no utilizado típicamente
     * si {@code renderMiss} se usa para agua, pero útil para diferenciar estados visuales).
     *
     * @param pane El panel donde dibujar.
     */
    private static void renderHitWater(StackPane pane) {
        Circle hitMarker = new Circle(5);
        hitMarker.setFill(Color.RED);
        pane.getChildren().add(hitMarker);
    }

    /**
     * Añade un efecto visual de explosión (círculo naranja) sobre la celda impactada.
     *
     * @param pane El panel donde añadir el efecto.
     */
    private static void addHitEffects(StackPane pane) {
        Circle explosion = new Circle(8);
        explosion.setFill(Color.TRANSPARENT);
        explosion.setStroke(Color.ORANGE);
        explosion.setStrokeWidth(2);
        pane.getChildren().add(explosion);
    }

    /**
     * Añade un efecto visual de hundimiento (círculo rojo oscuro) sobre la celda.
     *
     * @param pane El panel donde añadir el efecto.
     */
    private static void addSunkEffects(StackPane pane) {
        Circle ripple = new Circle(12);
        ripple.setFill(Color.TRANSPARENT);
        ripple.setStroke(Color.DARKRED);
        ripple.setStrokeWidth(2);
        pane.getChildren().add(ripple);
    }
}