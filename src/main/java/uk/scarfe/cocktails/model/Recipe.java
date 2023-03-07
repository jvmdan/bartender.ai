package uk.scarfe.cocktails.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * A Recipe represents a known-good cocktail solution, typically sourced from an online reference.
 * This shall be used as training data so that the learning algorithm can become familiar with
 * ingredient combinations that result in a good / positive outcome.
 *
 * @author Daniel Scarfe
 */
@NoArgsConstructor
@Data
public class Recipe {

    private Long id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Instruction> instructions;

    private List<String> tags;

    public Recipe(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public Recipe addIngredient(Ingredient i) {
        this.ingredients.add(i);
        return this;
    }

    public Recipe addInstruction(Instruction i) {
        this.instructions.add(i);
        return this;
    }

    public Recipe addTag(String s) {
        this.tags.add(s);
        return this;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "'" + name + '\'' +
                '}';
    }

}
