package recommendator.exceptions;

import recommendator.exceptions.responses.Response;
import recommendator.exceptions.responses.RowExistsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;

@RestControllerAdvice
public class ExceptionHandlers {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(RowAlreadyExistsException.class)
    public ResponseEntity<RowExistsResponse> handleRowAlreadyExistsException(RowAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new RowExistsResponse(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Response> handleRowAlreadyExistsException(NoResultException exception) {
        return ResponseEntity.ok(new Response(exception.getMessage()));
    }
}
