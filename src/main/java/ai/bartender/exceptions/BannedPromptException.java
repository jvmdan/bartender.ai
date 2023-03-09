package ai.bartender.exceptions;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Getter
public class BannedPromptException extends RuntimeException {

    /**
     * A list of words that will trigger a request to be rejected if any of the prompt contains a match.
     */
    private static final List<String> BANNED = Arrays.asList("TERROR", "SUICIDE");

    private final String prompt;

    public BannedPromptException(String prompt) {
        super("Prompt of \"" + prompt + "\" contains prohibited language");
        this.prompt = prompt;
    }

    /**
     * Compare our prompt against the list of banned words, ensuring our prompt does not contain a match.
     *
     * @param prompt the prompt we wish to validate against the banned word list.
     * @return true or false; does the prompt contain a match for a banned word?
     */
    public static boolean hasMatch(String prompt) {
        final String normalised = prompt.toUpperCase(Locale.ROOT);
        return BANNED.stream().anyMatch(normalised::contains);
    }

}
