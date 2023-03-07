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
        if (prompt == null || prompt.isBlank() || prompt.isEmpty()) {
            throw new InvalidPromptException(prompt); // Fast fail!
        }
        final String request = PromptUtils.normalise(prompt);
        final String uuid = PromptUtils.uuid(request);
        final Optional<Recipe> existing = dataStore.findById(uuid);
        if (existing.isPresent()) {
            log.info("Retrieved existing recipe for \"{}\"", request);
            return Mono.just(existing.get());
        } else {
            log.info("Creating new recipe for \"{}\"", request);
            final Generator curator = context.getBean(Generator.class);
            return Mono.just(curator.create(uuid, request));
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
