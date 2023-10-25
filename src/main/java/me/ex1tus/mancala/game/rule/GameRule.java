package me.ex1tus.mancala.game.rule;

import me.ex1tus.mancala.game.model.Game;

public interface GameRule {

    Game apply(Game game);
}
