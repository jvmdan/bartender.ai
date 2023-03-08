package ai.bartender.model;

import ai.bartender.utils.PromptUtils;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
@Entity
@Data
public class Recipe implements Serializable {

    @GeneratedValue
    @Id
    @Column(name = "id")
    private Long id;
    private String name;
    private String source;
    private List<String> ingredients;
    private List<String> directions;


    public Recipe(String name, String source) {
        this.name = PromptUtils.normalise(name);
        this.source = source;
        this.ingredients = new ArrayList<>();
        this.directions = new ArrayList<>();
    }

    public Recipe addIngredients(List<String> i) {
        this.ingredients.addAll(i);
        return this;
    }

    public Recipe addDirections(List<String> d) {
        this.directions.addAll(d);
        return this;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "'" + name + '\'' +
                '}';
    }

}
