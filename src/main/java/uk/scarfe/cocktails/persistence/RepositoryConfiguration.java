package uk.scarfe.cocktails.persistence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.scarfe.cocktails.model.Ingredient;
import uk.scarfe.cocktails.model.quantities.Quantity;
import uk.scarfe.cocktails.model.Recipe;
import uk.scarfe.cocktails.model.quantities.Count;
import uk.scarfe.cocktails.model.quantities.Liquid;

@Configuration
@Profile("h2")
@Slf4j
public class RepositoryConfiguration {

    @Bean
    @Profile("h2")
    CommandLineRunner initialise(RecipeRepository repository) {
        return args -> {

            // Preload the database with a number of "known-good" cocktail recipes.
            final Recipe mojito = new Recipe("Mojito")
                    .addIngredient(new Ingredient("Lime"), Quantity.of(1))
                    .addIngredient(new Ingredient("Mint"), Quantity.of(1, Count.HANDFUL))
                    .addIngredient(new Ingredient("White Rum"), Quantity.of(60, Liquid.MILLILITRES))
                    .addIngredient(new Ingredient("Granulated Sugar"), Quantity.of(1, Count.TEASPOON))
                    .addIngredient(new Ingredient("Soda Water"), Quantity.of(0, Count.TO_TASTE));
            log.info("Preloading " + repository.save(mojito));

            log.info("Preloading " + repository.save(new Recipe("Old Fashioned")));
            log.info("Preloading " + repository.save(new Recipe("Bloody Mary")));
            log.info("Preloading " + repository.save(new Recipe("Tequila Sunrise")));
            log.info("Preloading " + repository.save(new Recipe("Long Island Iced Tea")));
        };
    }

}
