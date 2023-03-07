package ai.bartender.exceptions;

import lombok.Getter;


@Getter
public class InvalidPromptException extends RuntimeException {

    private final String prompt;

    public InvalidPromptException(String prompt) {
        super("User prompt of \"" + prompt + "\" was invalid");
        this.prompt = prompt;
    }

    public InvalidPromptException(String prompt, Throwable causedBy) {
        super("User prompt of \"" + prompt + "\" was invalid", causedBy);
        this.prompt = prompt;
    }

}
