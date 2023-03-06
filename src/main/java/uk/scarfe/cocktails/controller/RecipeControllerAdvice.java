package uk.scarfe.cocktails.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.scarfe.cocktails.exceptions.RecipeNotFoundException;

@ControllerAdvice
class RecipeControllerAdvice {

    @ResponseBody
    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String recipeNotFound(RecipeNotFoundException ex) {
        return ex.getMessage();
    }

}