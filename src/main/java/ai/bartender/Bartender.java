package ai.bartender;

import ai.bartender.model.Ingredient;
import ai.bartender.model.Recipe;
import ai.bartender.model.quantities.Count;
import ai.bartender.model.quantities.Liquid;
import ai.bartender.model.quantities.Quantity;
import ai.bartender.persistence.DataStore;
import ai.bartender.persistence.InMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
        log.info("Initialised \"Bartender.ai\" with {} existing recipes", trainingData.count());
        log.info("Awaiting user prompt to generate new cocktail recipe...");
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
