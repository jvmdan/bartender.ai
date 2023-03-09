package ai.bartender.model;

/**
 * A Prompt is an immutable instruction given as an input to the AI engine.
 *
 * @param recipeName  the cocktail name we want the AI to generate a recipe for.
 * @param mustContain any ingredients that the recipe must contain.
 * @author Daniel Scarfe
 */
public record Prompt(String recipeName, String mustContain) {

}
