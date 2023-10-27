package me.ex1tus.mancala.game.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import me.ex1tus.mancala.game.exception.GameNotFoundException;
import me.ex1tus.mancala.game.rest.model.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String INVALID_REQUEST_MESSAGE = "Invalid request";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(
            responseCode = "404",
            description = "Game with the specified id either doesn’t exist or unavailable")
    @ExceptionHandler(GameNotFoundException.class)
    public void gameNotFound() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ValidationErrorResponse badRequest(ConstraintViolationException exception) {
        return new ValidationErrorResponse(
                INVALID_REQUEST_MESSAGE,
                formatConstraintViolations(exception.getConstraintViolations()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorResponse badRequest(MethodArgumentNotValidException exception) {
        return new ValidationErrorResponse(
                INVALID_REQUEST_MESSAGE,
                formatFieldErrors(exception.getFieldErrors()));
    }

    private List<String> formatConstraintViolations(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage()))
                .toList();
    }

    private List<String> formatFieldErrors(List<FieldError> errors) {
        return errors.stream()
                .map(e -> String.format("%s: %s", e.getField(), e.getDefaultMessage()))
                .toList();
    }
}
