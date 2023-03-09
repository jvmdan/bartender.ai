package ai.bartender.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private String permalink;
    private String category;
    private List<String> ingredients;
    private List<String> directions;


    public Recipe(String name, String category) {
        this.name = normalise(name);
        this.permalink = permalink(name);
        this.category = category;
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


    /**
     * Strip any bad characters from the prompt string, preventing malicious code execution.
     *
     * @param prompt the original prompt string we wish to normalise.
     * @return a String with all bad characters removed.
     */
    public static String normalise(String prompt) {
        return prompt.replace(" and ", " & ")
                .replace(" + ", " & ")
                .replaceAll("[^A-Za-z0-9()\\[\\] '&]", "");
    }

    public static String permalink(String prompt) {
        final String normalised = normalise(prompt);
        final String alphanumeric = normalised.replace(" ", "-")
                .replace("&", "and")
                .replace("'", "");
        return alphanumeric.toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "'" + name + '\'' +
                '}';
    }

}
