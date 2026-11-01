package br.inatel.gameproject.service;

import br.inatel.gameproject.handler.exceptions.GameNotFoundException;
import br.inatel.gameproject.handler.exceptions.PublisherNotFoundException;
import br.inatel.gameproject.model.dto.GameDTO;
import br.inatel.gameproject.model.dto.PublisherDTO;
import br.inatel.gameproject.model.entity.Game;
import br.inatel.gameproject.model.entity.TimePlayed;
import br.inatel.gameproject.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    private GameService gameService;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private PublisherManagerService publisherManagerService;
    GameDTO gameDTO;
    Game game;
    UUID gameId;

    @BeforeEach
    public void setup() {
        gameId = UUID.randomUUID();
        gameDTO = GameDTO.builder()
                .id(gameId)
                .publisherId("microsoft")
                .name("Halo Infinite")
                .timePlayed(Map.of(LocalDate.of(2023, 12, 1), 20,
                        LocalDate.of(2023, 1, 1), 18,
                        LocalDate.of(2024, 2, 3), 19,
                        LocalDate.of(2024, 2, 9), 24))
                .build();
        game = Game.builder().id(gameId)
                .publisherId("microsoft")
                .name("Halo Infinite")
                .timePlayed(new ArrayList<>(Arrays.asList(
                        TimePlayed.builder().game(game)
                                .timeDate(LocalDate.of(2023, 12, 1))
                                .timeHour(20).build(),
                        TimePlayed.builder().game(game)
                                .timeDate(LocalDate.of(2023, 1, 1))
                                .timeHour(18).build(),
                        TimePlayed.builder().game(game)
                                .timeDate(LocalDate.of(2024, 2, 3))
                                .timeHour(19).build(),
                        TimePlayed.builder().game(game)
                                .timeDate(LocalDate.of(2024, 2, 9))
                                .timeHour(24).build()
                )))
                .build();
    }

    @Test
    @DisplayName("When a game is posted with a valid publisher, it should be successfully created and returned")
    void givenValidPublisher_whenCreateGameIsCalled_thenReturnCreatedGame() {
        when(publisherManagerService.getAllPublishers())
                .thenReturn(List.of(new PublisherDTO("microsoft", "Microsoft Company")));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        GameDTO result = gameService.createGame(gameDTO);
        assertEquals(gameDTO.getPublisherId(), result.getPublisherId());
        assertEquals(gameDTO.getName(), result.getName());
        assertEquals(gameDTO.getTimePlayed(), result.getTimePlayed());
        verify(gameRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("When a game is posted with an invalid publisher, it should throw a PublisherNotFoundException")
    void givenInvalidPublisher_whenCreateGameIsCalled_thenThrowPublisherNotFoundException() {
        gameDTO = GameDTO.builder().publisherId("Invalid").build();
        PublisherNotFoundException invalidPublisher = assertThrows(PublisherNotFoundException.class,
                () -> gameService.createGame(gameDTO));
        assertEquals("PublisherId was not found.", invalidPublisher.getMessage());
    }

    @Test
    @DisplayName("When games are requested for a valid publisher ID, it should return the games of that publisher")
    void givenValidPublisherId_whenGetGamesIsCalled_thenReturnAllGamesOfThatPublisher() {
        when(gameRepository.findByPublisherId(anyString()))
                .thenReturn(Collections.singletonList(game));
        List<GameDTO> result = gameService.getAllGames(gameDTO.getPublisherId());
        assertEquals(1, result.size());
        assertEquals(gameDTO.getName(), result.get(0).getName());
        verify(gameRepository, times(1)).findByPublisherId(gameDTO.getPublisherId());
    }

    @Test
    @DisplayName("When games are requested without a publisher ID, it should return all games")
    void givenNoPublisherId_whenGetGamesIsCalled_thenReturnAllAllGames() {
        when(gameRepository.findAll()).thenReturn(Collections.singletonList(game));
        List<GameDTO> result = gameService.getAllGames(null);
        assertEquals(1, result.size());
        assertEquals(gameDTO.getName(), result.get(0).getName());
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("When an existing game ID is requested, it should return the game")
    void givenExistingGameId_whenGetGameByIdIsCalled_thenReturnGame() {
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        GameDTO result = gameService.getGameById(gameId);
        assertNotNull(result);
        assertEquals(game.getName(), result.getName());
        verify(gameRepository, times(1)).findById(gameId);
    }

    @Test
    @DisplayName("When a non-existing game ID is requested, it should throw GameNotFoundException")
    void givenNonExistingGameId_whenGetGameByIdIsCalled_thenThrowGameNotFoundException() {
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());
        assertThrows(GameNotFoundException.class, () -> gameService.getGameById(gameId));
        verify(gameRepository, times(1)).findById(gameId);
    }

    @Test
    @DisplayName("When updating an existing game with valid publisher, it should return updated game")
    void givenExistingGameAndValidPublisher_whenUpdateGameIsCalled_thenReturnUpdatedGame() {
        GameDTO updatedDTO = GameDTO.builder()
                .publisherId("microsoft")
                .name("Halo Infinite Updated")
                .timePlayed(Map.of(LocalDate.of(2024, 5, 1), 10))
                .build();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(publisherManagerService.getAllPublishers())
                .thenReturn(List.of(new PublisherDTO("microsoft", "Microsoft Company")));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GameDTO result = gameService.updateGame(gameId, updatedDTO);
        assertEquals("Halo Infinite Updated", result.getName());
        assertEquals("microsoft", result.getPublisherId());
        assertEquals(1, result.getTimePlayed().size());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    @DisplayName("When updating a non-existing game, it should throw GameNotFoundException")
    void givenNonExistingGame_whenUpdateGameIsCalled_thenThrowGameNotFoundException() {
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());
        assertThrows(GameNotFoundException.class, () -> gameService.updateGame(gameId, gameDTO));
        verify(gameRepository, never()).save(any());
    }

    @Test
    @DisplayName("When updating an existing game with invalid publisher, it should throw PublisherNotFoundException")
    void givenExistingGameAndInvalidPublisher_whenUpdateGameIsCalled_thenThrowPublisherNotFoundException() {
        GameDTO invalidPublisherDTO = GameDTO.builder()
                .publisherId("invalidPublisher")
                .name("Halo")
                .build();

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(publisherManagerService.getAllPublishers())
                .thenReturn(List.of(new PublisherDTO("microsoft", "Microsoft Company")));

        assertThrows(PublisherNotFoundException.class, () -> gameService.updateGame(gameId, invalidPublisherDTO));
        verify(gameRepository, never()).save(any());
    }

    @Test
    @DisplayName("When deleting an existing game, it should delete successfully")
    void givenExistingGameId_whenDeleteGameIsCalled_thenDeleteSuccessfully() {
        when(gameRepository.existsById(gameId)).thenReturn(true);
        assertDoesNotThrow(() -> gameService.deleteGame(gameId));
        verify(gameRepository, times(1)).deleteById(gameId);
    }

    @Test
    @DisplayName("When deleting a non-existing game, it should throw GameNotFoundException")
    void givenNonExistingGameId_whenDeleteGameIsCalled_thenThrowGameNotFoundException() {
        when(gameRepository.existsById(gameId)).thenReturn(false);
        assertThrows(GameNotFoundException.class, () -> gameService.deleteGame(gameId));
        verify(gameRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("When a valid publisher ID is checked for validity, it should return true")
    void givenValidPublisherId_whenIsPublisherValidIsCalled_thenReturnTrue() {
        when(publisherManagerService.getAllPublishers())
                .thenReturn(Collections.singletonList(
                        new PublisherDTO(gameDTO.getPublisherId(), "Valid Publisher")));
        boolean result = gameService.isPublisherValid(gameDTO.getPublisherId());
        assertTrue(result);
        verify(publisherManagerService, times(1)).getAllPublishers();
    }

    @Test
    @DisplayName("When an invalid publisher ID is checked for validity, it should return false")
    void givenInvalidPublisherId_whenIsPublisherValidIsCalled_thenReturnFalse() {
        when(publisherManagerService.getAllPublishers())
                .thenReturn(Collections.singletonList(
                        new PublisherDTO(gameDTO.getPublisherId(), "Valid Publisher")));
        boolean result = gameService.isPublisherValid("invalidPublisherId");
        assertFalse(result);
        verify(publisherManagerService, times(1)).getAllPublishers();
    }
}
