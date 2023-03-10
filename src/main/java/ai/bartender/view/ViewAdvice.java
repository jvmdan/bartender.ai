package ai.bartender.view;

import ai.bartender.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * The ViewAdvice is a ControllerAdvice implementation that catches exceptions when thrown and
 * ensures that they are transformed to a suitable view for the end-user. This class will
 * catch any runtime exception and append the details to the model for rendering in the web view.
 *
 * @author Daniel Scarfe
 */
@ControllerAdvice
@Slf4j
class ViewAdvice {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badRequest(RuntimeException ex, Model model) {
        log.warn(ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "view";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String responseException(Exception ex, Model model) {
        log.warn(ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "view";
    }

}