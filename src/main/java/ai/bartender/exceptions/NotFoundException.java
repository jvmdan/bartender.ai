package ai.bartender.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final String resource;

    public NotFoundException(String resource) {
        super("Unable to locate resource \"" + resource + "\"");
        this.resource = resource;
    }

}
