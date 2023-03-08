package ai.bartender.exceptions;

import lombok.Getter;


@Getter
public class BannedPromptException extends RuntimeException {

    private final String prompt;

    public BannedPromptException(String prompt) {
        super("Prompt of \"" + prompt + "\" contains prohibited language");
        this.prompt = prompt;
    }

}
