package ai.bartender.controller;

import ai.bartender.exceptions.InvalidPromptException;
import ai.bartender.exceptions.NotFoundException;
import ai.bartender.generator.Generator;
import ai.bartender.model.Recipe;
import ai.bartender.persistence.RecipeRepository;
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

    private final RecipeRepository repo;
    private final ApplicationContext context;

    @GetMapping("/generate")
    Mono<Recipe> generate(@RequestParam String prompt) {
        if (prompt == null || prompt.isBlank() || prompt.isEmpty() || PromptUtils.isBanned(prompt)) {
            throw new InvalidPromptException(prompt); // Reject invalid and/or banned prompts
        }
        final String request = PromptUtils.normalise(prompt);
        final Optional<Recipe> existing = repo.findByName(request);
        if (existing.isPresent()) {
            final Recipe r = existing.get();
            log.info("Retrieved Recipe: \"{}\" [ingredients={}, directions={}]",
                    request, r.getIngredients().size(), r.getDirections().size());
            return Mono.just(r);
        } else {
            final Generator generator = context.getBean(Generator.class);
            final Recipe r = generator.create(request);
            return Mono.just(r);
        }
    }

    @GetMapping({"/recipes", "/recipes/"})
    Flux<Recipe> all() {
        return Flux.fromIterable(repo.findAll());
    }

    @GetMapping({"/recipes/{id}", "/recipes/{id}/"})
    Mono<Recipe> getById(@PathVariable Long id) {
        final Optional<Recipe> foundRecipe = repo.findById(id);
        return foundRecipe.map(Mono::just).orElseThrow(() -> new NotFoundException(id.toString()));
    }

}
