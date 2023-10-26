package me.ex1tus.mancala.game.repository.impl;

import me.ex1tus.mancala.game.model.*;
import me.ex1tus.mancala.game.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryGameRepositoryTest {

    private static final String TEST_GAME_ID = "4d7da867-ce6d-4eab-8de8-79a2e38f717b";
    private static final String INVALID_GAME_ID = "79a2e38f717b";

    private GameRepository inMemoryGameRepository;

    @BeforeEach
    public void setUp() {
        inMemoryGameRepository = new InMemoryGameRepository();
    }

    @Test
    @DisplayName("Save and find a game")
    void shouldSaveAndFindGame() {
        // Given
        Game game = Game.builder()
                .id(TEST_GAME_ID)
                .board(new Board(new int[][]{
                        {0, 0, 0, 0, 0, 0, 20},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .status(Status.IN_PROGRESS)
                .players(Players.withActivePlayer(0))
                .result(Result.undefined())
                .build();

        // When
        Game saved = inMemoryGameRepository.save(game);

        // Then
        assertEquals(game, saved);
        Optional<Game> found = inMemoryGameRepository.findById(TEST_GAME_ID);
        assertTrue(found.isPresent());
        assertEquals(game, found.get());
    }

    @Test
    @DisplayName("Return empty optional if the game was’t found")
    void shouldReturnEmptyOptionalIfGameWasNotFound() {
        // Given - When
        Optional<Game> found = inMemoryGameRepository.findById(INVALID_GAME_ID);

        // Then
        assertTrue(found.isEmpty());
    }
}