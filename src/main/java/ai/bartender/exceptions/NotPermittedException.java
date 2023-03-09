package ai.bartender.exceptions;

import lombok.Getter;

@Getter
public class NotPermittedException extends RuntimeException {

    private final String resource;

    public NotPermittedException(String resource) {
        super("Access to \"" + resource + "\" is not permitted");
        this.resource = resource;
    }

}
