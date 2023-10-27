package me.ex1tus.mancala.game.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import me.ex1tus.mancala.game.service.messaging.MessagingService;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.model.Pit;
import me.ex1tus.mancala.game.service.GameService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Games API")
@Validated
@RestController
@RequestMapping("/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final MessagingService<Game> messagingService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new game")
    public Game create() {
        return gameService.create();
    }

    @PostMapping("/{gameId}/players")
    @Operation(summary = "Connect to an existing game")
    public Game connect(@PathVariable @UUID String gameId) {
        return messagingService.publish(
                gameService.connect(gameId));
    }

    @DeleteMapping("/{gameId}/players/{playerId}")
    @Operation(summary = "Disconnect from an existing game")
    public Game disconnect(
            @PathVariable @UUID String gameId,
            @PathVariable @Min(0) @Max(1) int playerId) {
        return messagingService.publish(
                gameService.disconnect(gameId, playerId));
    }

    @PostMapping("/{gameId}/turns")
    @Operation(summary = "Make a new turn")
    public Game makeTurn(
            @PathVariable @UUID String gameId,
            @RequestBody @Valid Pit pit) {
        return messagingService.publish(
                gameService.makeTurn(gameId, pit));
    }
}
