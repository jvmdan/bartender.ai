package ai.bartender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {JacksonAutoConfiguration.class})
@Slf4j
public class Bartender {

    public static void main(String[] args) {
        SpringApplication.run(Bartender.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        log.info("Awaiting user prompt to generate new cocktail recipe...");
    }

}
