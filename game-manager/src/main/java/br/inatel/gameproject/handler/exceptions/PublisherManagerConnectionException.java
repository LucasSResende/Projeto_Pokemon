package br.inatel.gameproject.handler.exceptions;

public class PublisherManagerConnectionException extends RuntimeException {
    public PublisherManagerConnectionException() {
        super("Publisher was not found.");
    }
}