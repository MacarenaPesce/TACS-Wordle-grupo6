package utn.frba.wordle.exception;

import ch.qos.logback.classic.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import utn.frba.wordle.logging.WordleLogger;

import javax.servlet.http.HttpSession;
import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = WordleLogger.getLogger(CustomizedExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<ExceptionResponse> handleBusinessException(Exception ex, WebRequest req, HttpSession session) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false), httpStatus.value());

        logger.error("Error Response: {}", response);
        logger.debug("Stacktrace:", ex);

        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(SessionJWTException.class)
    public final ResponseEntity<ExceptionResponse> handleSessionException(Exception ex, WebRequest req, HttpSession session) {

        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false), httpStatus.value());

        logger.error("Error Response: {}", response);
        logger.debug("Stacktrace:", ex);

        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest req, HttpSession session) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false), httpStatus.value());

        logger.error("Error Response: {}", response);
        logger.debug("Stacktrace:", ex);

        return ResponseEntity.status(httpStatus).body(response);
    }
}