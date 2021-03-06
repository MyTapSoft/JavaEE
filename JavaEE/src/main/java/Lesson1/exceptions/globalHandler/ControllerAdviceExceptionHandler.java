package Lesson1.exceptions.globalHandler;

import Lesson1.exceptions.BadRequestException;
import Lesson1.exceptions.InternalServerException;
import Lesson1.exceptions.UnauthorizedException;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityExistsException;
import java.util.Arrays;

@ControllerAdvice
public class ControllerAdviceExceptionHandler {
    private final static Logger log = Logger.getLogger(ControllerAdviceExceptionHandler.class);

    @ExceptionHandler(value = BadRequestException.class)
    public ModelAndView badRequestHandler(BadRequestException badRequest) {
        log.error(Arrays.toString(badRequest.getStackTrace()));
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
    public ModelAndView entityExistsHandler(EntityExistsException notExist) {
        log.error(Arrays.toString(notExist.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("404");
        modelAndView.addObject("error", notExist.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ModelAndView numberFormatHandler(NumberFormatException numberFormat) {
        log.error(Arrays.toString(numberFormat.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("400");
        modelAndView.addObject("error", numberFormat.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(value = InternalServerException.class)
    public ModelAndView internalServerException(InternalServerException internalServer) {
        log.error(Arrays.toString(internalServer.getStackTrace()));
        ModelAndView modelAndView = new ModelAndView("500");
        modelAndView.addObject("error", internalServer.getMessage());
        return modelAndView;
    }


}
