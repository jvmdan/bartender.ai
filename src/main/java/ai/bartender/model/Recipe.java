package ai.bartender.model;

import ai.bartender.utils.PromptUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private String id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Instruction> instructions;

    private List<String> tags;

    public Recipe(String name) {
        this.name = PromptUtils.normalise(name);
        this.id = PromptUtils.uuid(name);
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.tags = new ArrayList<>();
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
