package ai.bartender.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ai.bartender.model.Recipe;
import ai.bartender.persistence.DataStore;

@Component
@Scope("prototype") // Create a new instance for every request
@RequiredArgsConstructor
@Slf4j
public class Generator {

    private final DataStore<Recipe, String> dataStore;

    public Recipe create(String uuid, String request) {
        final Recipe result = new Recipe(request);
        result.setId(uuid);
        dataStore.save(result);
        log.info("Created \"{}\" recipe with {} ingredients & {} instructions",
                request, result.getIngredients().size(), result.getInstructions().size());
        return result;
    }

}
