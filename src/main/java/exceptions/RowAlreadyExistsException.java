package exceptions;

public class RowAlreadyExistsException extends RuntimeException {
    public RowAlreadyExistsException(String message) {
        super(message);
    }
}
