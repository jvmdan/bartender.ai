package uk.scarfe.cocktails.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uk.scarfe.cocktails.exceptions.RecipeNotFoundException;
import uk.scarfe.cocktails.model.Recipe;
import uk.scarfe.cocktails.persistence.RecipeCache;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController implements Controller<Recipe> {

    private final RecipeCache repository;

    @GetMapping("/")
    List<Recipe> all() {
        return repository.findAll().stream().toList();
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
