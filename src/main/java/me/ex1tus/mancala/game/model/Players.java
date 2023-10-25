package me.ex1tus.mancala.game.model;

public record Players(int active) {

    public Players withSwitchedPlayer() {
        return new Players(passive());
    }

    public static Players withActivePlayer(int active) {
        return new Players(active);
    }

    public static Players withNoActivePlayer() {
        return new Players(-1);
    }

    public int passive() {
        return (active + 1) % 2;
    }

    @Override
    public String toString() {
        return String.format("Players[active=%s, passive=%s]", active, passive());
    }
}
