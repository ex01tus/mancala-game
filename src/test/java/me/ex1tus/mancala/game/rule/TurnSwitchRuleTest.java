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

public class TurnSwitchRuleTest {

    private GameRule rule;

    @BeforeEach
    public void setUp() {
        rule = new TurnSwitchRule(new NoopRule());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    @DisplayName("Switch active player")
    public void shouldSwitchActivePlayer(int activePlayer) {
        // Given
        Game game = Game.builder()
                .board(new Board(new int[][]{
                        {6, 6, 6, 6, 6, 6, 0},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .pit(new Pit(activePlayer, 3))
                .players(Players.withActivePlayer(activePlayer))
                .build();

        // When
        Game updated = rule.apply(game);

        // Then
        assertEquals((activePlayer + 1) % 2, updated.players().active());
    }

    @Test
    @DisplayName("Switch active player if pit is opponent’s mancala")
    public void shouldSwitchActivePlayerIfPitIsOpponentsMancala() {
        // Given
        Game game = Game.builder()
                .board(new Board(new int[][]{
                        {6, 6, 6, 6, 6, 6, 0},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .pit(new Pit(1, 6))
                .players(Players.withActivePlayer(0))
                .build();

        // When
        Game updated = rule.apply(game);

        // Then
        assertEquals(1, updated.players().active());
    }

    @Test
    @DisplayName("Don’t switch active player if pit is theirs mancala")
    public void shouldNotSwitchActivePlayerIfPitIsTheirsMancala() {
        // Given
        Game game = Game.builder()
                .board(new Board(new int[][]{
                        {6, 6, 6, 6, 6, 6, 0},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .pit(new Pit(1, 6))
                .players(Players.withActivePlayer(1))
                .build();

        // When
        Game updated = rule.apply(game);

        // Then
        assertEquals(1, updated.players().active());
    }
}
