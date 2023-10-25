package me.ex1tus.mancala.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Result(int winner) {

    private static final int UNDEFINED = -1;

    @JsonIgnore
    public boolean isDefined() {
        return winner != UNDEFINED;
    }

    public static Result undefined() {
        return new Result(UNDEFINED);
    }

    public static Result withWinner(int winner) {
        return new Result(winner);
    }

    @Override
    public String toString() {
        return String.format("Result[%s]",
                isDefined()
                        ? String.format("winner=%s", winner)
                        : "undefined");
    }
}
