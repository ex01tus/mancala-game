package me.ex1tus.mancala.game.rule;

import me.ex1tus.mancala.game.model.Board;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.model.Pit;
import me.ex1tus.mancala.game.model.Players;
import me.ex1tus.mancala.game.rule.helper.NoopRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoneCapturingRuleTest {

    private GameRule rule;

    @BeforeEach
    public void setUp() {
        rule = new StoneCapturingRule(new NoopRule());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    @DisplayName("Capture stones from an opposite pit and put them to mancala")
    public void shouldCaptureStonesFromOppositePitAndPutThemToMancala(int currentPit) {
        // Given
        Game game = Game.builder()
                .board(getBoardWithOneStoneIn(new Pit(0, currentPit)))
                .pit(new Pit(0, currentPit))
                .players(Players.withActivePlayer(0))
                .build();

        // When
        Game updated = rule.apply(game);
        System.out.println(updated.board());

        // Then
        int oppositePit = 6 - currentPit - 1;
        assertEquals(0, updated.board().takeAllStonesFrom(new Pit(1, oppositePit)));
        assertEquals(7, updated.board().countStonesInMancalaOf(0));
    }

    @Test
    @DisplayName("Don’t capture stones if pit belongs to an opponent")
    public void shouldNotCaptureStonesIfPitBelongsToOpponent() {
        // Given
        Game game = Game.builder()
                .board(new Board(new int[][]{
                        {1, 6, 6, 6, 6, 6, 0},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .pit(new Pit(0, 0))
                .players(Players.withActivePlayer(1))
                .build();

        // When
        Game updated = rule.apply(game);

        // Then
        assertEquals(
                new Board(new int[][]{
                        {1, 6, 6, 6, 6, 6, 0},
                        {6, 6, 6, 6, 6, 6, 0},
                }),
                updated.board());
    }

    @Test
    @DisplayName("Don’t capture stones if pit is mancala")
    public void shouldNotCaptureStonesIfPitIsMancala() {
        // Given
        Game game = Game.builder()
                .board(new Board(new int[][]{
                        {6, 6, 6, 6, 6, 6, 6},
                        {6, 6, 6, 6, 6, 6, 1}
                }))
                .pit(new Pit(1, 6))
                .players(Players.withActivePlayer(1))
                .build();

        // When
        Game updated = rule.apply(game);

        // Then
        assertEquals(
                new Board(new int[][]{
                        {6, 6, 6, 6, 6, 6, 6},
                        {6, 6, 6, 6, 6, 6, 1},
                }),
                updated.board());
    }

    @Test
    @DisplayName("Don’t capture stones if there’re more than 1 stone in a pit")
    public void shouldNotCaptureStonesIfThereAreMoreThanOneStoneInPit() {
        // Given
        Game game = Game.builder()
                .board(new Board(new int[][]{
                        {6, 6, 6, 6, 6, 6, 0},
                        {2, 6, 6, 6, 6, 6, 0}
                }))
                .pit(new Pit(1, 0))
                .players(Players.withActivePlayer(1))
                .build();

        // When
        Game updated = rule.apply(game);

        // Then
        assertEquals(
                new Board(new int[][]{
                        {6, 6, 6, 6, 6, 6, 0},
                        {2, 6, 6, 6, 6, 6, 0},
                }),
                updated.board());
    }

    private Board getBoardWithOneStoneIn(Pit pit) {
        int[][] pits = new int[][]{
                {6, 6, 6, 6, 6, 6, 0},
                {6, 6, 6, 6, 6, 6, 0}
        };
        pits[pit.row()][pit.col()] = 1;

        return new Board(pits);
    }
}
