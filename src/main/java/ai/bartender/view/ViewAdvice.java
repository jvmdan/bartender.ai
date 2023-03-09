package ai.bartender.view;

import ai.bartender.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The ViewAdvice is a ControllerAdvice implementation that catches exceptions when thrown and
 * ensures that they are transformed to a suitable view for the end-user. This class will
 * catch any runtime exception and append the details to the model for rendering in the web view.
 *
 * @author Daniel Scarfe
 */
@ControllerAdvice
class ViewAdvice {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badRequest(RuntimeException ex, Model model) {
        model.addAttribute("error", ex);
        return "error";
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound(NotFoundException ex, Model model) {
        model.addAttribute("error", ex);
        return "error";
    }

}