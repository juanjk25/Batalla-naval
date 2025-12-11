package org.example.batalla_naval_re.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Representa el tablero de juego de Batalla Naval.
 * <p>
 * Mantiene el estado de la cuadrícula de celdas (10x10), la lista de barcos colocados
 * y gestiona la lógica principal del juego: disparos, colocación de barcos (manual y aleatoria)
 * y verificación de reglas de posición.
 * </p>
 */
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Tamaño del tablero (número de filas y columnas). Por defecto es 10.
     */
    public static final int SIZE = 10;

    private final Cell[][] grid;
    private final List<Ship> ships;
    private final Random random;

    /**
     * Constructor por defecto.
     * Inicializa la cuadrícula de celdas vacías y las listas internas.
     */
    public Board() {
        grid = new Cell[SIZE][SIZE];
        ships = new ArrayList<>();
        random = new Random();
        initializeGrid();
    }

    /**
     * Llena la cuadrícula con nuevas instancias de {@link Cell}.
     */
    private void initializeGrid() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = new Cell(r, c);
            }
        }
    }

    /**
     * Obtiene la celda en la posición especificada.
     *
     * @param row indice de la fila (0 a SIZE-1).
     * @param col indice de la columna (0 a SIZE-1).
     * @return El objeto {@link Cell} en esa posición.
     * @throws IndexOutOfBoundsException Si las coordenadas estan fuera del tablero.
     */
    public Cell getCell(int row, int col) {
        if (isValidPosition(row, col)) return grid[row][col];
        throw new IndexOutOfBoundsException("Posición fuera del tablero");
    }

    /**
     * Verifica si una coordenada está dentro de los limites del tablero.
     *
     * @param row Fila a verificar.
     * @param col Columna a verificar.
     * @return {@code true} si la posición es válida, {@code false} en caso contrario.
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    // -----------------------------------
    //      DISPAROS
    // -----------------------------------

    /**
     * Realiza un disparo en la coordenada indicada.
     * <p>
     * Delega la lógica a la celda correspondiente y verifica si el disparo
     * resultó en el hundimiento de un barco completo.
     * </p>
     *
     * @param row Fila objetivo.
     * @param col Columna objetivo.
     * @return El resultado del disparo: {@code MISS} (Agua), {@code HIT} (Tocado), o {@code SUNK} (Hundido).
     */
    public Cell.ShotResult shoot(int row, int col) {
        Cell cell = getCell(row, col);
        Cell.ShotResult result = cell.shoot();

        if (result == Cell.ShotResult.HIT) {
            Ship ship = cell.getShip();
            if (ship != null && ship.isSunk()) result = Cell.ShotResult.SUNK;
        }
        return result;
    }

    // -----------------------------------
    //      COLOCACIÓN MANUAL
    // -----------------------------------

    /**
     * Intenta colocar un barco en el tablero.
     * <p>
     * Verifica primero si la colocación es válida. Si lo es, vincula el barco
     * al tablero y lo añade a la lista de barcos activos.
     * </p>
     *
     * @param ship       El barco a colocar.
     * @param row        Fila inicial (coordenada superior/izquierda).
     * @param col        Columna inicial (coordenada superior/izquierda).
     * @param horizontal {@code true} para orientación horizontal, {@code false} para vertical.
     * @return {@code true} si el barco se colocó correctamente, {@code false} si no fue posible.
     */
    public boolean addShip(Ship ship, int row, int col, boolean horizontal) {
        if (!canPlaceShip(ship, row, col, horizontal)) return false;

        ship.placeAt(this, row, col, horizontal);

        if (!ships.contains(ship)) ships.add(ship);

        return true;
    }

    // 🛑 Sin tocar bordes ni diagonales
    /**
     * Verifica si una celda específica está libre para colocar parte de un barco,
     * respetando la regla de separación.
     * <p>
     * La regla establece que los barcos no pueden tocarse entre sí, ni siquiera en diagonal.
     * Este método revisa la celda objetivo y sus 8 celdas adyacentes.
     * </p>
     *
     * @param row Fila de la celda.
     * @param col Columna de la celda.
     * @return {@code true} si la celda y sus alrededores no contienen otros barcos.
     */
    public boolean isCellFreeForShipPlacement(int row, int col) {
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (isValidPosition(r, c) && grid[r][c].isShip()) return false;
            }
        }
        return true;
    }

    /**
     * Evalúa si un barco completo puede ser colocado en una posición y orientación dadas.
     * <p>
     * Verifica límites del tablero, colisiones directas y reglas de proximidad
     * para cada segmento del barco.
     * </p>
     *
     * @param ship       El barco a evaluar.
     * @param startRow   Fila de inicio.
     * @param startCol   Columna de inicio.
     * @param horizontal Orientación propuesta.
     * @return {@code true} si la colocación es válida.
     */
    public boolean canPlaceShip(Ship ship, int startRow, int startCol, boolean horizontal) {
        int size = ship.getSize();

        for (int i = 0; i < size; i++) {
            int row = horizontal ? startRow : startRow + i;
            int col = horizontal ? startCol + i : startCol;

            if (!isValidPosition(row, col) || grid[row][col].isShip() || !isCellFreeForShipPlacement(row, col)) {
                return false;
            }
        }
        return true;
    }

    // -----------------------------------
    //      PROGRESO
    // -----------------------------------

    /**
     * Verifica si todos los barcos requeridos (10 en total) han sido colocados correctamente.
     *
     * @return {@code true} si la flota está completa y posicionada.
     */
    public boolean allShipsPlaced() {
        return ships.size() == 10 && ships.stream().allMatch(s -> s.getCells().size() == s.getSize());
    }

    /**
     * Verifica si todos los barcos del tablero han sido hundidos.
     * Determina la condición de derrota para el dueño de este tablero.
     *
     * @return {@code true} si no quedan barcos a flote.
     */
    public boolean allShipsSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    /**
     * Obtiene una copia de la lista de barcos en el tablero.
     *
     * @return Lista de objetos {@link Ship}.
     */
    public List<Ship> getShips() {
        return new ArrayList<>(ships);
    }

    /**
     * Limpia el tablero por completo, eliminando barcos y reiniciando celdas.
     */
    public void clear() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c].clear();
            }
        }
        ships.clear();
    }

    // -----------------------------------
    //      PARA LA MAQUINA
    // -----------------------------------

    /**
     * Genera la lista de barcos estándar para el juego sin colocarlos en el tablero.
     * Util para inicializar la flota del jugador antes de la fase de colocacion manual.
     */
    public void createShipsWithoutPlacement() {
        ships.clear();
        ships.add(new Ship(ShipType.CARRIER));
        ships.add(new Ship(ShipType.SUBMARINE));
        ships.add(new Ship(ShipType.SUBMARINE));
        ships.add(new Ship(ShipType.DESTROYER));
        ships.add(new Ship(ShipType.DESTROYER));
        ships.add(new Ship(ShipType.DESTROYER));
        ships.add(new Ship(ShipType.FRIGATE));
        ships.add(new Ship(ShipType.FRIGATE));
        ships.add(new Ship(ShipType.FRIGATE));
        ships.add(new Ship(ShipType.FRIGATE));
    }

    /**
     * Coloca aleatoriamente toda la flota en el tablero.
     * <p>
     * Usado por la IA para preparar su tablero. Si la colocación falla (por bloqueo),
     * reinicia el proceso hasta lograr una configuración válida.
     * </p>
     *
     * @return {@code true} si la colocación fue exitosa.
     */
    public boolean randomPlaceAllShips() {
        clear();
        List<Ship> shipsToPlace = createShipsForAI();
        Collections.shuffle(shipsToPlace, random);

        for (Ship ship : shipsToPlace) {
            if (!placeShipRandomly(ship)) {
                clear();
                return false;
            }
            ships.add(ship);
        }
        return true;
    }

    /**
     * Crea una nueva lista de barcos para uso interno de la IA.
     * @return Lista de barcos no colocados.
     */
    private List<Ship> createShipsForAI() {
        List<Ship> list = new ArrayList<>();
        list.add(new Ship(ShipType.CARRIER));
        list.add(new Ship(ShipType.SUBMARINE));
        list.add(new Ship(ShipType.SUBMARINE));
        list.add(new Ship(ShipType.DESTROYER));
        list.add(new Ship(ShipType.DESTROYER));
        list.add(new Ship(ShipType.DESTROYER));
        list.add(new Ship(ShipType.FRIGATE));
        list.add(new Ship(ShipType.FRIGATE));
        list.add(new Ship(ShipType.FRIGATE));
        list.add(new Ship(ShipType.FRIGATE));
        return list;
    }

    /**
     * Intenta colocar un barco específico en una posición aleatoria válida.
     * Realiza hasta 100 intentos antes de desistir.
     *
     * @param ship El barco a colocar.
     * @return {@code true} si se encontró una posición válida.
     */
    private boolean placeShipRandomly(Ship ship) {
        int maxAttempts = 100;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {

            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            boolean horizontal = random.nextBoolean();

            if (canPlaceShip(ship, row, col, horizontal)) {
                ship.placeAt(this, row, col, horizontal);
                return true;
            }
        }
        return false;
    }
}