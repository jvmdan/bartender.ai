package uk.scarfe.cocktails.exceptions;

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
class ExceptionService {

    @ResponseBody
    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Mono<String> recipeNotFound(RecipeNotFoundException ex) {
        return Mono.just(ex.toString());
    }

}