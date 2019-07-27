package org.godseop.cherry.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.godseop.cherry.core.exception.CherryException;
import org.godseop.cherry.core.model.Error;
import org.godseop.cherry.core.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = {CherryException.class})
    public ResponseEntity<Result> cherryException(CherryException exception, WebRequest request) {
        Result result = new Result();

        log.debug("handling CherryException...{}", exception.getMessage());
        result.put(exception);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Result> exception(Exception exception, WebRequest request) {
        Result result = new Result();

        log.debug("handling Exception...{}", exception.getMessage());
        exception.printStackTrace();
        result.put(Error.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
