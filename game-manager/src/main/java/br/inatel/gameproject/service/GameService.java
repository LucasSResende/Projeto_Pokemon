package br.inatel.gameproject.service;

import br.inatel.gameproject.handler.exceptions.PublisherNotFoundException;
import br.inatel.gameproject.mapper.GameMapper;
import br.inatel.gameproject.model.dto.GameDTO;
import br.inatel.gameproject.model.dto.PublisherDTO;
import br.inatel.gameproject.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PublisherManagerService publisherManagerService;

    @Autowired
    public GameService(GameRepository gameRepository, PublisherManagerService publisherManagerService) {
        this.gameRepository = gameRepository;
        this.publisherManagerService = publisherManagerService;
    }

    public GameDTO postGame(GameDTO gameDTO) {
        if (isPublisherValid(gameDTO.getPublisherId())) {
            return GameMapper.gameToGameDTO(gameRepository.save(GameMapper.gameDTOToGame(gameDTO)));
        }
        throw new PublisherNotFoundException(gameDTO.getPublisherId());
    }

    public List<GameDTO> getGames(String publisherId) {
        return (publisherId != null)
                ? GameMapper.gameListToGameDTOList(gameRepository.findByPublisherId(publisherId))
                : GameMapper.gameListToGameDTOList(gameRepository.findAll());
    }

    public boolean isPublisherValid(String publisherId) {
        List<PublisherDTO> publisherDTOList = publisherManagerService.getAllPublishers();
        return publisherDTOList.stream().anyMatch(publisherDTO -> publisherDTO.getId().equals(publisherId));
    }
}