package ai.bartender.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * The PromptUtils class provides a number of public static methods for dealing with original
 */
public final class PromptUtils {

    /**
     * A list of words that will trigger a request to be rejected if any of the prompt contains a match.
     */
    private static final List<String> BANNED = Arrays.asList("TERROR", "SUICIDE");

    private PromptUtils() {
        // Private constructor to prevent instantiation of utility class.
    }

    /**
     * Strip any bad characters from the prompt string, capitalising the first letter of every word.
     *
     * @param original the original prompt string we wish to normalise.
     * @return a String with all bad characters removed, case corrected.
     */
    public static String normalise(String original) {
        final String stripped = original.replace(" and ", " & ")
                .replace(" + ", " & ")
                .replaceAll("[^A-Za-z0-9()\\[\\] '&]", "");
        final StringBuilder builder = new StringBuilder();
        boolean convertNext = true;
        for (int i = 0; i < stripped.length(); i++) {
            char currentChar = stripped.charAt(i);
            if (currentChar == ' ') {
                builder.append(' ');
                convertNext = false;
            } else if (convertNext && i != 0) {
                builder.append(Character.toLowerCase(currentChar));
            } else {
                builder.append(Character.toUpperCase(currentChar));
                convertNext = true;
            }
        }
        return builder.toString();
    }

    /**
     * Compare our prompt against the list of banned words, ensuring our prompt does not contain a match.
     *
     * @param prompt the prompt we wish to validate against the banned word list.
     * @return true or false; does the prompt contain a match for a banned word?
     */
    public static boolean isBanned(String prompt) {
        final String normalised = prompt.toUpperCase(Locale.ROOT);
        return BANNED.stream().anyMatch(normalised::contains);
    }

}
