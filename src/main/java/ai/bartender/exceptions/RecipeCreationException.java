package ai.bartender.exceptions;

import ai.bartender.model.Prompt;
import lombok.Getter;

@Getter
public class RecipeCreationException extends RuntimeException {

    private final Prompt prompt;

    public RecipeCreationException(Prompt prompt) {
        super("Unable to create \"" + prompt.recipeName() + "\" recipe");
        this.prompt = prompt;
    }
    
}
