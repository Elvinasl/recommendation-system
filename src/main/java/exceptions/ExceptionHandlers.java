package exceptions;

import exceptions.responses.Response;
import exceptions.responses.RowExistsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.ok(new Response(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(RowAlreadyExistsException.class)
    public ResponseEntity<RowExistsResponse> handleRowAlreadyExistsException(RowAlreadyExistsException exception) {
        return ResponseEntity.ok(new RowExistsResponse(exception.getMessage()));
    }
}
