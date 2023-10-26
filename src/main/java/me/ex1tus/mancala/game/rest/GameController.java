package me.ex1tus.mancala.game.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import me.ex1tus.mancala.game.model.Game;
import me.ex1tus.mancala.game.model.Pit;
import me.ex1tus.mancala.game.service.GameService;
import me.ex1tus.mancala.game.service.messaging.MessagingService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final MessagingService<Game> messagingService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Game create() {
        return gameService.create();
    }

    @PostMapping("/{gameId}/players")
    public Game connect(@PathVariable @UUID String gameId) {
        return messagingService.publish(
                gameService.connect(gameId));
    }

    @DeleteMapping("/{gameId}/players/{playerId}")
    public Game disconnect(
            @PathVariable @UUID String gameId,
            @PathVariable @Min(0) @Max(1) int playerId) {
        return messagingService.publish(
                gameService.disconnect(gameId, playerId));
    }

    @PostMapping("/{gameId}/turns")
    public Game makeTurn(
            @PathVariable @UUID String gameId,
            @RequestBody @Valid Pit pit) {
        return messagingService.publish(
                gameService.makeTurn(gameId, pit));
    }
}
