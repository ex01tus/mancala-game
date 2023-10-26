package me.ex1tus.mancala.game.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ex1tus.mancala.game.config.property.GameProperties;
import me.ex1tus.mancala.game.exception.GameNotFoundException;
import me.ex1tus.mancala.game.model.*;
import me.ex1tus.mancala.game.repository.GameRepository;
import me.ex1tus.mancala.game.rule.GameRule;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository repository;
    private final GameProperties properties;
    private final GameRule rules;

    public Game create() {
        Game created = repository.save(Game.initWith(properties));
        log.info("Game={}: player=0 connected", created.id());

        return created;
    }

    public Game connect(String gameId) {
        Game connected = repository.findById(gameId)
                .filter(game -> game.status() == Status.CREATED)
                .map(game -> game.withStatus(Status.IN_PROGRESS))
                .map(repository::save)
                .orElseThrow(GameNotFoundException::new);
        log.info("Game={}: player=1 connected", connected.id());

        return connected;
    }

    public Game disconnect(String gameId, int playerId) {
        Game disconnected = repository.findById(gameId)
                .map(game -> game.withStatus(Status.FINISHED))
                .map(game -> game.withPlayers(Players.withNoActivePlayer()))
                .map(game -> game.withResult(Result.withWinner(new Players(playerId).withSwitchedPlayer().active())))
                .map(repository::save)
                .orElseThrow(GameNotFoundException::new);
        log.info("Game={}: player={} disconnected", disconnected.id(), playerId);

        return disconnected;
    }

    public Game makeTurn(String gameId, Pit pit) {
        Game updated = repository.findById(gameId)
                .filter(game -> game.status() == Status.IN_PROGRESS)
                .map(game -> game.withPit(pit))
                .map(rules::apply)
                .map(repository::save)
                .orElseThrow(GameNotFoundException::new);
        log.info("Game={}: registered a new turn={}", gameId, pit);

        return updated;
    }
}
