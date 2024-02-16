package br.inatel.gameproject.mapper;

import br.inatel.gameproject.model.dto.GameDTO;
import br.inatel.gameproject.model.entity.Game;
import br.inatel.gameproject.model.entity.TimePlayed;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameMapper {
    public static List<GameDTO> gameListToGameDTOList(List<Game> gameList) {
        return gameList.stream().map(GameMapper::gameToGameDTO).collect(Collectors.toList());
    }

    public static Game gameDTOToGame(GameDTO gameDTO) {
        Game game = Game.builder()
                .publisherId(gameDTO.getPublisherId())
                .name(gameDTO.getName())
                .timePlayed(new ArrayList<>())
                .build();

        if (gameDTO.getTimePlayed() != null && !gameDTO.getTimePlayed().isEmpty()) {
            gameDTO.getTimePlayed().forEach((date, hour) ->
                    game.addTimePlayed(TimePlayed.builder()
                            .timeDate(date)
                            .timeHour(hour)
                            .build())
            );
        }
        return game;
    }

    public static GameDTO gameToGameDTO(Game game) {
        GameDTO gameDTO = GameDTO.builder()
                .id(game.getId())
                .name(game.getName())
                .publisherId(game.getPublisherId())
                .build();

        if (game.getTimePlayed() != null && !game.getTimePlayed().isEmpty()) {
            gameDTO.setTimePlayed(
                    game.getTimePlayed()
                            .stream()
                            .collect(Collectors.toMap(TimePlayed::getTimeDate, TimePlayed::getTimeHour)));
        }
        return gameDTO;


    }

}