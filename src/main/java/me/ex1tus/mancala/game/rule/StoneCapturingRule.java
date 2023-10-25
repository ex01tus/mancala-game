package me.ex1tus.mancala.game.rule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.model.Pit;

@Slf4j
@RequiredArgsConstructor
public class StoneCapturingRule implements GameRule {

    private final GameRule next;

    @Override
    public Game apply(Game game) {
        if (game.board().isPitOwnedBy(game.pit(), game.players().active())
                && !game.board().isPitMancala(game.pit())
                && game.board().hasOneStoneIn(game.pit())) {
            game.board().moveAllPitStonesToMancalaOf(game.pit(), game.players().active());

            Pit oppositePit = game.board().getPitOppositeTo(game.pit());
            game.board().moveAllPitStonesToMancalaOf(oppositePit, game.players().active());

            log.debug("Game={}: Player={} captured all stones from the opponent’s pit={}",
                    game.id(), game.players().active(), oppositePit.col());
        }

        return next.apply(game);
    }
}
