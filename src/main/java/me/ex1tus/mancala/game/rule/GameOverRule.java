package me.ex1tus.mancala.game.rule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.model.Players;
import me.ex1tus.mancala.game.model.Result;
import me.ex1tus.mancala.game.model.Status;

@Slf4j
@RequiredArgsConstructor
public class GameOverRule implements GameRule {

    @Override
    public Game apply(Game game) {
        boolean isGameOver = game.board().areAllPitsEmptyInAnyRow();
        if (!isGameOver) {
            return game;
        }

        game.board().moveAllStonesFromAllPitsToMancalaOf(game.players().active());
        int activePlayerScore = game.board().countStonesInMancalaOf(game.players().active());

        game.board().moveAllStonesFromAllPitsToMancalaOf(game.players().passive());
        int passivePlayerScore = game.board().countStonesInMancalaOf(game.players().passive());

        int winner = activePlayerScore > passivePlayerScore
                ? game.players().active()
                : game.players().passive();

        log.info("Game={}: player={} won", game.id(), winner);
        return game
                .withStatus(Status.FINISHED)
                .withResult(Result.withWinner(winner))
                .withPlayers(Players.withNoActivePlayer());
    }
}
