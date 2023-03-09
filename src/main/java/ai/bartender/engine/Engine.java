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
     * Request that the AI assess whether the given prompt meets the content moderation guidelines.
     * This method is used to filter requests that may be deemed hateful, sexual, violent, etc.
     * <p/>
     * Note: There is nothing stopping the user prompting the AI regardless of the outcome of this
     * method, and any such checks must be done inside the implementation. Regardless, beware that
     * requesting inappropriate content may get the user banned from some public AI services.
     *
     * @param prompt the prompt we wish to validate against the moderation guidelines.
     * @return true or false; the prompt is deemed acceptable.
     */
    boolean moderate(Prompt prompt);

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
