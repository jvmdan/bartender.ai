package ai.bartender.exceptions;

import lombok.Getter;

@Getter
public class RecipeCreationException extends RuntimeException {

    private final String prompt;

    public RecipeCreationException(String prompt) {
        super("Unable to create \"" + prompt + "\" recipe");
        this.prompt = prompt;
    }

    public RecipeCreationException(String prompt, Throwable cause) {
        super("Unable to create \"" + prompt + "\" recipe", cause);
        this.prompt = prompt;
    }
    
}
