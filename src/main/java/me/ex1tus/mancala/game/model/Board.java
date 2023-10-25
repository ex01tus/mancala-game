package me.ex1tus.mancala.game.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;

@Data
public class Board {

    @JsonIgnore private final int pitsInRow;
    @JsonIgnore private final int mancalaPosition;
    private final int[][] pits;

    @JsonCreator
    public Board(@JsonProperty("pits") int[][] pits) {
        this.pits = pits;
        this.pitsInRow = pits[0].length - 1;
        this.mancalaPosition = pitsInRow;
    }

    private Board(int pitsInRow, int stonesInPit) {
        this.pitsInRow = pitsInRow;
        this.mancalaPosition = pitsInRow;
        this.pits = initPits(stonesInPit);
    }

    public int takeAllStonesFrom(Pit pit) {
        int stones = pits[pit.row()][pit.col()];
        pits[pit.row()][pit.col()] = 0;
        return stones;
    }

    public void placeOneStoneTo(Pit pit) {
        pits[pit.row()][pit.col()] += 1;
    }

    public Pit getPitOppositeTo(Pit pit) {
        return new Pit((pit.row() + 1) % 2, pitsInRow - pit.col() - 1);
    }

    public Pit getNextAvailablePitAfter(Pit pit, int player) {
        if ((pit.col() == pitsInRow - 1 && player != pit.row())
                || pit.col() == mancalaPosition) {
            return new Pit((pit.row() + 1) % 2, 0);
        }

        return new Pit(pit.row(), pit.col() + 1);
    }

    public boolean isPitOwnedBy(Pit pit, int player) {
        return pit.row() == player;
    }

    public boolean isPitMancala(Pit pit) {
        return pit.col() == mancalaPosition;
    }

    public boolean hasOneStoneIn(Pit pit) {
        return pits[pit.row()][pit.col()] == 1;
    }

    public void moveAllPitStonesToMancalaOf(Pit pit, int player) {
        int stones = pits[pit.row()][pit.col()];
        pits[pit.row()][pit.col()] = 0;
        pits[player][mancalaPosition] += stones;
    }

    public boolean areAllPitsEmptyInAnyRow() {
        return countStonesInPitsOf(0) == 0
                || countStonesInPitsOf(1) == 0;
    }

    public void moveAllStonesFromAllPitsToMancalaOf(int player) {
        for (int i = 0; i < pitsInRow; i++) {
            moveAllPitStonesToMancalaOf(new Pit(player, i), player);
        }
    }

    public int countStonesInMancalaOf(int player) {
        return pits[player][mancalaPosition];
    }

    public static BoardBuilder builder() {
        return new BoardBuilder();
    }

    @Override
    public String toString() {
        return String.format("%s\n%s", Arrays.toString(pits[0]), Arrays.toString(pits[1]));
    }

    private int[][] initPits(int stonesInPit) {
        int[][] pits = new int[2][pitsInRow + 1];
        Arrays.fill(pits[0], 0, pitsInRow, stonesInPit);
        Arrays.fill(pits[1], 0, pitsInRow, stonesInPit);

        return pits;
    }

    private int countStonesInPitsOf(int player) {
        int count = 0;
        for (int i = 0; i < pitsInRow; i++) {
            count += pits[player][i];
        }

        return count;
    }

    public static class BoardBuilder {

        private int pitsInRow;
        private int stonesInPit;

        public BoardBuilder pitsInRow(int pitsInRow) {
            this.pitsInRow = pitsInRow;
            return this;
        }

        public BoardBuilder stonesInPit(int stones) {
            this.stonesInPit = stones;
            return this;
        }

        public Board build() {
            return new Board(pitsInRow, stonesInPit);
        }
    }
}
