package org.example.batalla_naval_re.model;

import java.io.Serializable;

/**
 * Representa una unidad individual (casilla) dentro del tablero de juego.
 * <p>
 * Mantiene el estado de una coordenada específica: si contiene un barco, si ha sido disparada,
 * si el disparo fue un acierto o fallo, y si es parte de un barco hundido.
 * </p>
 */
public class Cell implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Indica si esta celda contiene un barco.
     */
    private boolean ship;

    /**
     * Indica si esta celda ha recibido un disparo que impactó en un barco.
     */
    private boolean hit;

    /**
     * Indica si esta celda ha recibido un disparo que cayó en el agua (fallo).
     */
    private boolean miss;

    /**
     * Indica si esta celda forma parte de un barco que ya ha sido completamente hundido.
     */
    private boolean sunkPart;

    /**
     * Determina si el contenido de la celda es visible para el jugador (usado para debug o fin de juego).
     */
    private boolean visibleToPlayer;

    /**
     * Referencia al objeto {@link Ship} que ocupa esta celda, si existe.
     */
    private Ship shipObject;

    private int row;
    private int col;

    /**
     * Constructor que inicializa una celda en una posición específica.
     *
     * @param row La fila de la celda.
     * @param col La columna de la celda.
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        clear();
    }

    /**
     * Constructor por defecto que inicializa la celda en (0,0).
     */
    public Cell() {
        this(0, 0);
    }

    // =========================
    // GETTERS
    // =========================

    /**
     * Verifica si la celda contiene un barco.
     * @return {@code true} si hay un barco, {@code false} si es agua.
     */
    public boolean isShip() { return ship; }

    /**
     * Verifica si la celda ha sido impactada.
     * @return {@code true} si se disparó y había un barco.
     */
    public boolean isHit() { return hit; }

    /**
     * Verifica si el disparo en esta celda fue al agua.
     * @return {@code true} si se disparó y no había barco.
     */
    public boolean isMiss() { return miss; }

    /**
     * Verifica si esta celda pertenece a un barco hundido.
     * @return {@code true} si el barco asociado está completamente hundido.
     */
    public boolean isSunkPart() { return sunkPart; }

    /**
     * Verifica si la celda debe ser visible para el jugador.
     * @return {@code true} si es visible.
     */
    public boolean isVisibleToPlayer() { return visibleToPlayer; }

    /**
     * Verifica si la celda ya ha sido disparada (sea acierto o fallo).
     * @return {@code true} si ya se realizó un intento en esta celda.
     */
    public boolean isTried() { return hit || miss; }

    public int getRow() { return row; }
    public int getCol() { return col; }

    /**
     * Obtiene el objeto Barco asociado a esta celda.
     * @return El objeto {@link Ship} o {@code null} si no hay barco.
     */
    public Ship getShip() { return shipObject; }

    // =========================
    // SETTERS IMPORTANTES
    // =========================

    /**
     * Asigna un barco a esta celda.
     * @param ship El barco a colocar en esta posición.
     */
    public void setShip(Ship ship) {
        this.shipObject = ship;
        this.ship = ship != null;
    }

    /**
     * Marca la celda como impactada (Hit).
     * <p>
     * Si la celda contiene un barco, notifica al barco del impacto para que actualice su estado.
     * </p>
     *
     * @param hit {@code true} para marcar impacto.
     */
    public void setHit(boolean hit) {
        if (this.hit) return;   // ✅ evita repetir
        this.hit = hit;
        this.miss = false;

        if (hit && shipObject != null) {
            shipObject.notifyHit(this); // ✅ notifica UNA vez
        }
    }

    /**
     * Marca la celda como fallo (Miss/Agua).
     * @param miss {@code true} para marcar fallo.
     */
    public void setMiss(boolean miss) {
        if (this.miss) return;
        this.miss = miss;
        this.hit = false;
    }

    /**
     * Marca esta celda como parte de un barco hundido.
     * Esto suele cambiar la representación visual de la celda.
     *
     * @param sunkPart {@code true} si el barco ha sido hundido.
     */
    public void setSunkPart(boolean sunkPart) {
        this.sunkPart = sunkPart;
        if (sunkPart) this.hit = true;
    }

    public void setVisibleToPlayer(boolean visibleToPlayer) {
        this.visibleToPlayer = visibleToPlayer;
    }

    // =========================
    // DISPARO
    // =========================

    /**
     * Procesa un disparo realizado sobre esta celda.
     * <p>
     * Determina el resultado del disparo basándose en el estado actual:
     * <ul>
     *     <li>{@code ALREADY_TRIED}: Si ya se había disparado aquí.</li>
     *     <li>{@code SUNK}: Si el disparo hunde el barco.</li>
     *     <li>{@code HIT}: Si el disparo impacta un barco pero no lo hunde.</li>
     *     <li>{@code MISS}: Si el disparo cae en el agua.</li>
     * </ul>
     *
     *
     * @return El valor {@link ShotResult} correspondiente al resultado de la acción.
     */
    public ShotResult shoot() {
        if (isTried()) {
            return ShotResult.ALREADY_TRIED;
        }

        if (ship) {
            setHit(true);
            if (shipObject != null && shipObject.isSunk()) {
                return ShotResult.SUNK;
            }
            return ShotResult.HIT;
        } else {
            setMiss(true);
            return ShotResult.MISS;
        }
    }

    // =========================
    // RESET
    // =========================

    /**
     * Restablece la celda a su estado inicial (vacía y sin disparos).
     */
    public void clear() {
        ship = false;
        hit = false;
        miss = false;
        sunkPart = false;
        visibleToPlayer = false;
        shipObject = null;
    }

    /**
     * Enumeración que define los posibles resultados de un disparo.
     */
    public enum ShotResult {
        /** Impacto en un barco. */
        HIT,
        /** Disparo al agua. */
        MISS,
        /** El disparo causó el hundimiento del barco. */
        SUNK,
        /** La celda ya había sido disparada previamente. */
        ALREADY_TRIED
    }

    @Override
    public String toString() {
        return "Cell[" + row + "," + col + "] → " +
                (ship ? "SHIP" : "WATER") +
                (hit ? " HIT" : miss ? " MISS" : "");
    }
}