package uk.scarfe.cocktails.model;

import uk.scarfe.cocktails.model.quantities.Quantity;

/**
 * An ingredient is an immutable entity that represents a single object to be used in a cocktail recipe.
 * We tie the ingredient to its quantity, because we want to be able to compare recipes which use similar
 * ingredients in varying quantities. This allows us to gain an understanding of what ingredients work
 * well together and in what quantities.
 *
 * @param name     a unique name for the ingredient.
 * @param quantity the amount of ingredient required for our recipe.
 */
public record Ingredient(String name, Quantity<?> quantity) {

}
