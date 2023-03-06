package uk.scarfe.cocktails.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecipeNotFoundException extends RuntimeException {

    private Long id;

}
