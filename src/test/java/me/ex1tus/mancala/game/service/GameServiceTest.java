package me.ex1tus.mancala.game.service;

import me.ex1tus.mancala.game.config.property.GameProperties;
import me.ex1tus.mancala.game.exception.GameNotFoundException;
import me.ex1tus.mancala.game.model.*;
import me.ex1tus.mancala.game.repository.GameRepository;
import me.ex1tus.mancala.game.rule.GameRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    private static final String TEST_GAME_ID = "4d7da867-ce6d-4eab-8de8-79a2e38f717b";
    private static final int TEST_PLAYER = 1;

    private GameService service;

    private GameRepository repository;
    private GameRule rules;

    @BeforeEach
    public void setUp() {
        repository = mock(GameRepository.class);
        rules = mock(GameRule.class);
        service = new GameService(
                repository,
                new GameProperties(),
                rules);
    }

    @Test
    @DisplayName("Create a new game")
    public void shouldCreateNewGame() {
        // Given
        Game game = Game.builder()
                .id(TEST_GAME_ID)
                .board(new Board(new int[][]{
                        {0, 0, 0, 0, 0, 0, 20},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .status(Status.IN_PROGRESS)
                .players(Players.withActivePlayer(0))
                .result(Result.undefined())
                .build();

        when(repository.save(any(Game.class))).thenReturn(game);

        // When
        Game created = service.create();

        // Then
        verify(repository, times(1)).save(any(Game.class));
        assertEquals(game, created);
    }

    @Test
    @DisplayName("Connect to an existing game and switch its status to IN_PROGRESS")
    public void shouldConnectToAnExistingGameAndSwitchItsStatusToInProgress() {
        // Given
        Game game = Game.builder()
                .id(TEST_GAME_ID)
                .board(new Board(new int[][]{
                        {0, 0, 0, 0, 0, 0, 20},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .status(Status.CREATED)
                .players(Players.withActivePlayer(0))
                .result(Result.undefined())
                .build();

        when(repository.findById(TEST_GAME_ID)).thenReturn(Optional.of(game));
        when(repository.save(any(Game.class))).thenReturn(game);
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);

        // When
        Game connected = service.connect(TEST_GAME_ID);

        // Then
        verify(repository, times(1)).save(captor.capture());
        assertEquals(game.withStatus(Status.IN_PROGRESS), captor.getValue());
        assertEquals(game, connected);
    }

    @Test
    @DisplayName("Disconnect from an existing game and switch its status to FINISHED")
    public void shouldDisconnectFromAnExistingGameAndSwitchItsStatusToFinished() {
        // Given
        Game game = Game.builder()
                .id(TEST_GAME_ID)
                .board(new Board(new int[][]{
                        {0, 0, 0, 0, 0, 0, 20},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .status(Status.IN_PROGRESS)
                .players(Players.withActivePlayer(0))
                .result(Result.undefined())
                .build();

        when(repository.findById(TEST_GAME_ID)).thenReturn(Optional.of(game));
        when(repository.save(any(Game.class))).thenReturn(game);
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);

        // When
        Game disconnected = service.disconnect(TEST_GAME_ID, TEST_PLAYER);

        // Then
        verify(repository, times(1)).save(captor.capture());
        assertEquals(game
                        .withStatus(Status.FINISHED)
                        .withResult(Result.withWinner(0))
                        .withPlayers(Players.withNoActivePlayer()),
                captor.getValue());
        assertEquals(game, disconnected);
    }

    @Test
    @DisplayName("Make a new turn")
    public void shouldMakeNewTurn() {
        // Given
        Game game = Game.builder()
                .id(TEST_GAME_ID)
                .board(new Board(new int[][]{
                        {0, 0, 0, 0, 0, 0, 20},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .status(Status.IN_PROGRESS)
                .players(Players.withActivePlayer(0))
                .result(Result.undefined())
                .build();

        when(repository.findById(TEST_GAME_ID)).thenReturn(Optional.of(game));
        when(repository.save(any(Game.class))).thenReturn(game);
        when(rules.apply(any(Game.class))).thenReturn(game);

        // When
        Game updated = service.makeTurn(TEST_GAME_ID, new Pit(0, 0));

        // Then
        verify(repository, times(1)).save(game);
        verify(rules, times(1)).apply(game.withPit(new Pit(0, 0)));
        assertEquals(game, updated);
    }

    @Test
    @DisplayName("Throw exception on connection attempt if the game is in a wrong status")
    public void shouldThrowExceptionOnConnectionAttemptIfGameIsInWrongStatus() {
        // Given
        Game game = Game.builder()
                .id(TEST_GAME_ID)
                .board(new Board(new int[][]{
                        {0, 0, 0, 0, 0, 0, 20},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .status(Status.IN_PROGRESS)
                .players(Players.withActivePlayer(0))
                .result(Result.undefined())
                .build();

        when(repository.findById(TEST_GAME_ID)).thenReturn(Optional.of(game));

        // When - Then
        assertThrows(GameNotFoundException.class, () -> service.connect(TEST_GAME_ID));
    }

    @Test
    @DisplayName("Throw exception on a new turn attempt if the game is in a wrong status")
    public void shouldThrowExceptionOnNewTurnAttemptIfGameIsInWrongStatus() {
        // Given
        Game game = Game.builder()
                .id(TEST_GAME_ID)
                .board(new Board(new int[][]{
                        {0, 0, 0, 0, 0, 0, 20},
                        {6, 6, 6, 6, 6, 6, 0}
                }))
                .status(Status.CREATED)
                .players(Players.withActivePlayer(0))
                .result(Result.undefined())
                .build();

        when(repository.findById(TEST_GAME_ID)).thenReturn(Optional.of(game));

        // When - Then
        assertThrows(GameNotFoundException.class, () -> service.makeTurn(TEST_GAME_ID, new Pit(0, 0)));
    }

    @Test
    @DisplayName("Throw exception on connection attempt if the game doesn’t exist")
    public void shouldThrowExceptionOnConnectionAttemptIfGameDoesNotExist() {
        // Given
        when(repository.findById(TEST_GAME_ID)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(GameNotFoundException.class, () -> service.connect(TEST_GAME_ID));
    }

    @Test
    @DisplayName("Throw exception on disconnection attempt if the game doesn’t exist")
    public void shouldThrowExceptionOnDisconnectionAttemptIfGameDoesNotExist() {
        // Given
        when(repository.findById(TEST_GAME_ID)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(GameNotFoundException.class, () -> service.disconnect(TEST_GAME_ID, TEST_PLAYER));
    }

    @Test
    @DisplayName("Throw exception on a new turn attempt if the game doesn’t exist")
    public void shouldThrowExceptionOnNewTurnAttemptIfGameDoesNotExist() {
        // Given
        when(repository.findById(TEST_GAME_ID)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(GameNotFoundException.class, () -> service.makeTurn(TEST_GAME_ID, new Pit(0, 0)));
    }
}
