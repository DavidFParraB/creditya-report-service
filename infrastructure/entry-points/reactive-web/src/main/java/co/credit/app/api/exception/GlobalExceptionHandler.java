package co.credit.app.api.exception;

import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import co.credit.app.api.dto.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneralException(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);

        String friendlyMessage = "An unexpected error occurred. Please try again later";
        ErrorResponse errorResponse = new ErrorResponse(friendlyMessage, null);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneralException(IllegalArgumentException ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), null);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationError(ResponseStatusException ex) {
        log.error("Validation error occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), null);
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }

    @ExceptionHandler(NumberFormatException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNumberFormatException(NumberFormatException ex) {
      log.error("Invalid input format: {}", ex.getMessage(), ex);

      String friendlyMessage = "Invalid input format. Please provide valid data.";
      ErrorResponse errorResponse = new ErrorResponse(friendlyMessage, null);
      return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

}