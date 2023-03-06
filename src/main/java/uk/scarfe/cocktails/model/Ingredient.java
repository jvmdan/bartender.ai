package uk.scarfe.cocktails.model;

/**
 * An ingredient is an immutable entity that represents a single object to be used in a cocktail recipe.
 * We deliberately don't tie the ingredient to its quantity, because we want to be able to compare
 * recipes which use identical ingredients in varying quantities. This allows us to gain an understanding
 * of what ingredients work well together and in what quantities.
 *
 * @param name a unique name for the ingredient.
 */
public record Ingredient(String name) {

}
