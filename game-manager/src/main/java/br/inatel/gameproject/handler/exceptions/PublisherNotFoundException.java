package br.inatel.gameproject.handler.exceptions;

public class PublisherNotFoundException extends RuntimeException {
    public PublisherNotFoundException() {
        super("PublisherId was not found.");
    }
}