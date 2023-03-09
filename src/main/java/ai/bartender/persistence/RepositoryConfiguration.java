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

    @Value("classpath:data/classic.json")
    private Resource classic;

    @Value("classpath:data/generated.json")
    private Resource generated;

    @Bean
    CommandLineRunner preload(RecipeRepository repository) {
        return args -> {
            // Preload the database with a number of "known-good" classic cocktail recipes.
            final ObjectMapper mapper = new ObjectMapper();
            try (JsonParser parser = mapper.createParser(classic.getFile())) {
                Arrays.stream(mapper.readValue(parser, Recipe[].class)).forEachOrdered(repository::save);
            }

            // Preload a number of "known-good" generated recipes, to further strengthen the model.
            try (JsonParser parser = mapper.createParser(generated.getFile())) {
                Arrays.stream(mapper.readValue(parser, Recipe[].class)).forEachOrdered(repository::save);
            }
        };
    }

}
