package exceptions;

import models.Row;

public class RowAlreadyExistsException extends RuntimeException {
    public RowAlreadyExistsException(String message) {
        super(message);
    }
}
