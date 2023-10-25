package me.ex1tus.mancala.game.rest.model;

import java.util.List;

public record ValidationErrorResponse(String message, List<String> errors) {
}
