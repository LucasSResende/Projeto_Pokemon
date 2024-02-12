package br.inatel.gameproject.controller;

import br.inatel.gameproject.model.dto.GameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameController gameService;

    @Autowired
    public GameController(GameController gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Page<GameDTO> getAllGames(@RequestParam(required = false) String publisherId, Pageable pageable) {
        if (publisherId != null) {
            return gameService.getAllGames(publisherId, pageable);
        }
        return gameService.getAllGames(pageable);
    }
}