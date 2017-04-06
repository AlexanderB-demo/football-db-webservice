package de.balogha.footballint.dbrestservice.rest.handler;

import de.balogha.footballint.dbrestservice.model.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class BadInputExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(BadInputExceptionHandler.class);



    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(CONFLICT)
    @ResponseBody
    public ApiError dataIntegrityViolationHandler(HttpServletRequest req, DataIntegrityViolationException ex) throws IOException {
        log.warn("Request: {} {} violates database integrity constraint",
                req.getMethod(),
                req.getRequestURI(),
                ex);
        return new ApiError(
                req.getRequestURI(),
                CONFLICT.value(),
                "Request violated a database integrity constraint");
    }

    // Handle invalid @RequestBody (by bean validation) in requests to @RestController
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ApiError processValidationError(HttpServletRequest req, MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        log.warn("Invalid request body for request: {} {}",
                req.getMethod(),
                req.getRequestURI(),
                ex);
        return new ApiError(
                req.getRequestURI(),
                BAD_REQUEST.value(),
                "Invalid request body. Errors in fields: " + fieldErrors);
    }

    // Handle wrong @PathVariable data types in requests to @RestController
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ApiError processValidationError(HttpServletRequest req, MethodArgumentTypeMismatchException ex) {
        log.warn("Malformed path variables in request: {} {}",
                req.getMethod(),
                req.getRequestURI(),
                ex);
        return new ApiError(
                req.getRequestURI(),
                BAD_REQUEST.value(),
                "malformed request: " + ex.getMessage());
    }

    // Handle validation exceptions
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ApiError processValidationError(HttpServletRequest req, ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        log.warn("Invalid request body for request: {} {}",
                req.getMethod(),
                req.getRequestURI(),
                ex);
        return new ApiError(
                req.getRequestURI(),
                BAD_REQUEST.value(),
                "Request violates contraints: " + violations);
    }


}
