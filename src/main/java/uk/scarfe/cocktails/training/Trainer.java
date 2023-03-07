package uk.scarfe.cocktails.training;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.scarfe.cocktails.exceptions.RecipeNotFoundException;
import uk.scarfe.cocktails.model.Recipe;
import uk.scarfe.cocktails.persistence.InMemoryStore;

import java.util.Optional;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class Trainer {

    private final InMemoryStore repository;

    @GetMapping("/")
    Flux<Recipe> all() {
        return Flux.fromIterable(repository.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Recipe> create(@RequestBody Recipe newRecipe) {
        return Mono.just(repository.save(newRecipe));
    }

    @GetMapping("/{id}")
    Mono<Recipe> getById(@PathVariable Long id) {
        final Optional<Recipe> foundRecipe = repository.findById(id);
        return foundRecipe.map(Mono::just).orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
