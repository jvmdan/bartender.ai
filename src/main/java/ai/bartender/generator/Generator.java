package ai.bartender.generator;

import ai.bartender.exceptions.RecipeCreationException;
import ai.bartender.generator.ai.Ai;
import ai.bartender.model.Recipe;
import ai.bartender.persistence.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype") // Create a new instance for every request
@RequiredArgsConstructor
@Slf4j
public class Generator {

    private final Ai aiService;
    private final RecipeRepository repo;

    public Recipe create(String request) {
        final String result = aiService.request("Create a recipe for a cocktail named \"" + request + "\"");

        final List<String> ingredients = new ArrayList<>();
        final List<String> directions = new ArrayList<>();
        result.lines().forEachOrdered(l -> {
            if (l.startsWith("- ")) {
                ingredients.add(l.substring(2));
            } else if (l.length() > 0 && Character.isDigit(l.charAt(0))) {
                directions.add(l.substring(2));
            }
        });

        // The AI might reject a given request for any reason. Throw a useful error to the client.
        if (ingredients.isEmpty() || directions.isEmpty()) {
            throw new RecipeCreationException(request);
        }


        final Recipe recipe = new Recipe(request, aiService.getName());
        recipe.addIngredients(ingredients);
        recipe.addDirections(directions);
        try {
            repo.save(recipe);
        } catch (Throwable t) {
            throw new RecipeCreationException(request, t);
        }

        log.info("Created Recipe: \"{}\" [ingredients={}, directions={}]",
                request, ingredients.size(), directions.size());
        return recipe;
    }

}
