package me.ex1tus.mancala.game.service;

import lombok.RequiredArgsConstructor;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.model.Pit;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    public Game create() {
        return null;
    }

    public Game connect(String gameId) {
        return null;
    }

    public Game disconnect(String gameId, int playerId) {
        return null;
    }

    public Game makeTurn(String gameId, Pit pit) {
        return null;
    }
}
