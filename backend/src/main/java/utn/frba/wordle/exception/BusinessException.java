package utn.frba.wordle.exception;

import utn.frba.wordle.model.enums.ErrorMessages;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ErrorMessages errorMessage) {
        super(errorMessage.getDescription());
    }
}
