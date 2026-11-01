package br.inatel.gameproject.service;

import br.inatel.gameproject.handler.exceptions.GameNotFoundException;
import br.inatel.gameproject.handler.exceptions.PublisherNotFoundException;
import br.inatel.gameproject.mapper.GameMapper;
import br.inatel.gameproject.model.dto.GameDTO;
import br.inatel.gameproject.model.dto.PublisherDTO;
import br.inatel.gameproject.model.entity.Game;
import br.inatel.gameproject.model.entity.TimePlayed;
import br.inatel.gameproject.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PublisherManagerService publisherManagerService;

    @Autowired
    public GameService(GameRepository gameRepository, PublisherManagerService publisherManagerService) {
        this.gameRepository = gameRepository;
        this.publisherManagerService = publisherManagerService;
    }

    public GameDTO createGame(GameDTO gameDTO) {
        if (isPublisherValid(gameDTO.getPublisherId())) {
            return GameMapper.gameToGameDTO(gameRepository.save(GameMapper.gameDTOToGame(gameDTO)));
        }
        throw new PublisherNotFoundException(gameDTO.getPublisherId());
    }

    public List<GameDTO> getAllGames(String publisherId) {
        return (publisherId != null)
                ? GameMapper.gameListToGameDTOList(gameRepository.findByPublisherId(publisherId))
                : GameMapper.gameListToGameDTOList(gameRepository.findAll());
    }

    public GameDTO getGameById(UUID id) {
        return gameRepository.findById(id)
                .map(GameMapper::gameToGameDTO)
                .orElseThrow(() -> new GameNotFoundException(id));
    }

    public GameDTO updateGame(UUID id, GameDTO gameDTO) {
        Game existingGame = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));

        if (!isPublisherValid(gameDTO.getPublisherId())) {
            throw new PublisherNotFoundException(gameDTO.getPublisherId());
        }

        existingGame.setName(gameDTO.getName());
        existingGame.setPublisherId(gameDTO.getPublisherId());

        existingGame.getTimePlayed().clear();
        if (gameDTO.getTimePlayed() != null && !gameDTO.getTimePlayed().isEmpty()) {
            gameDTO.getTimePlayed().forEach((date, hour) ->
                    existingGame.addTimePlayed(TimePlayed.builder()
                            .timeDate(date)
                            .timeHour(hour)
                            .build())
            );
        }

        Game updatedGame = gameRepository.save(existingGame);
        return GameMapper.gameToGameDTO(updatedGame);
    }

    public void deleteGame(UUID id) {
        if (!gameRepository.existsById(id)) {
            throw new GameNotFoundException(id);
        }
        gameRepository.deleteById(id);
    }

    public boolean isPublisherValid(String publisherId) {
        List<PublisherDTO> publisherDTOList = publisherManagerService.getAllPublishers();
        return publisherDTOList.stream().anyMatch(publisherDTO -> publisherDTO.getId().equals(publisherId));
    }
}
