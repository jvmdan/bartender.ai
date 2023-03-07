package ai.bartender.controller;

import ai.bartender.exceptions.InvalidPromptException;
import ai.bartender.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

/**
 * The ExceptionService is a ControllerAdvice implementation that catches exceptions when thrown and
 * ensures that they are transformed to a suitable HTTP response for the end-user.
 *
 * @author Daniel Scarfe
 */
@ControllerAdvice
class ErrorHandling {

    @ResponseBody
    @ExceptionHandler(InvalidPromptException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Mono<String> invalidPrompt(InvalidPromptException ex) {
        return Mono.just(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Mono<String> notFound(NotFoundException ex) {
        return Mono.just(ex.getMessage());
    }

}