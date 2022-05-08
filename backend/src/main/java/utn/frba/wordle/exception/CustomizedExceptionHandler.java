package utn.frba.wordle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpSession;
import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<Object> handleBusinessException(Exception ex, WebRequest req, HttpSession session) {

        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        response.setErrorCode(400);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SessionJWTException.class)
    public final ResponseEntity<Object> handleSessionException(Exception ex, WebRequest req, HttpSession session) {

        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        response.setErrorCode(401);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest req, HttpSession session) {

        ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), req.getDescription(false));
        response.setErrorCode(500);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}