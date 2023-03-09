package ai.bartender.controller;

import ai.bartender.exceptions.BannedPromptException;
import ai.bartender.exceptions.EmptyPromptException;
import ai.bartender.exceptions.NotFoundException;
import ai.bartender.exceptions.NotPermittedException;
import ai.bartender.generator.Generator;
import ai.bartender.model.Recipe;
import ai.bartender.persistence.RecipeRepository;
import ai.bartender.utils.PromptUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.MethodNotAllowedException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ViewController {

    private final RecipeRepository repo;
    private final ApplicationContext context;

    @RequestMapping({"/", "/index"})
    String index() {
        return "create";
    }

    @GetMapping("/random")
    String random(Model model) {
        final Random random = new Random();
        final List<Recipe> generated = repo.findByCategory("generated");
        final Recipe selected = generated.get((int) random.nextLong(generated.size()));
        model.addAttribute("recipe", selected);
        return "create";
    }

    @GetMapping("/result")
    String result(@RequestParam String prompt, Model model) {
        // Reject invalid and/or banned prompts
        if (prompt == null || prompt.isBlank() || prompt.isEmpty()) {
            throw new EmptyPromptException();
        } else if (PromptUtils.isBanned(prompt)) {
            throw new BannedPromptException(prompt);
        }
        final String request = PromptUtils.normalise(prompt);
        final Optional<Recipe> existing = repo.findByName(request);
        if (existing.isPresent()) {
            final Recipe r = existing.get();
            log.info("Retrieved Recipe: \"{}\" [ingredients={}, directions={}]",
                    request, r.getIngredients().size(), r.getDirections().size());
            model.addAttribute("recipe", r);
        } else {
            final Generator generator = context.getBean(Generator.class);
            final Recipe r = generator.create(request);
            model.addAttribute("recipe", r);
        }
        return "create";
    }

    @GetMapping({"/recipes", "/recipes/"})
    @ResponseBody
    Mono<Void> all() {
        throw new NotPermittedException("/recipes");
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
