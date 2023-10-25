package me.ex1tus.mancala.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import me.ex1tus.mancala.game.config.property.GameProperties;

import java.util.UUID;

@Builder
public record Game(
        String id,
        Board board,
        @JsonIgnore Pit pit,
        Players players,
        Status status,
        Result result) {

    public static Game initWith(GameProperties properties) {
        return Game.builder()
                .id(UUID.randomUUID().toString())
                .board(Board.builder()
                        .pitsInRow(properties.getPitsInRow())
                        .stonesInPit(properties.getStonesInPit())
                        .build())
                .pit(null)
                .players(Players.withActivePlayer(0))
                .status(Status.CREATED)
                .result(Result.undefined())
                .build();
    }

    public Game withPit(Pit updated) {
        return Game.builder()
                .id(id)
                .board(board)
                .pit(updated)
                .players(players)
                .status(status)
                .result(result)
                .build();
    }

    public Game withPlayers(Players updated) {
        return Game.builder()
                .id(id)
                .board(board)
                .pit(pit)
                .players(updated)
                .status(status)
                .result(result)
                .build();
    }

    public Game withStatus(Status updated) {
        return Game.builder()
                .id(id)
                .board(board)
                .pit(pit)
                .players(players)
                .status(updated)
                .result(result)
                .build();
    }

    public Game withResult(Result updated) {
        return Game.builder()
                .id(id)
                .board(board)
                .pit(pit)
                .players(players)
                .status(status)
                .result(updated)
                .build();
    }

    @Override
    public String toString() {
        return String.format("Id: %s\nBoard:\n%s\nPlayers: %s\nResult: %s", id, board, players, result);
    }
}
