package de.balogha.footballint.dbrestservice.rest.handler;

import de.balogha.footballint.dbrestservice.exception.NoSuchEntityException;
import de.balogha.footballint.dbrestservice.model.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class MissingEntityExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(MissingEntityExceptionHandler.class);



    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public ApiError noSuchEntityHandler(HttpServletRequest req, NoSuchEntityException ex) throws IOException {
        log.warn("Resource requested by: {} {} does not exist",
                req.getMethod(),
                req.getRequestURI(),
                ex);
        return new ApiError(
                req.getRequestURI(),
                NOT_FOUND.value(),
                ex.getMessage());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public ApiError emptyResultDataAccessHandler(HttpServletRequest req, EmptyResultDataAccessException ex) {
        log.warn("Resource requested by: {} {} does not exist",
                req.getMethod(),
                req.getRequestURI(),
                ex);
        return new ApiError(
                req.getRequestURI(),
                NOT_FOUND.value(),
                "Requested resource does not exist");
    }

}
