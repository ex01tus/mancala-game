package me.ex1tus.mancala.game.rule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ex1tus.mancala.game.model.Game;

@Slf4j
@RequiredArgsConstructor
public class TurnSwitchRule implements GameRule {

    private final GameRule next;

    @Override
    public Game apply(Game game) {
        if (game.board().isPitOwnedBy(game.pit(), game.players().active())
                && game.board().isPitMancala(game.pit())) {
            log.debug("Game={}: player={} gained another turn", game.id(), game.players().active());
            return next.apply(game);
        }

        return next.apply(
                game.withPlayers(
                        game.players().withSwitchedPlayer()));
    }
}
