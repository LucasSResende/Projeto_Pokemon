package br.inatel.gameproject.handler.exceptions;

public class PublisherNotFoundException extends RuntimeException {
    public PublisherNotFoundException(String publisherId) {// [Adauto] - The publisherId param is not being used
        super("PublisherId was not found.");
    }
}