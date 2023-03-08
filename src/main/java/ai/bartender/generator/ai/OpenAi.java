package ai.bartender.generator.ai;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public class OpenAi implements Ai, InitializingBean {

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
    public String request(String prompt) {
        final List<ChatMessage> context = new ArrayList<>();
        context.add(new ChatMessage("system", pretext));
        context.add(new ChatMessage("user", prompt));
        final ChatCompletionRequest request = ChatCompletionRequest.builder()
                .messages(context)
                .model(model)
                .n(1)
                .temperature(0.5)
                .build();
        final List<ChatCompletionChoice> result = service.createChatCompletion(request).getChoices();
        return (result.size() > 0) ? result.get(0).getMessage().getContent() : "";
    }

    @Override
    public String getName() {
        return this.model;
    }

}
