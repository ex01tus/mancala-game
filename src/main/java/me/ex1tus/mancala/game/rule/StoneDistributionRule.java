package me.ex1tus.mancala.game.rule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.model.Pit;

@Slf4j
@RequiredArgsConstructor
public class StoneDistributionRule implements GameRule {

    private final GameRule next;

    @Override
    public Game apply(Game game) {
        int stonesLeft = game.board().takeAllStonesFrom(game.pit());
        Pit currentPit = game.pit();
        log.debug("Game={}: player={} distributed {} stones from the pit={}",
                game.id(), game.players().active(), stonesLeft, currentPit.col());

        while (stonesLeft > 0) {
            currentPit = game.board().getNextAvailablePitAfter(currentPit, game.players().active());
            game.board().placeOneStoneTo(currentPit);
            stonesLeft--;
        }

        return next.apply(game.withPit(currentPit));
    }
}
