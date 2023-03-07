package uk.scarfe.cocktails;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import uk.scarfe.cocktails.model.Ingredient;
import uk.scarfe.cocktails.model.Recipe;
import uk.scarfe.cocktails.model.quantities.Count;
import uk.scarfe.cocktails.model.quantities.Liquid;
import uk.scarfe.cocktails.model.quantities.Quantity;
import uk.scarfe.cocktails.persistence.InMemoryStore;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {JacksonAutoConfiguration.class})
@Slf4j
public class Bartender implements ApplicationContextAware {

    ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(Bartender.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        final InMemoryStore trainingData = context.getBean(InMemoryStore.class);
        log.info("Initialised \"Bartender.ai\" with training data containing {} recipes", trainingData.count());
        log.info("Awaiting user prompt to generate new cocktail recipe...");
    }

    @Bean
    @Profile("in-memory")
    CommandLineRunner initialise(InMemoryStore repository) {
        // Preload the database with a number of "known-good" cocktail recipes.
        return args -> {

            final Recipe mojito = new Recipe("Mojito")
                    .addIngredient(new Ingredient("Lime", Quantity.of(1)))
                    .addIngredient(new Ingredient("Mint", Quantity.of(1, Count.HANDFUL)))
                    .addIngredient(new Ingredient("Scotch Whiskey", Quantity.of(60, Liquid.MILLILITRES)))
                    .addIngredient(new Ingredient("Granulated Sugar", Quantity.of(1, Count.TEASPOON)))
                    .addIngredient(new Ingredient("Soda Water", Quantity.of(0, Count.TO_TASTE)));
            log.debug("Preloaded " + repository.save(mojito));

            final Recipe oldFashioned = new Recipe("Old Fashioned");
            log.debug("Preloaded " + repository.save(oldFashioned));

            final Recipe bloodyMary = new Recipe("Bloody Mary");
            log.debug("Preloaded " + repository.save(bloodyMary));

            final Recipe tequilaSunrise = new Recipe("Tequila Sunrise");
            log.debug("Preloaded " + repository.save(tequilaSunrise));

            final Recipe longIsland = new Recipe("Long Island Iced Tea");
            log.debug("Preloaded " + repository.save(longIsland));
        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
