package ai.bartender.engine;

import ai.bartender.exceptions.RecipeCreationException;
import ai.bartender.model.Prompt;
import ai.bartender.model.Recipe;
import ai.bartender.model.Response;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.moderation.Moderation;
import com.theokanning.openai.moderation.ModerationRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * A service for connecting to OpenAI's public API. This class requires the presence of an `openai.properties`
 * file containing a unique API token along with an initial AI prompt.
 *
 * @author Daniel Scarfe
 */
@Service
@Profile("openai")
@PropertySource(value = "classpath:openai.properties")
@Slf4j
public class OpenAIEngine implements Engine<Recipe>, InitializingBean {

    /**
     * The underlying API only supports a subset of OpenAI models. As such, we do a simple check upon
     * bean construction to ensure that the configured model is supported at the API level.
     */
    private static final List<String> SUPPORTED_MODELS = List.of("gpt-3.5-turbo", "gpt-3.5-turbo-0301");

    private OpenAiService service;

    @Value("${token}")
    public String token;

    @Value("${model:gpt-3.5-turbo}")
    public String model;

    @Value("${timeout:30s}")
    public Duration timeout;

    @Value("${pretext}")
    public String pretext;

    @Override
    public void afterPropertiesSet() {
        if (!SUPPORTED_MODELS.contains(model.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Unsupported OpenAI model specified: " + model);
        } else if (token.isBlank()) {
            throw new IllegalArgumentException("Unable to connect to OpenAI. You must specify an API token!");
        } else {
            this.service = new OpenAiService(token, timeout);
            log.info("Configured OpenAI with model: {} [timeout={}s]", model, timeout.getSeconds());
            log.info("Configured OpenAI with token: {}", token);
        }
    }

    @Override
    public boolean moderate(Prompt prompt) {
        log.info("Asserting \"{}\" meets the content moderation guidelines", prompt.recipeName());
        final ModerationRequest request = ModerationRequest.builder()
                .model("text-moderation-latest")
                .input("Create a recipe for a cocktail named \"" + prompt.recipeName() + "\"")
                .build();
        final List<Moderation> results = service.createModeration(request).getResults();
        return results.stream().anyMatch(Moderation::isFlagged);
    }

    @Override
    public Response<Recipe> respond(Prompt prompt) {
        // Convert the user prompt into a natural language request for OpenAI to process.
        log.info("Processing request to build \"{}\" recipe", prompt.recipeName());
        final String name = prompt.recipeName();

        // Construct the user request, providing the pretext to the system to help scope the AI.
        final List<ChatMessage> context = new ArrayList<>(2);
        context.add(new ChatMessage("system", pretext));
        context.add(new ChatMessage("user", "Create a recipe for a cocktail named \"" + name + "\"."));
        final ChatCompletionRequest request = ChatCompletionRequest.builder()
                .messages(context).model(model).n(1).temperature(0.5).build();

        // Extract the resulting ingredients & directions from the AI response.
        final Recipe recipe = new Recipe(name, "generated");
        final List<ChatCompletionChoice> responses = service.createChatCompletion(request).getChoices();
        final ChatMessage message = responses.get(0).getMessage();
        final List<String> i = responses.isEmpty() ?
                Collections.emptyList() : extract(message, (l) -> l.startsWith("-"));
        final List<String> d = responses.isEmpty() ?
                Collections.emptyList() : extract(message, (l) -> l.length() > 0 && Character.isDigit(l.charAt(0)));

        // The AI might reject a given request for any reason. Throw a useful error to the client.
        if (i.isEmpty() || d.isEmpty()) {
            throw new RecipeCreationException(prompt);
        } else {
            recipe.addIngredients(i);
            recipe.addDirections(d);
        }

        // Return the Response object containing our Recipe instance to the caller.
        return new Response<>(recipe);
    }

    /**
     * A package-private static helper method for extracting relevant parts of an OpenAI response.
     *
     * @param response  the response we have received from OpenAI.
     * @param predicate the predicate we wish to use to assert a match.
     * @return a list of Strings representing any & all lines that meet our predicate.
     */
    static List<String> extract(ChatMessage response, Predicate<String> predicate) {
        final List<String> results = new ArrayList<>();
        response.getContent().lines().filter(predicate)
                .forEachOrdered(l -> results.add(l.substring(l.indexOf(" ") + 1)));
        return results;
    }

    @Override
    public Response<Recipe> refine(Response<Recipe> result, Prompt prompt) {
        throw new UnsupportedOperationException(); // TODO!
    }

}
