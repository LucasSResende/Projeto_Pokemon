package br.inatel.gameproject.controller;

import br.inatel.gameproject.model.dto.GameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebClient
@ActiveProfiles("test")
@DisplayName("GameController Tests")
class GameControllerTest {

    GameDTO gameDTO;
    private WebTestClient webTestClient;
    @Value("${game.manager.port}")
    private int port;

    @Value("${game.manager.host}")
    private String host;


    @BeforeEach
    public void setup() {
        String url = String.format("http://%s:%d", host, port);
        this.webTestClient = WebTestClient.bindToServer().baseUrl(url).build();
        gameDTO = GameDTO.builder()
                .publisherId("microsoft")
                .name("Halo Infinite")
                .timePlayed(Map.of(LocalDate.of(2023, 12, 1), 20,
                        LocalDate.of(2023, 1, 1), 18,
                        LocalDate.of(2024, 2, 3), 19,
                        LocalDate.of(2024, 2, 9), 24))
                .build();
    }

    @Test
    @DisplayName("Return the created game")
    void givenGame_whenPostGameIsCalled_thenReturnCreatedGame() {
        List<GameDTO> response = webTestClient.post()
                .uri("/game")
                .body(BodyInserters.fromValue(gameDTO))
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(GameDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.get(0).getId()).isNotNull();
        assertThat(response.get(0).getPublisherId()).isEqualTo(gameDTO.getPublisherId());
        assertThat(response.get(0).getName()).isEqualTo(gameDTO.getName());
        assertThat(response.get(0).getTimePlayed()).isEqualTo(gameDTO.getTimePlayed());
    }

    @Test
    @DisplayName("Return a list of games")
    void givenPublisherId_whenGetGamesIsCalled_thenReturnListOfGames() {
        List<GameDTO> response = webTestClient.get()
                .uri("/game?publisherId=microsoft")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GameDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.get(0).getId()).isNotNull();
        assertThat(response.get(0).getName()).isEqualTo(gameDTO.getName());
        assertThat(response.get(0).getPublisherId()).isEqualTo(gameDTO.getPublisherId());
        assertThat(response.get(0).getTimePlayed()).isEqualTo(gameDTO.getTimePlayed());
    }

}