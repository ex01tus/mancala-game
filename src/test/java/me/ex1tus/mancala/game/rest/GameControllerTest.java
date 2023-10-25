package me.ex1tus.mancala.game.rest;

import me.ex1tus.mancala.game.exception.GameNotFoundException;
import me.ex1tus.mancala.game.model.*;
import me.ex1tus.mancala.game.service.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    private static final String TEST_GAME_ID = "4d7da867-ce6d-4eab-8de8-79a2e38f717b";
    private static final String INVALID_GAME_ID = "79a2e38f717b";
    private static final int TEST_PLAYER = 1;
    private static final int INVALID_PLAYER = 3;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @MockBean
    private GameService gameService;

    @Test
    @DisplayName("Create a new game")
    public void shouldCreateNewGame() throws Exception {
        // Given
        Game created = Game.builder()
                .id(TEST_GAME_ID)
                .board(Board.builder()
                        .pitsInRow(6)
                        .stonesInPit(6)
                        .build())
                .pit(null)
                .players(Players.withActivePlayer(0))
                .status(Status.CREATED)
                .result(Result.undefined())
                .build();

        // When
        when(gameService.create()).thenReturn(created);

        // Then
        mockMvc.perform(post("/v1/games/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .json(fileContent("game-created-response-created.json")));
    }

    @Test
    @DisplayName("Connect to an existing game")
    public void shouldConnectToAnExistingGame() throws Exception {
        // Given
        Game connected = Game.builder()
                .id(TEST_GAME_ID)
                .board(Board.builder()
                        .pitsInRow(6)
                        .stonesInPit(6)
                        .build())
                .pit(null)
                .players(Players.withActivePlayer(1))
                .status(Status.IN_PROGRESS)
                .result(Result.undefined())
                .build();

        // When
        when(gameService.connect(TEST_GAME_ID)).thenReturn(connected);

        // Then
        mockMvc.perform(post("/v1/games/" + TEST_GAME_ID + "/players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(fileContent("game-connected-response-ok.json")));
    }

    @Test
    @DisplayName("Disconnect from an existing game")
    public void shouldDisconnectFromAnExistingGame() throws Exception {
        // Given
        Game disconnected = Game.builder()
                .id(TEST_GAME_ID)
                .board(Board.builder()
                        .pitsInRow(6)
                        .stonesInPit(6)
                        .build())
                .pit(null)
                .players(Players.withNoActivePlayer())
                .status(Status.FINISHED)
                .result(Result.withWinner(0))
                .build();

        // When
        when(gameService.disconnect(TEST_GAME_ID, TEST_PLAYER)).thenReturn(disconnected);

        // Then
        mockMvc.perform(delete("/v1/games/" + TEST_GAME_ID + "/players/" + TEST_PLAYER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(fileContent("game-disconnected-response-ok.json")));
    }

    @Test
    @DisplayName("Make a new turn")
    public void shouldMakeNewTurn() throws Exception {
        // Given
        Game updated = Game.builder()
                .id(TEST_GAME_ID)
                .board(Board.builder()
                        .pitsInRow(6)
                        .stonesInPit(6)
                        .build())
                .pit(null)
                .players(Players.withActivePlayer(0))
                .status(Status.IN_PROGRESS)
                .result(Result.undefined())
                .build();

        // When
        when(gameService.makeTurn(TEST_GAME_ID, new Pit(1, 2))).thenReturn(updated);

        // Then
        mockMvc.perform(post("/v1/games/" + TEST_GAME_ID + "/turns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fileContent("turn-request-ok.json")))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(fileContent("game-updated-response-ok.json")));
    }

    @Test
    @DisplayName("Return 400 on connection attempt with invalid game id")
    public void shouldReturnBadRequestOnConnectionAttemptWithInvalidGameId() throws Exception {
        // Given - When - Then
        mockMvc.perform(post("/v1/games/" + INVALID_GAME_ID + "/players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .json(fileContent("game-connected-response-bad-request.json")));
    }

    @Test
    @DisplayName("Return 400 on disconnection attempt with invalid game id")
    public void shouldReturnBadRequestOnDisconnectionAttemptWithInvalidGameId() throws Exception {
        // Given - When - Then
        mockMvc.perform(delete("/v1/games/" + INVALID_GAME_ID + "/players/" + TEST_PLAYER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .json(fileContent("game-disconnected-response-bad-request-game-id.json")));
    }

    @Test
    @DisplayName("Return 400 on new turn attempt with invalid game id")
    public void shouldReturnBadRequestOnNewTurnAttemptWithInvalidGameId() throws Exception {
        // Given - When - Then
        mockMvc.perform(post("/v1/games/" + INVALID_GAME_ID + "/turns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fileContent("turn-request-ok.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .json(fileContent("game-updated-response-bad-request-game-id.json")));
    }

    @Test
    @DisplayName("Return 400 on disconnection attempt with invalid player id")
    public void shouldReturnBadRequestOnDisconnectionAttemptWithInvalidPlayerId() throws Exception {
        // Given - When - Then
        mockMvc.perform(delete("/v1/games/" + TEST_GAME_ID + "/players/" + INVALID_PLAYER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .json(fileContent("game-disconnected-response-bad-request-player-id.json")));
    }

    @Test
    @DisplayName("Return 400 on new turn attempt with invalid pit")
    public void shouldReturnBadRequestOnNewTurnAttemptWithInvalidPit() throws Exception {
        // Given - When - Then
        mockMvc.perform(post("/v1/games/" + TEST_GAME_ID + "/turns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fileContent("turn-request-invalid.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .json(fileContent("game-updated-response-bad-request-pit.json")));
    }

    @Test
    @DisplayName("Return 404 on connection attempt if game doesn’t exist")
    public void shouldReturnNotFoundOnConnectionAttemptIfGameDoesntExist() throws Exception {
        // Given - When
        when(gameService.connect(TEST_GAME_ID)).thenThrow(GameNotFoundException.class);

        // Then
        mockMvc.perform(post("/v1/games/" + TEST_GAME_ID + "/players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Return 404 on disconnection attempt if game doesn’t exist")
    public void shouldReturnNotFoundOnDisconnectionAttemptIfGameDoesntExist() throws Exception {
        // Given - When
        when(gameService.disconnect(TEST_GAME_ID, TEST_PLAYER)).thenThrow(GameNotFoundException.class);

        // Then
        mockMvc.perform(delete("/v1/games/" + TEST_GAME_ID + "/players/" + TEST_PLAYER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Return 404 on new turn attempt if game doesn’t exist")
    public void shouldReturnNotFoundOnNewTurnAttemptIfGameDoesntExist() throws Exception {
        // When
        when(gameService.makeTurn(TEST_GAME_ID, new Pit(1, 2))).thenThrow(GameNotFoundException.class);

        // Then
        mockMvc.perform(post("/v1/games/" + TEST_GAME_ID + "/turns")
                        .content(fileContent("turn-request-ok.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private String fileContent(String fileName) throws IOException {
        return resourceLoader.getResource("classpath:json/" + fileName)
                .getContentAsString(StandardCharsets.UTF_8);
    }
}
