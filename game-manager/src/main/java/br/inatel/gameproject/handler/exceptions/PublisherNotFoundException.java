package br.inatel.gameproject.handler.exceptions;

public class PublisherNotFoundException extends RuntimeException {
    public PublisherNotFoundException(String publisherId) {
        super("PublisherId was not found.");
    }
}