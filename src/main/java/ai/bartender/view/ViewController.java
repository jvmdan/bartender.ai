package ai.bartender.view;

import ai.bartender.engine.Engine;
import ai.bartender.exceptions.EmptyPromptException;
import ai.bartender.exceptions.RecipeCreationException;
import ai.bartender.model.Prompt;
import ai.bartender.model.Recipe;
import ai.bartender.model.Response;
import ai.bartender.persistence.DataStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ViewController {

    private final DataStore<String, Recipe> repo;
    private final Engine<Recipe> engine;

    @Value("${bartender.overwrite:false}")
    public boolean overwrite;

    @RequestMapping({"/", "/index"})
    String index() {
        return "view";
    }

    @GetMapping({"/random", "/random/"})
    String random() {
        final Random random = new Random();
        final List<Recipe> generated = repo.findAll().stream().filter(r -> r.getCategory().equals("generated")).toList();
        final Recipe selected = generated.get((int) random.nextLong(generated.size()));
        final String reference = URLEncoder.encode(selected.getName(), StandardCharsets.UTF_8);
        return "redirect:/result?prompt=" + reference + "&fetch=true";
    }

    @GetMapping("/result")
    String result(@RequestParam String prompt, @RequestParam(required = false) boolean fetch, Model model) {
        // Reject invalid and/or banned prompts
        if (prompt == null || prompt.isBlank()) throw new EmptyPromptException();
        final String id = Recipe.permalink(prompt);
        final Prompt p = new Prompt(prompt);
        if (engine.moderate(p)) throw new RecipeCreationException(p);

        final Optional<Recipe> existing = repo.findById(id);
        if (fetch && existing.isPresent()) {
            // If there's already a recipe available, we can just fetch it.
            final Recipe result = existing.get();
            log.info("Fetched Recipe: \"{}\" [ingredients={}, directions={}]",
                    result.getName(), result.getIngredients().size(), result.getDirections().size());
            model.addAttribute("recipe", result);
        } else {
            // Create a new recipe & append that to the model instead.
            final Response<Recipe> response = engine.respond(p);
            final Recipe result = response.result();
            if (overwrite && existing.isPresent()) {
                repo.delete(existing.get());
            } else if (existing.isEmpty()) {
                repo.save(result);
            }
            log.info("Created Recipe: \"{}\" [ingredients={}, directions={}]",
                    result.getName(), result.getIngredients().size(), result.getDirections().size());
            model.addAttribute("recipe", result);
        }
        return "view";
    }

    @GetMapping({"/recipes/{permalink}", "/recipes/{permalink}/"})
    @ResponseBody
    Mono<Recipe> getByPermalink(@PathVariable String permalink) {
        final List<Recipe> recipes = repo.findAll().stream().filter(r -> r.getCategory().equals("generated")).toList();
        final Optional<Recipe> match = recipes.stream().filter(r -> r.getId().equals(permalink)).findAny();
        if (match.isEmpty()) throw new ResponseStatusException(NOT_FOUND, "Unable to locate " + permalink);
        return Mono.just(match.get());
    }

}
