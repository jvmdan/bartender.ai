package ai.bartender.persistence;

import ai.bartender.model.Recipe;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.Arrays;

@Configuration
public class RepositoryConfiguration {

    @Value("classpath:data/default.json")
    private Resource defaultData;

    @Value("classpath:data/favourites.json")
    private Resource favourites;

    @Bean
    CommandLineRunner preload(RecipeRepository repository) {
        return args -> {
            // Preload the database with a number of "known-good" cocktail recipes.
            final ObjectMapper mapper = new ObjectMapper();
            try (JsonParser parser = mapper.createParser(defaultData.getFile())) {
                Arrays.stream(mapper.readValue(parser, Recipe[].class)).forEachOrdered(repository::save);
            }
            try (JsonParser parser = mapper.createParser(favourites.getFile())) {
                Arrays.stream(mapper.readValue(parser, Recipe[].class)).forEachOrdered(repository::save);
            }
        };
    }

}
