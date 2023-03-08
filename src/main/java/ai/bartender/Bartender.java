package ai.bartender;

import ai.bartender.persistence.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.context.event.EventListener;
import org.thymeleaf.spring6.SpringTemplateEngine;

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
        final RecipeRepository defaultData = context.getBean(RecipeRepository.class);
        log.info("Initialised \"Bartender.ai\" with {} existing recipes", defaultData.count());
        log.info("Awaiting user prompt to generate new cocktail recipe...");
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
