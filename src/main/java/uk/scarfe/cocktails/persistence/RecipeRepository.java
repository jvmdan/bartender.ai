package uk.scarfe.cocktails.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.scarfe.cocktails.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
