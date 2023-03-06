package uk.scarfe.cocktails.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Recipe represents a known-good cocktail solution, typically sourced from an online reference.
 * This shall be used as training data so that the learning algorithm can become familiar with
 * ingredient combinations that result in a good / positive outcome.
 *
 * @author Daniel Scarfe
 */
@Entity
@Table(name = "recipes")
@NoArgsConstructor
@Data
public class Recipe {

    private @Id @GeneratedValue @Column(name = "id") Long id;
    private String name;

    public Recipe(String name) {
        this.name = name;
    }

    public Recipe addIngredient(Ingredient i, Quantity<?> q) {
        // FIXME | Does nothing until we add the logic.
        return this;
    }

}
