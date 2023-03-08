package ai.bartender.controller;

import ai.bartender.exceptions.InvalidPromptException;
import ai.bartender.exceptions.NotFoundException;
import ai.bartender.generator.Generator;
import ai.bartender.model.Recipe;
import ai.bartender.persistence.DataStore;
import ai.bartender.utils.PromptUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final DataStore<Recipe, String> dataStore;
    private final ApplicationContext context;

    @GetMapping("/generate")
    Mono<Recipe> generate(@RequestParam String prompt) {
        if (prompt == null || prompt.isBlank() || prompt.isEmpty() || PromptUtils.isBanned(prompt)) {
            throw new InvalidPromptException(prompt); // Reject invalid and/or banned prompts
        }
        final String request = PromptUtils.normalise(prompt);
        final Optional<Recipe> existing = dataStore.findById(PromptUtils.uuid(request));
        if (existing.isPresent()) {
            final Recipe r = existing.get();
            log.info("Retrieved Recipe: \"{}\" [ingredients={}, directions={}]",
                    request, r.getIngredients().size(), r.getDirections().size());
            return Mono.just(r);
        } else {
            final Generator generator = context.getBean(Generator.class);
            final Recipe r = generator.create(request);
            dataStore.save(r);
            log.info("Retrieved Recipe: \"{}\" [ingredients={}, directions={}]",
                    request, r.getIngredients().size(), r.getDirections().size());
            return Mono.just(r);
        }
    }

    @GetMapping("/recipes")
    Flux<Recipe> all() {
        return Flux.fromIterable(dataStore.findAll());
    }

    @GetMapping("/recipes/{id}")
    Mono<Recipe> getById(@PathVariable String id) {
        final Optional<Recipe> foundRecipe = dataStore.findById(id);
        return foundRecipe.map(Mono::just).orElseThrow(() -> new NotFoundException(id));
    }

}
