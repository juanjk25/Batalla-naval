package org.example.batalla_naval_re.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    // ------------------------------------
    // PRUEBA 1: Colocación válida de barco
    // ------------------------------------
    @Test
    void shouldPlaceShipSuccessfully() {
        Ship ship = new Ship(ShipType.DESTROYER);

        boolean placed = board.addShip(ship, 0, 0, true);

        assertTrue(placed, "El barco debería colocarse correctamente");
        assertTrue(board.getCell(0, 0).isShip());
    }

    // ------------------------------------
    // PRUEBA 2: Colocación inválida (fuera)
    // ------------------------------------
    @Test
    void shouldNotPlaceShipOutsideBoard() {
        Ship ship = new Ship(ShipType.CARRIER);

        boolean placed = board.addShip(ship, 9, 9, true);

        assertFalse(placed, "No debe permitir colocar barcos fuera del tablero");
    }


    // ------------------------------------
    // PRUEBA 3: Disparo MISS
    // ------------------------------------
    @Test
    void shouldReturnMissWhenShootingWater() {
        Cell.ShotResult result = board.shoot(5, 5);

        assertEquals(Cell.ShotResult.MISS, result);
    }

}