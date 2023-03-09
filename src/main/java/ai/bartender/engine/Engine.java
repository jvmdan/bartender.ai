package ai.bartender.engine;

import ai.bartender.model.Prompt;
import ai.bartender.model.Response;

public interface Engine {

    Response request(Prompt p);

}
