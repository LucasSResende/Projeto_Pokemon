package br.inatel.gameproject.controller;

import br.inatel.gameproject.model.dto.GameDTO;
import br.inatel.gameproject.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("game")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames(@RequestParam(required = false) String publisherId) {
        return ResponseEntity.ok(gameService.getAllGames(publisherId));
    }

    @PostMapping
    public ResponseEntity<GameDTO> createGame(@Valid @RequestBody GameDTO gameDTO) {
        return new ResponseEntity<>(gameService.createGame(gameDTO), HttpStatus.CREATED);
    }
}