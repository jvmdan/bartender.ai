package ai.bartender.model;

/**
 * A Response represents a piece of immutable data received as an output of the AI Engine.
 *
 * @param result the data received from the AI engine.
 * @param <T>    the type of response data we are dealing with.
 * @author Daniel Scarfe
 */
public record Response<T>(T result) {

}
