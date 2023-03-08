package ai.bartender.controller;

import ai.bartender.exceptions.BannedPromptException;
import ai.bartender.exceptions.NotFoundException;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @PostMapping("/random")
    String random(Model model) {
        final Random random = new Random();
        final Recipe recipe = repo.findAll().get((int) random.nextLong(repo.count()));
        model.addAttribute("recipe", recipe);
        return "create";
    }

    @GetMapping("/generate")
    String generate(@RequestParam String prompt, Model model) {
        if (prompt == null || prompt.isBlank() || prompt.isEmpty()) {
            return "create";
        } else if (PromptUtils.isBanned(prompt)) {
            throw new BannedPromptException(prompt); // Reject invalid and/or banned prompts
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
    Flux<Recipe> all() {
        return Flux.fromIterable(repo.findAll());
    }

    @GetMapping({"/recipes/{id}", "/recipes/{id}/"})
    @ResponseBody
    Mono<Recipe> getById(@PathVariable Long id) {
        final Optional<Recipe> foundRecipe = repo.findById(id);
        return foundRecipe.map(Mono::just).orElseThrow(() -> new NotFoundException(id.toString()));
    }

}
