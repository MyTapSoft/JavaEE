package Lesson1.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        System.out.println("NotFoundException");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    public NotFoundException(String message, Throwable cause) {
        System.out.println("NotFoundException");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, cause);
    }

    public NotFoundException(Throwable cause) {
        System.out.println("NotFoundException");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cause.getMessage());
    }
}
