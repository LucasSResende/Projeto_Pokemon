package br.inatel.gameproject.handler.exceptions;

import java.util.UUID;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(UUID id) {
        super("Game with id " + id + " was not found.");
    }
}
