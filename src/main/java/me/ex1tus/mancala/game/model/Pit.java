package me.ex1tus.mancala.game.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record Pit(
        @Min(0) @Max(1) int row,
        @Min(0) @Max(5) int col) {
}
