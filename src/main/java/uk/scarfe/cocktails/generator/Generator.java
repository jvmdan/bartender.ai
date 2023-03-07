package uk.scarfe.cocktails.generator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import uk.scarfe.cocktails.model.Ingredient;
import uk.scarfe.cocktails.model.Recipe;
import uk.scarfe.cocktails.model.quantities.Liquid;
import uk.scarfe.cocktails.model.quantities.Quantity;

@RestController
@RequestMapping("/generate")
@Slf4j
public class Generator {

    @GetMapping
    Mono<Recipe> generate(@RequestParam String prompt) {
        // TODO | Take prompt from user and generate a new recipe based upon it.
        log.info("Received prompt from user: {}", prompt);
        final String[] tags = prompt.split(" ");
        final Recipe result = new Recipe(prompt);
        result.setId(1L);
        result.addIngredient(new Ingredient("Apple Juice", Quantity.of(50, Liquid.MILLILITRES)));
        return Mono.justOrEmpty(result);
    }

}
