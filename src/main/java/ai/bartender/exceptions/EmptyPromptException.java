package ai.bartender.exceptions;

public class EmptyPromptException extends RuntimeException {

    public EmptyPromptException() {
        super("You must specify a prompt for the AI");
    }

}
