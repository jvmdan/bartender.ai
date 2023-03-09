package ai.bartender.repository;

import ai.bartender.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    Optional<Recipe> findByName(String name);

    List<Recipe> findByCategory(String category);

}