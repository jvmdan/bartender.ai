package ai.bartender.view;

import ai.bartender.engine.Engine;
import ai.bartender.exceptions.EmptyPromptException;
import ai.bartender.exceptions.NotPermittedException;
import ai.bartender.exceptions.RecipeCreationException;
import ai.bartender.model.Prompt;
import ai.bartender.model.Recipe;
import ai.bartender.model.Response;
import ai.bartender.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ViewController {

    private final RecipeRepository repo;
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
        final List<Recipe> generated = repo.findByCategory("generated");
        final Recipe selected = generated.get((int) random.nextLong(generated.size()));
        final String reference = URLEncoder.encode(selected.getName(), StandardCharsets.UTF_8);
        return "redirect:/result?prompt=" + reference + "&fetch=true";
    }

    @GetMapping("/result")
    String result(@RequestParam String prompt, @RequestParam(required = false) boolean fetch, Model model) {
        // Reject invalid and/or banned prompts
        if (prompt == null || prompt.isBlank()) throw new EmptyPromptException();
        final String name = Recipe.normalise(prompt);
        final Prompt p = new Prompt(name);
        if (engine.moderate(p)) throw new RecipeCreationException(p);

        final Optional<Recipe> existing = repo.findByName(name);
        if (fetch && existing.isPresent()) {
            // If there's already a recipe available, we can just fetch it.
            model.addAttribute("recipe", existing.get());
        } else {
            // Create a new recipe & append that to the model instead.
            final Response<Recipe> r = engine.respond(p);
            if (overwrite && existing.isPresent()) {
                repo.delete(existing.get());
            } else if (existing.isEmpty()) {
                repo.save(r.result());
            }
            model.addAttribute("recipe", r.result());
        }
        return "view";
    }


    @GetMapping({"/recipes", "/recipes/"})
    @ResponseBody
    Mono<Void> getAll() {
        throw new NotPermittedException("/recipes/");
    }

    @GetMapping({"/recipes/{category}", "/recipes/{category}/"})
    @ResponseBody
    Flux<Recipe> getCategory(@PathVariable String category) {
        return Flux.fromIterable(repo.findByCategory(category));
    }

    @GetMapping({"/recipes/{category}/{permalink}", "/recipes/{category}/{permalink}/"})
    @ResponseBody
    Mono<Recipe> getByPermalink(@PathVariable String category, @PathVariable String permalink) {
        final List<Recipe> recipes = repo.findByCategory(category);
        final Optional<Recipe> match = recipes.stream().filter(r -> r.getPermalink().equals(permalink)).findAny();
        return Mono.justOrEmpty(match);
    }

}
