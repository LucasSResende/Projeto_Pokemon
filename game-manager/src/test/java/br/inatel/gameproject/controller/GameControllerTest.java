package br.inatel.gameproject.controller;

import br.inatel.gameproject.handler.exceptions.GameNotFoundException;
import br.inatel.gameproject.model.dto.GameDTO;
import br.inatel.gameproject.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
@DisplayName("GameController Unit Tests")
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    private GameDTO gameDTO;
    private UUID gameId;

    @BeforeEach
    public void setup() {
        gameId = UUID.randomUUID();
        gameDTO = GameDTO.builder()
                .id(gameId)
                .publisherId("microsoft")
                .name("Halo Infinite")
                .timePlayed(Map.of(
                        LocalDate.of(2023, 12, 1), 20,
                        LocalDate.of(2023, 1, 1), 18,
                        LocalDate.of(2024, 2, 3), 19,
                        LocalDate.of(2024, 2, 9), 24))
                .build();
    }

    @Test
    @DisplayName("Return the created game")
    void givenGame_whenCreateGameIsCalled_thenReturnCreatedGame() throws Exception {
        when(gameService.createGame(any(GameDTO.class))).thenReturn(gameDTO);

        mockMvc.perform(post("/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "publisherId": "microsoft",
                                  "name": "Halo Infinite",
                                  "timePlayed": {
                                    "2023-12-01": 20,
                                    "2023-01-01": 18,
                                    "2024-02-03": 19,
                                    "2024-02-09": 24
                                  }
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Halo Infinite"))
                .andExpect(jsonPath("$.publisherId").value("microsoft"));

        ArgumentCaptor<GameDTO> gameCaptor = ArgumentCaptor.forClass(GameDTO.class);
        verify(gameService).createGame(gameCaptor.capture());

        GameDTO capturedGame = gameCaptor.getValue();
        assertThat(capturedGame.getName()).isEqualTo("Halo Infinite");
        assertThat(capturedGame.getPublisherId()).isEqualTo("microsoft");
        assertThat(capturedGame.getTimePlayed()).isEqualTo(gameDTO.getTimePlayed());
    }

    @Test
    @DisplayName("Return a list of games when publisherId is provided")
    void givenPublisherId_whenGetGamesIsCalled_thenReturnListOfGames() throws Exception {
        when(gameService.getAllGames("microsoft")).thenReturn(List.of(gameDTO));

        mockMvc.perform(get("/game")
                        .param("publisherId", "microsoft")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Halo Infinite"))
                .andExpect(jsonPath("$[0].publisherId").value("microsoft"));

        verify(gameService).getAllGames("microsoft");
    }

    @Test
    @DisplayName("Return a list of all games when no publisherId is provided")
    void whenGetGamesIsCalled_thenReturnListOfAllGames() throws Exception {
        when(gameService.getAllGames(null)).thenReturn(List.of(gameDTO));

        mockMvc.perform(get("/game")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Halo Infinite"))
                .andExpect(jsonPath("$[0].publisherId").value("microsoft"));

        verify(gameService).getAllGames(null);
    }

    @Test
    @DisplayName("Handle empty list when no games are found")
    void givenNoGames_whenGetGamesIsCalled_thenReturnEmptyList() throws Exception {
        when(gameService.getAllGames(null)).thenReturn(List.of());

        mockMvc.perform(get("/game")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(gameService).getAllGames(null);
    }

    @Test
    @DisplayName("Return game when valid game ID is provided")
    void givenExistingGameId_whenGetGameByIdIsCalled_thenReturnGame() throws Exception {
        when(gameService.getGameById(gameId)).thenReturn(gameDTO);

        mockMvc.perform(get("/game/{id}", gameId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Halo Infinite"))
                .andExpect(jsonPath("$.publisherId").value("microsoft"));

        verify(gameService).getGameById(gameId);
    }

    @Test
    @DisplayName("Return 404 when requesting non-existing game ID")
    void givenNonExistingGameId_whenGetGameByIdIsCalled_thenReturnNotFound() throws Exception {
        when(gameService.getGameById(gameId)).thenThrow(new GameNotFoundException(gameId));

        mockMvc.perform(get("/game/{id}", gameId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Game was not found."));

        verify(gameService).getGameById(gameId);
    }

    @Test
    @DisplayName("Return updated game when valid update is provided")
    void givenValidUpdate_whenUpdateGameIsCalled_thenReturnUpdatedGame() throws Exception {
        GameDTO updatedGame = GameDTO.builder()
                .id(gameId)
                .publisherId("microsoft")
                .name("Halo Infinite Updated")
                .build();

        when(gameService.updateGame(eq(gameId), any(GameDTO.class))).thenReturn(updatedGame);

        mockMvc.perform(put("/game/{id}", gameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "publisherId": "microsoft",
                                  "name": "Halo Infinite Updated"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Halo Infinite Updated"))
                .andExpect(jsonPath("$.publisherId").value("microsoft"));

        verify(gameService).updateGame(eq(gameId), any(GameDTO.class));
    }

    @Test
    @DisplayName("Return 204 No Content when game is successfully deleted")
    void givenValidGameId_whenDeleteGameIsCalled_thenReturnNoContent() throws Exception {
        doNothing().when(gameService).deleteGame(gameId);

        mockMvc.perform(delete("/game/{id}", gameId))
                .andExpect(status().isNoContent());

        verify(gameService).deleteGame(gameId);
    }

    @Test
    @DisplayName("Return 404 when deleting a non-existing game")
    void givenNonExistingGameId_whenDeleteGameIsCalled_thenReturnNotFound() throws Exception {
        doThrow(new GameNotFoundException(gameId)).when(gameService).deleteGame(gameId);

        mockMvc.perform(delete("/game/{id}", gameId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Game was not found."));

        verify(gameService).deleteGame(gameId);
    }
}
