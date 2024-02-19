package br.inatel.gameproject.handler.exceptions;

public class PublisherManagerConnectionException extends RuntimeException {
    public PublisherManagerConnectionException() {
        super("Publisher was not found.");//[Adauto] - Copy/pasted from the other exception, fix
    }
}