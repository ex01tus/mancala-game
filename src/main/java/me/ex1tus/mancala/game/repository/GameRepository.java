package me.ex1tus.mancala.game.repository;

import me.ex1tus.mancala.game.model.Game;

import java.util.Optional;

public interface GameRepository {

    Game save(Game game);
    Optional<Game> findById(String gameId);
}
