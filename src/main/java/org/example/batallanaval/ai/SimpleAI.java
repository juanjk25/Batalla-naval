package org.example.batallanaval.ai;

import org.example.batallanaval.model.Board;
import org.example.batallanaval.model.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleAI {
    private final Board opponentBoard; // AI shoots against this board
    private final List<int[]> possibleShots = new ArrayList<>();
    private final Random rnd = new Random();

    public SimpleAI(Board opponentBoard) {
        this.opponentBoard = opponentBoard;
        for (int r=0;r<Board.SIZE;r++) for (int c=0;c<Board.SIZE;c++) possibleShots.add(new int[]{r,c});
    }

    public int[] nextShot() {
        if (possibleShots.isEmpty()) return new int[]{0,0};
        int idx = rnd.nextInt(possibleShots.size());
        int[] shot = possibleShots.remove(idx);
        // ensure not tried
        Cell cell = opponentBoard.getCell(shot[0], shot[1]);
        if (cell.isTried()) return nextShot();
        return shot;
    }

    public void reportResult(int[] shot, boolean hit) {
        // optional: could bias further shots
    }
}
