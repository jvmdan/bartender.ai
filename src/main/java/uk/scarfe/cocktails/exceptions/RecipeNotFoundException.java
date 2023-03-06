package uk.scarfe.cocktails.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RecipeNotFoundException extends RuntimeException {

    private Long id;

}
