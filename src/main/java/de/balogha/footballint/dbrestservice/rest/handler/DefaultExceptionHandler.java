package de.balogha.footballint.dbrestservice.rest.handler;

import de.balogha.footballint.dbrestservice.model.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Order(LOWEST_PRECEDENCE)
public class DefaultExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError unexpectedException(HttpServletRequest req, Exception ex) throws IOException {
        log.error("Unexpected exception while processing: {} {}",
                req.getMethod(),
                req.getRequestURI(),
                ex);
        return new ApiError(
                req.getRequestURI(),
                INTERNAL_SERVER_ERROR.value(),
                "An unexpected exception occurred");
    }
}
