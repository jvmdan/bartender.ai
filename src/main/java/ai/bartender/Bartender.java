package ai.bartender;

import ai.bartender.model.Ingredient;
import ai.bartender.model.Recipe;
import ai.bartender.model.quantities.Count;
import ai.bartender.model.quantities.Liquid;
import ai.bartender.model.quantities.Quantity;
import ai.bartender.persistence.DataStore;
import ai.bartender.persistence.InMemoryStore;
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

import java.util.ArrayList;
import java.util.List;

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
    CommandLineRunner initialise(DataStore<Recipe, String> store) {
        return args -> {
            // Preload the database with a number of "known-good" cocktail recipes.
            final List<Recipe> preload = new ArrayList<>();
            preload.add(new Recipe("Mojito")
                    .addIngredient(new Ingredient("Lime", Quantity.of(1)))
                    .addIngredient(new Ingredient("Mint", Quantity.of(1, Count.HANDFUL)))
                    .addIngredient(new Ingredient("Scotch Whiskey", Quantity.of(60, Liquid.MILLILITRES)))
                    .addIngredient(new Ingredient("Granulated Sugar", Quantity.of(1, Count.TEASPOON)))
                    .addIngredient(new Ingredient("Soda Water", Quantity.of(0, Count.TO_TASTE))));
            preload.add(new Recipe("Old Fashioned"));
            preload.add(new Recipe("Bloody Mary"));
            preload.add(new Recipe("Tequila Sunrise"));
            preload.add(new Recipe("Long Island Iced Tea"));
            preload.forEach(r -> log.debug("Loaded " + store.save(r) + " into " + store.getClass().getSimpleName()));
        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
