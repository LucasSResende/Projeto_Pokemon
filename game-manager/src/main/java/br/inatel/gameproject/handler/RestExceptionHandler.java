package br.inatel.gameproject.handler;

import br.inatel.gameproject.handler.exceptions.PublisherManagerConnectionException;
import br.inatel.gameproject.handler.exceptions.PublisherNotFoundException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ProblemDetail error = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        error.setTitle("Invalid Fields");
        error.setType(URI.create("MethodArgumentNotValidException"));
        error.setStatus(HttpStatus.BAD_REQUEST);
        return error;
    }

    @ExceptionHandler(PublisherNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handlerPublisherNotFoundException(PublisherNotFoundException e) {
        ProblemDetail error = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        error.setTitle("PublisherId was not found.");
        error.setType(URI.create("PublisherNotFoundException"));
        error.setStatus(HttpStatus.BAD_REQUEST);
        return error;
    }

    @ExceptionHandler(JDBCConnectionException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ProblemDetail handlerWithJDBCConnectionException(JDBCConnectionException e) {
        ProblemDetail error = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        error.setTitle("Database Connection Failed");
        error.setType(URI.create("JDBCConnectionException"));
        error.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
        return error;
    }

    @ExceptionHandler(PublisherManagerConnectionException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ProblemDetail handlerWithPublisherConnectionException(PublisherManagerConnectionException e) {
        ProblemDetail error = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        error.setTitle("Database Connection Failed");
        error.setType(URI.create("JDBCConnectionException"));
        error.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
        return error;
    }
}