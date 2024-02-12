package br.inatel.gameproject.service;

import br.inatel.gameproject.mapper.GameMapper;
import br.inatel.gameproject.model.dto.GameDTO;
import br.inatel.gameproject.model.entity.Game;
import br.inatel.gameproject.model.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository;


    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameDTO postGame(GameDTO gameDTO) throws RuntimeException {
        Game game = GameMapper.gameDTOToGame(gameDTO);
        return null;
    }

    public Page<GameDTO> getGameByPublisher(String publisherId, Pageable pageable) {
        Page<Game> gamePage = gameRepository.findAll(pageable);
        return gamePage.map(GameMapper::gameToGameDTO);
    }
}