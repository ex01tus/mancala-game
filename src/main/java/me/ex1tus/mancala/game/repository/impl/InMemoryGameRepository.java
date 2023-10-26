package me.ex1tus.mancala.game.repository.impl;

import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.repository.GameRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "features.redis.enabled", havingValue = "false")
public class InMemoryGameRepository implements GameRepository {

    private final Map<String, Game> games;

    public InMemoryGameRepository() {
        this.games = new HashMap<>();
    }

    @Override
    public Game save(Game game) {
        games.put(game.id(), game);
        return game;
    }

    @Override
    public Optional<Game> findById(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }
}
