package me.ex1tus.mancala.game.rule;

import me.ex1tus.mancala.game.model.Board;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.model.Players;
import me.ex1tus.mancala.game.model.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameOverRuleTest {

    private GameRule rule;

    @BeforeEach
    public void setUp() {
        rule = new GameOverRule();
    }

    @Test
    @DisplayName("Move all stones to mancalas and determine a winner")
    public void shouldMoveAllStonesToMancalasAndDetermineWinner() {
        // Given
        Game game = Game.builder()
                .board(new Board(new int[][]{
                        {0, 0, 0, 0, 0, 0, 20},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .players(Players.withActivePlayer(0))
                .result(Result.undefined())
                .build();

        // When
        Game updated = rule.apply(game);

        // Then
        assertEquals(1, updated.result().winner());
        assertEquals(-1, updated.players().active());
        assertEquals(20, updated.board().countStonesInMancalaOf(0));
        assertEquals(36, updated.board().countStonesInMancalaOf(1));
    }

    @Test
    @DisplayName("Don’t determine a winner if there’re stones left")
    public void shouldNotDetermineWinnerIfThereAreStonesLeft() {
        // Given
        Game game = Game.builder()
                .board(new Board(new int[][]{
                        {0, 1, 0, 0, 0, 0, 20},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .players(Players.withActivePlayer(0))
                .result(Result.undefined())
                .build();

        // When
        Game updated = rule.apply(game);

        // Then
        assertFalse(updated.result().isDefined());
        assertEquals(0, updated.players().active());
        assertEquals(20, updated.board().countStonesInMancalaOf(0));
        assertEquals(0, updated.board().countStonesInMancalaOf(1));
    }
}