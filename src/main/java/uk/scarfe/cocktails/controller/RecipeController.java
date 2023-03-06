package uk.scarfe.cocktails.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uk.scarfe.cocktails.exception.RecipeNotFoundException;
import uk.scarfe.cocktails.model.Recipe;
import uk.scarfe.cocktails.repository.RecipeRepository;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeRepository repository;

    @GetMapping("/")
    List<Recipe> all() {
        return repository.findAll();
    }

    @PostMapping("/")
    Recipe create(@RequestBody Recipe newRecipe) {
        return repository.save(newRecipe);
    }

    @GetMapping("/{id}")
    Recipe getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    @PutMapping("/{id}")
    Recipe replace(@RequestBody Recipe newRecipe, @PathVariable Long id) {
        return repository.findById(id)
                .map(r -> {
                    r.setName(newRecipe.getName());
                    return repository.save(r);
                })
                .orElseGet(() -> {
                    newRecipe.setId(id);
                    return repository.save(newRecipe);
                });
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
