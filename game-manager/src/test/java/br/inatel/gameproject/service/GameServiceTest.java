package br.inatel.gameproject.service;

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

    @BeforeEach
    public void setup() {
        gameDTO = GameDTO.builder()
                .publisherId("microsoft")
                .name("Halo Infinite")
                .timePlayed(Map.of(LocalDate.of(2023, 12, 1), 20,
                        LocalDate.of(2023, 1, 1), 18,
                        LocalDate.of(2024, 2, 3), 19,
                        LocalDate.of(2024, 2, 9), 24))
                .build();
        game = Game.builder().id(UUID.randomUUID())
                .publisherId("microsoft")
                .name("Halo Infinite")
                .timePlayed(Arrays.asList(
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
                ))
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
    @DisplayName("When a valid publisher ID is checked for validity, it should return true")
    void givenValidPublisherId_whenIsPublisherValidIsCalled_thenReturnTrue() {
        when(publisherManagerService.getAllPublishers())
                .thenReturn(Collections.singletonList(
                        new PublisherDTO(gameDTO
                                .getPublisherId(), "Valid Publisher")));
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