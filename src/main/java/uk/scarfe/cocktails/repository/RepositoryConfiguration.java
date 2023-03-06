package uk.scarfe.cocktails.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.scarfe.cocktails.model.Ingredient;
import uk.scarfe.cocktails.model.Quantity;
import uk.scarfe.cocktails.model.Recipe;
import uk.scarfe.cocktails.model.units.Liquid;
import uk.scarfe.cocktails.model.units.Mass;

@Configuration
@Profile("h2")
@Slf4j
public class RepositoryConfiguration {

    @Bean
    @Profile("h2")
    CommandLineRunner initialise(RecipeRepository repository) {
        return args -> {

            final Recipe mojito = new Recipe("Mojito")
                    .addIngredient(new Ingredient("Lime Juice"), Quantity.of(10, Liquid.MILLILITRES))
                    .addIngredient(new Ingredient("Mint"), Quantity.of(10, Mass.GRAMMES))
                    .addIngredient(new Ingredient("Rum"), Quantity.of(35, Liquid.MILLILITRES))
                    .addIngredient(new Ingredient("Sugar"), Quantity.of(5, Mass.GRAMMES))
                    .addIngredient(new Ingredient("Soda"), Quantity.of(150, Liquid.MILLILITRES));
            log.info("Preloading " + repository.save(mojito));

            log.info("Preloading " + repository.save(new Recipe("Old Fashioned")));
            log.info("Preloading " + repository.save(new Recipe("Bloody Mary")));
            log.info("Preloading " + repository.save(new Recipe("Tequila Sunrise")));
            log.info("Preloading " + repository.save(new Recipe("Long Island Iced Tea")));
        };
    }

}
