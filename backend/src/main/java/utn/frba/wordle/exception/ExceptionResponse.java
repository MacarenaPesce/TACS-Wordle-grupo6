package utn.frba.wordle.exception;

import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
public class ExceptionResponse {

    private final int errorCode;
    private final Date timestamp;
    private final String message;
    private final String details;

    public ExceptionResponse(Date timestamp, String message, String details, int errorCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.errorCode = errorCode;
    }
}