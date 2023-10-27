package me.ex1tus.mancala.game.repository.impl;

import com.redis.testcontainers.RedisContainer;
import me.ex1tus.mancala.game.model.*;
import me.ex1tus.mancala.game.repository.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class RedisGameRepositoryTest {

    private static final String TEST_GAME_ID = "4d7da867-ce6d-4eab-8de8-79a2e38f717b";
    private static final String INVALID_GAME_ID = "79a2e38f717b";

    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine"))
                    .withExposedPorts(6379);

    @Autowired
    private GameRepository redisGameRepository;

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
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
        Game saved = redisGameRepository.save(game);

        // Then
        assertEquals(game, saved);
        Optional<Game> found = redisGameRepository.findById(TEST_GAME_ID);
        assertTrue(found.isPresent());
        assertEquals(game, found.get());
    }

    @Test
    @DisplayName("Return empty optional if the game was’t found")
    void shouldReturnEmptyOptionalIfGameWasNotFound() {
        // Given - When
        Optional<Game> found = redisGameRepository.findById(INVALID_GAME_ID);

        // Then
        assertTrue(found.isEmpty());
    }
}