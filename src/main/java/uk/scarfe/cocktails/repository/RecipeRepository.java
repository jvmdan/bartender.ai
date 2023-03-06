package uk.scarfe.cocktails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.scarfe.cocktails.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
