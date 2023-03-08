package ai.bartender.generator;

import ai.bartender.generator.ai.Ai;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ai.bartender.model.Recipe;
import ai.bartender.persistence.DataStore;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype") // Create a new instance for every request
@RequiredArgsConstructor
@Slf4j
public class Generator {

    private final Ai aiService;
    private final DataStore<Recipe, String> dataStore;

    public Recipe create(String request) {
        final String result = aiService.request("Create a recipe for a cocktail named \"" + request + "\"");

        final List<String> ingredients = new ArrayList<>();
        final List<String> directions = new ArrayList<>();
        result.lines().forEachOrdered(l -> {
            if (l.startsWith("- ")) {
                ingredients.add(l.substring(2));
            } else if (l.length() > 0 && Character.isDigit(l.charAt(0))) {
                directions.add(l);
            }
        });

        final Recipe recipe = new Recipe(request, aiService.getName());
        recipe.addIngredients(ingredients);
        recipe.addDirections(directions);
        log.info("New Recipe: \"{}\" [ingredients={}, directions={}]", request, ingredients.size(), directions.size());
        return recipe;
    }

}
