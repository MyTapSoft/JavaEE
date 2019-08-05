package Lesson1.Exceptions;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@ControllerAdvice
public class ResponseStatusHandler {
    private final static Logger log = Logger.getLogger(ResponseStatusHandler.class);

    @ExceptionHandler(value = BadRequestException.class)
    public ModelAndView badRequestHandler(HttpSession session, BadRequestException badRequest) {
        log.error("User ID: " + session.getAttribute("userId") + ". " + Arrays.toString(badRequest.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("400");
        modelAndView.addObject("error", badRequest.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ModelAndView unauthorizedHandler(UnauthorizedException unauthorized) {
        log.error(Arrays.toString(unauthorized.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("401");
        modelAndView.addObject("error", unauthorized.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(value = EntityExistsException.class)
    public ModelAndView entityExistsHandler(HttpSession session, EntityExistsException notExist) {
        log.error("User ID: " + session.getAttribute("userId") + ". " + Arrays.toString(notExist.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("404");
        modelAndView.addObject("error", notExist.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ModelAndView numberFormatHandler(HttpSession session, NumberFormatException numberFormat) {
        log.error("User ID: " + session.getAttribute("userId") + ". " + Arrays.toString(numberFormat.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("400");
        modelAndView.addObject("error", numberFormat.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView otherException(HttpSession session, Exception otherException) {
        log.error("User ID: " + session.getAttribute("userId") + ". " + Arrays.toString(otherException.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("500");
        modelAndView.addObject("error", otherException.getMessage());
        return modelAndView;
    }

}
