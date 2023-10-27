package me.ex1tus.mancala.game.repository.impl;

import lombok.RequiredArgsConstructor;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.repository.GameRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "features.redis.enabled", havingValue = "true")
public class RedisGameRepository implements GameRepository {

    private final RedisTemplate<String, Game> redisTemplate;

    @Override
    public Game save(Game game) {
        redisTemplate.opsForValue().set(game.id(), game);
        return game;
    }

    @Override
    public Optional<Game> findById(String gameId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(gameId));
    }
}
