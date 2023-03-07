package uk.scarfe.cocktails;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@Slf4j
public class Cocktails {

	public static void main(String[] args) {
		SpringApplication.run(Cocktails.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void ready() {
		log.info("Awaiting user prompt to generate new cocktail recipe...");
	}

}
