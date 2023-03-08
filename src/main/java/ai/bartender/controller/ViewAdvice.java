package ai.bartender.controller;

import ai.bartender.exceptions.BannedPromptException;
import ai.bartender.exceptions.NotFoundException;
import ai.bartender.exceptions.RecipeCreationException;
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
class ViewAdvice {

    @ResponseBody
    @ExceptionHandler(BannedPromptException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Mono<String> bannedPrompt(BannedPromptException ex) {
        return Mono.just(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(RecipeCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Mono<String> creationFailure(RecipeCreationException ex) {
        return Mono.just(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Mono<String> notFound(NotFoundException ex) {
        return Mono.just(ex.getMessage());
    }

}