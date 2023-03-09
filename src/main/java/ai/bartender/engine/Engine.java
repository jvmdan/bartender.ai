package ai.bartender.engine;

import ai.bartender.model.Prompt;
import ai.bartender.model.Response;

/**
 * An Engine represents an Artificial Intelligence (AI) that can be used to generate a Response object
 * from a given Prompt. Any implementation must specify a response type, representing the AI output.
 *
 * @param <R> the type of response we expect to receive from the AI.
 * @author Daniel Scarfe
 */
public interface Engine<R> {

    /**
     * Trigger the AI with a Prompt, and generate a corresponding Response.
     *
     * @param prompt the user-specified prompt representing an input to the AI.
     * @return a Response object containing our resulting AI output.
     */
    Response<R> respond(Prompt prompt);

    /**
     * Request the AI to refine a Response, with additional information provided as a Prompt.
     *
     * @param result a previous result, received from the AI.
     * @param prompt a new prompt to further refine the AI response.
     * @return a Response object containing our resulting AI output.
     */
    Response<R> refine(Response<R> result, Prompt prompt);

}
