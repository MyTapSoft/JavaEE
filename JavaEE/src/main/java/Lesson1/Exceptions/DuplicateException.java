package Lesson1.Exceptions;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Some parameters are invalid")
public class DuplicateException extends RuntimeException {

    private final static Logger log = Logger.getLogger(ResponseStatusHandler.class);

    public DuplicateException(String message) {
        super(message);
        log.error(message);

    }
}
