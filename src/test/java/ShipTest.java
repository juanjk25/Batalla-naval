package org.example.batalla_naval_re.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void shipShouldBePlacedCorrectly() {
        Board board = new Board();
        Ship ship = new Ship(ShipType.DESTROYER);

        boolean placed = ship.placeAt(board, 2, 2, true);

        assertTrue(placed);
        assertEquals(2, ship.getCells().size());
    }

    @Test
    void shipShouldSinkWhenAllCellsAreHit() {
        Board board = new Board();
        Ship ship = new Ship(ShipType.FRIGATE);

        ship.placeAt(board, 0, 0, true);

        Cell cell = ship.getCells().get(0);
        ship.notifyHit(cell);

        assertTrue(ship.isSunk());
        assertTrue(cell.isSunkPart());
    }

    @Test
    void shipCannotBePlacedOnAnotherShip() {
        Board board = new Board();
        Ship ship1 = new Ship(ShipType.SUBMARINE);
        Ship ship2 = new Ship(ShipType.SUBMARINE);

        ship1.placeAt(board, 3, 3, true);
        boolean placed = ship2.placeAt(board, 3, 3, true);

        assertFalse(placed);
    }
}
