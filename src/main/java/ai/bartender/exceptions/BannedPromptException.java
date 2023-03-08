package ai.bartender.exceptions;

import lombok.Getter;


@Getter
public class BannedPromptException extends RuntimeException {

    private final String prompt;

    public BannedPromptException(String prompt) {
        super("User prompt of \"" + prompt + "\" is not allowed!");
        this.prompt = prompt;
    }

}
