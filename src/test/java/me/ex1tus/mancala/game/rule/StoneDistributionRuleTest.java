package me.ex1tus.mancala.game.rule;

import me.ex1tus.mancala.game.model.Board;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.model.Pit;
import me.ex1tus.mancala.game.model.Players;
import me.ex1tus.mancala.game.rule.helper.NoopRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoneDistributionRuleTest {

    private GameRule rule;

    @BeforeEach
    public void setUp() {
        rule = new StoneDistributionRule(new NoopRule());
    }

    @Test
    @DisplayName("Distribute stones, ignoring opponent's mancala")
    public void shouldDistributeStonesIgnoringOpponentsMancala() {
        // Given
        Game game = Game.builder()
                .board(new Board(new int[][]{
                        {100, 6, 6, 6, 6, 6, 0},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .pit(new Pit(0, 0))
                .players(Players.withActivePlayer(0))
                .build();

        // When
        Game updated = rule.apply(game);

        // Then
        assertEquals(
                new Board(new int[][]{
                        {7, 14, 14, 14, 14, 14, 8},
                        {14, 14, 14, 13, 13, 13, 0}
                }),
                updated.board());
    }
}