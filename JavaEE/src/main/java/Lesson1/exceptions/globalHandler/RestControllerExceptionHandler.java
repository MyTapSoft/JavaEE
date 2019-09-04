package Lesson1.exceptions.globalHandler;

import Lesson1.exceptions.DuplicateException;
import Lesson1.exceptions.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class RestControllerExceptionHandler {
    private final static Logger log = Logger.getLogger(ControllerAdviceExceptionHandler.class);


    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Object> duplicateExceptionHandler(DuplicateException exc) {
        log.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exc.getMessage());
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(NotFoundException exc) {
        log.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exc.getMessage());
    }
}
