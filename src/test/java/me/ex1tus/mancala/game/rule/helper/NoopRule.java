package me.ex1tus.mancala.game.rule.helper;

import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.rule.GameRule;

public class NoopRule implements GameRule {

    @Override
    public Game apply(Game game) {
        return game;
    }
}
