package me.ex1tus.mancala.game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board(
                new int[][]{
                        {0, 1, 2, 3, 4, 5, 0},
                        {0, 1, 2, 3, 4, 5, 0}
                });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    @DisplayName("Take all stones from a pit")
    public void shouldTakeAllStonesFromPit(int currentPit) {
        // When
        int stones = board.takeAllStonesFrom(new Pit(0, currentPit));

        // Then
        assertEquals(currentPit, stones);
        assertEquals(0, board.takeAllStonesFrom(new Pit(0, currentPit)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    @DisplayName("Place one stone to a pit")
    public void shouldPlaceOneStoneToPit(int currentPit) {
        // When
        board.placeOneStoneTo(new Pit(0, currentPit));

        // Then
        assertEquals(currentPit + 1, board.takeAllStonesFrom(new Pit(0, currentPit)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    @DisplayName("Get opposite pit")
    public void shouldGetOppositePit(int currentPit) {
        // When
        Pit oppositePit = board.getPitOppositeTo(new Pit(0, currentPit));

        // Then
        assertEquals(new Pit(1, 6 - currentPit - 1), oppositePit);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    @DisplayName("Get next available pit in the same row")
    public void shouldGetNextAvailablePitInTheSameRow(int currentPit) {
        // When
        Pit next = board.getNextAvailablePitAfter(new Pit(0, currentPit), 0);

        // Then
        assertEquals(new Pit(0, currentPit + 1), next);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    @DisplayName("Get next available pit in the opposite row")
    public void shouldGetNextAvailablePitInTheOppositeRow(int currentPit) {
        // When
        Pit next = board.getNextAvailablePitAfter(new Pit(1, currentPit), 0);

        // Then
        assertEquals(new Pit(1, currentPit + 1), next);
    }

    @Test
    @DisplayName("Get next available pit by switching row")
    public void shouldGetNextAvailablePitBySwitchingRow() {
        // When
        Pit next = board.getNextAvailablePitAfter(new Pit(0, 6), 0);

        // Then
        assertEquals(new Pit(1, 0), next);
    }

    @Test
    @DisplayName("Get next available pit if it’s player’s mancala")
    public void shouldGetNextAvailablePitIfItsPlayersMancala() {
        // When
        Pit next = board.getNextAvailablePitAfter(new Pit(0, 5), 0);

        // Then
        assertEquals(new Pit(0, 6), next);
    }

    @Test
    @DisplayName("Get next available pit by skipping opponent’s mancala")
    public void shouldGetNextAvailablePitBySkippingOpponentsMancala() {
        // When
        Pit next = board.getNextAvailablePitAfter(new Pit(1, 5), 0);

        // Then
        assertEquals(new Pit(0, 0), next);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6})
    @DisplayName("Determine pit’s owner")
    public void shouldDeterminePitsOwner(int currentPit) {
        // When - Then
        assertTrue(board.isPitOwnedBy(new Pit(0, currentPit), 0));
        assertFalse(board.isPitOwnedBy(new Pit(1, currentPit), 0));
        assertTrue(board.isPitOwnedBy(new Pit(1, currentPit), 1));
        assertFalse(board.isPitOwnedBy(new Pit(1, currentPit), 0));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    @DisplayName("Determine if pit is not mancala")
    public void shouldDetermineIfPitIsNotMancala(int currentPit) {
        // When - Then
        assertFalse(board.isPitMancala(new Pit(0, currentPit)));
        assertFalse(board.isPitMancala(new Pit(1, currentPit)));
    }

    @Test
    @DisplayName("Determine if pit is mancala")
    public void shouldDetermineIfPitIsMancala() {
        // When - Then
        assertTrue(board.isPitMancala(new Pit(0, 6)));
        assertTrue(board.isPitMancala(new Pit(1, 6)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 3, 4, 5})
    @DisplayName("Determine if pit has more than one stone in it")
    public void shouldDetermineIfPitHasMoreThanOneStoneInIt(int currentPit) {
        // When - Then
        assertFalse(board.hasOneStoneIn(new Pit(0, currentPit)));
        assertFalse(board.hasOneStoneIn(new Pit(1, currentPit)));
    }

    @Test
    @DisplayName("Determine if pit has one stone in it")
    public void shouldDetermineIfHasOneStoneInIt() {
        // When - Then
        assertTrue(board.hasOneStoneIn(new Pit(0, 1)));
        assertTrue(board.hasOneStoneIn(new Pit(1, 1)));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    @DisplayName("Move all pit stones to mancala and count them")
    public void shouldMoveAllPitStonesToMancalaAndCountThem(int currentPit) {
        // When
        board.moveAllPitStonesToMancalaOf(new Pit(0, currentPit), 1);

        // Then
        assertEquals(currentPit, board.countStonesInMancalaOf(1));
        assertEquals(0, board.takeAllStonesFrom(new Pit(0, currentPit)));
    }
}